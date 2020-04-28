package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SimplePlatformLauncher implements SpellLauncher<SimplePlatformLauncher> {

    private Set<Vector3i> placedBlocks;

    public SimplePlatformLauncher() {
        this.placedBlocks = new HashSet<>();
    }

    @Override
    public LaunchResult commonLaunch(SpellCreationData<SimplePlatformLauncher> data, Player caster) {

        PropertyMap map = data.getPropertyMap();
        int radius = map.getProperty("radius", Integer.class);
        int time = map.getProperty("time", Integer.class);
        TimeUnit unit = map.getProperty("time_unit", TimeUnit.class);

        Vector3i center = Optional.ofNullable(map.getProperty("location", Vector3d.class))
                .map(Vector3d::toInt)
                .orElse(
                        YggdrasilMath.headRotationToDirection(caster.getHeadRotation())
                                .normalize()
                                .mul(radius)
                                .add(caster.getPosition())
                                .toInt());

        World world = caster.getWorld();
        createPlatform(center, radius, world);
        scheduleDeletion(time, unit, world);
        return LaunchResult.SUCCESS;
    }

    private void createPlatform(Vector3i center, int radius, World world) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                Vector3i currentPos = center.add(dx, 0, dz);
                if (currentPos.distance(center) - 0.5 > radius) {
                    continue;
                }
                if (world.getBlockType(currentPos) != BlockTypes.AIR) {
                    continue;
                }
                world.setBlockType(currentPos, BlockTypes.DIRT);
                placedBlocks.add(currentPos);
            }
        }
    }

    private void scheduleDeletion(int time, TimeUnit unit, World world) {
        Task.builder()
                .delay(time, unit)
                .execute(() -> this.removeBlocks(world))
                .submit(YggdrasilMain.getPlugin());
    }

    private void removeBlocks(World world) {
        for (Vector3i pos : placedBlocks) {
            if (world.getBlockType(pos) == BlockTypes.DIRT) {
                world.setBlockType(pos, BlockTypes.AIR);
            }
        }
    }
}

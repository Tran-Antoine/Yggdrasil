package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.spongepowered.api.block.BlockTypes.*;

public class FreezingSpellLauncher implements SpellLauncher<FreezingSpellLauncher> {

    private Vector3i center;

    @Override
    public LaunchResult commonLaunch(SpellCreationData<FreezingSpellLauncher> data, Player caster) {

        PropertyMap map = data.getPropertyMap();
        int time = map.getProperty("time", Integer.class);
        int radius = map.getProperty("radius", Integer.class);
        this.center = caster.getPosition().toInt();
        World world = caster.getWorld();

        createArea(center, radius, world, ICE, FLOWING_WATER, WATER);

        if(map.getPropertyOrElse("further_cancel", true)) {
            scheduleMeltIce(time, radius, world);
        }

        return LaunchResult.SUCCESS;
    }

    private void scheduleMeltIce(int time, int radius, World world) {
        Task.builder()
                .delay(time, TimeUnit.SECONDS)
                .execute(() -> createArea(center, radius, world, WATER, ICE))
                .submit(YggdrasilMain.getPlugin());
    }

    private void createArea(Vector3i center, int radius, World world, BlockType newType, BlockType... old) {
        List<BlockType> matches = Arrays.asList(old);
        for(long dx = -radius; dx <= radius; dx++) {
            for(long dy = -radius; dy <= radius; dy++) {
                for(long dz = -radius; dz <= radius; dz++) {
                    Vector3i pos = center.add(dx, dy, dz);
                    BlockType type = world.getBlockType(pos);
                    if(matches.contains(type)) {
                        world.setBlockType(pos, newType);
                    }
                }
            }
        }
    }
}

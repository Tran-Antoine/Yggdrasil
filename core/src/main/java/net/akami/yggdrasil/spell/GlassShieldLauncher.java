package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.utils.SpaceScanner;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GlassShieldLauncher implements SpellLauncher<GlassShieldLauncher> {

    private Set<Vector3i> blocksPlaced;

    public GlassShieldLauncher() {
        this.blocksPlaced = new HashSet<>();
    }

    @Override
    public LaunchResult commonLaunch(SpellCreationData<GlassShieldLauncher> data, Player caster) {

        PropertyMap map = data.getPropertyMap();
        float radius = map.getPropertyOrElse("radius", 1);
        World world = caster.getWorld();

        createShield(
                caster.getPosition().add(0, 1, 0),
                YggdrasilMath.headRotationToDirection(caster.getHeadRotation())
                        .mul(5 * (1 + 0.1*radius)),
                radius,
                world);

        scheduleDestruction(map.getPropertyOrElse("time", 5), world);
        return LaunchResult.SUCCESS;
    }

    private void createShield(Vector3d center, Vector3d dir, float shieldRadius, World world) {

        SpaceScanner scanner = new SpaceScanner();
        int sphereRadius = (int) dir.length();
        for(int i = 0; i < 3; i++) {
            scanner.queueIteration(-sphereRadius, sphereRadius, 1);
        }
        scanner.shift(Arrays.asList(center.getX(), center.getY(), center.getZ()));
        scanner.addFilter((values) -> areCloseEnough(center, sphereRadius, values));
        scanner.addFilter((values) -> areWithinReach(center.add(dir), shieldRadius, values));
        scanner.addAction((values) -> createBlocks(values, world));
        scanner.run();
    }

    private boolean areCloseEnough(Vector3d center, float radius, List<Double> values) {
        Vector3d pos = new Vector3d(values.get(0), values.get(1), values.get(2));
        return Math.abs(pos.distance(center) - radius) < 0.5;
    }

    private boolean areWithinReach(Vector3d center, float radius, List<Double> values) {
        Vector3d pos = new Vector3d(values.get(0), values.get(1), values.get(2));
        return pos.distance(center) <= radius + 0.5;
    }

    private void createBlocks(List<Double> values, World world) {
        Vector3i pos = new Vector3d(values.get(0), values.get(1), values.get(2)).toInt();
        if(world.getBlockType(pos) == BlockTypes.AIR) {
            world.setBlockType(pos, BlockTypes.GLASS);
            blocksPlaced.add(pos);
        }
    }

    private void scheduleDestruction(int time, World world) {
        Task.builder()
                .delay(time, TimeUnit.SECONDS)
                .execute(() -> destroyBlocks(world))
                .submit(YggdrasilMain.getPlugin());
    }

    private void destroyBlocks(World world) {
        for(Vector3i pos : blocksPlaced) {
            if(world.getBlockType(pos) == BlockTypes.GLASS) {
                world.setBlockType(pos, BlockTypes.AIR);
            }
        }
    }

}

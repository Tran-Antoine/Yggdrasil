package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.explosive.Explosive;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class IncendiaSpellLauncher implements SpellLauncher {

    @Override
    public LaunchResult commonLaunch(SpellCreationData data, Player caster) {

        PropertyMap map = data.getPropertyMap();
        Vector3d center = map.getProperty("location", Vector3d.class);
        int fireRadius = map.getProperty("radius", Integer.class);
        int explosionRadius = map.getPropertyOrElse("explosion_radius", 0);
        World world = caster.getWorld();

        if(explosionRadius != 0) {
            createExplosion(world, explosionRadius, center);
        }
        Task.builder()
                .delay(250, TimeUnit.MILLISECONDS)
                .execute(() -> createFireArea(world, fireRadius, center))
                .submit(Sponge.getPluginManager().getPlugin("yggdrasil").get());
        return LaunchResult.SUCCESS;
    }

    private void createFireArea(World world, int radius, Vector3d center) {
        Random random = new Random();
        for(int dx = -radius; dx <= radius; dx++) {
            for(int dy = -10; dy <= 5; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    Vector3i fireLoc = new Vector3i(
                            center.getFloorX() + dx,
                            center.getFloorY() + dy,
                            center.getFloorZ() + dz);
                    if(world.getBlockType(fireLoc) == BlockTypes.AIR && random.nextFloat() < 0.6) {
                        world.setBlockType(fireLoc, BlockTypes.FIRE);
                    }
                }
            }
        }
    }

    private void createExplosion(World world, int radius, Vector3d center) {
        Explosive tnt = (Explosive) world.createEntity(EntityTypes.PRIMED_TNT, center);
        tnt.offer(Keys.EXPLOSION_RADIUS, Optional.of(radius));
        world.spawnEntity(tnt);
        tnt.detonate();
    }
}

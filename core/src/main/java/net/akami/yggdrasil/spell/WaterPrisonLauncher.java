package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.api.life.LivingEntity;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WaterPrisonLauncher implements SpellLauncher<WaterPrisonLauncher> {

    private static final BlockType ALL = null;
    private List<UUID> trappedEntities;
    private int radius;
    private Vector3d center;
    private SpellCreationData data;

    public WaterPrisonLauncher() {
        this.trappedEntities = new ArrayList<>();
    }

    @Override
    public void commonLaunch(SpellCreationData data, Player caster) {

        PropertyMap map = data.getPropertyMap();
        this.data = data;
        this.radius = map.getProperty("radius", Integer.class);
        this.center = map.getProperty("position", Vector3d.class);
        int lifeSpan = map.getProperty("life_span", Integer.class);
        World world = caster.getWorld();
        Object plugin = Sponge.getPluginManager().getPlugin("yggdrasil").get();

        createPrison(radius, world);
        register(plugin);
        Task.builder()
                .delay(lifeSpan, TimeUnit.SECONDS)
                .execute(() -> removeWater(radius, world))
                .submit(plugin);
    }

    @Listener
    public void onMove(MoveEntityEvent event) {
        Entity target = event.getTargetEntity();

        if(!(target instanceof LivingEntity)) {
            return;
        }

        Vector3d position = target.getLocation().getPosition();
        if(position.distance(center) > radius) {
            checkClearEffects(target);
        } else {
            depriveEntity(target);
            trappedEntities.add(target.getUniqueId());
        }
    }

    private void checkClearEffects(Entity target) {
        if(!trappedEntities.contains(target.getUniqueId())) {
            return;
        }
        // TODO add the rest according to the newest api changes
    }

    private void depriveEntity(Entity target) {
        if(trappedEntities.contains(target.getUniqueId())) {
            return;
        }
        // TODO add the change of left air (two bubbles)
    }

    private void register(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    private void removeWater(int radius, World world) {
        createBlocks(radius, world, BlockTypes.AIR, BlockTypes.WATER);
        Sponge.getEventManager().unregisterListeners(this);
    }

    private void createPrison(int radius, World world) {
        createBlocks(radius, world, BlockTypes.WATER, ALL);
    }

    private void createBlocks(int radius, World world, BlockType newType, BlockType toReplace) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    createBlock(center, radius, dx, dy, dz, world, newType, toReplace);
                }
            }
        }
    }

    private void createBlock(Vector3d center, int radius, int dx, int dy, int dz,
                             World world, BlockType newType, BlockType toReplace) {
        Vector3i pos = center.add(dx, dy, dz).toInt();
        if(pos.length() > radius) {
            return;
        }
        if(toReplace == null || world.getBlockType(pos) == toReplace) {
            world.setBlockType(pos, newType);
        }
    }

    public List<UUID> getTrappedEntities() {
        return trappedEntities;
    }
}

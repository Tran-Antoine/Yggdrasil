package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.property.block.MatterProperty;
import org.spongepowered.api.data.property.block.MatterProperty.Matter;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WaterPrisonLauncher implements SpellLauncher<WaterPrisonLauncher> {

    private static final BlockType ALL = null;
    private List<UUID> trappedEntities;
    private int radius;
    private Vector3i center;
    private SpellCreationData data;

    public WaterPrisonLauncher() {
        this.trappedEntities = new ArrayList<>();
    }

    @Override
    public LaunchResult commonLaunch(SpellCreationData data, Player caster) {

        PropertyMap map = data.getPropertyMap();
        this.data = data;
        this.radius = map.getProperty("radius", Integer.class);
        this.center = map.getProperty("location", Vector3d.class).toInt();
        int lifeSpan = map.getProperty("life_span", Integer.class);
        World world = caster.getWorld();
        Object plugin = Sponge.getPluginManager().getPlugin("yggdrasil").get();

        createPrison(radius, world);
        register(plugin);
        Task.builder()
                .delay(lifeSpan, TimeUnit.SECONDS)
                .execute(() -> removeWater(radius, world))
                .submit(plugin);
        return LaunchResult.SUCCESS;
    }

    @Listener
    public void onMove(MoveEntityEvent event) {
        Entity target = event.getTargetEntity();

        if(!(target instanceof Living)) {
            return;
        }

        Vector3d position = target.getLocation().getPosition();
        if(position.distance(center.toDouble()) > radius) {
            checkClearEffects(target);
        } else {
            depriveEntity((Living) target);
            trappedEntities.add(target.getUniqueId());
        }
    }

    @Listener
    public void stopLiquidFlow(ChangeBlockEvent.Place event) {
        BlockSnapshot snapshot = event.getTransactions().get(0).getFinal();
        if(snapshot.getPosition().distance(center) > radius + 1.5) {
            return;
        }

        Optional<MatterProperty> matter = snapshot.getState().getProperty(MatterProperty.class);
        if (matter.isPresent() && matter.get().getValue() == Matter.LIQUID) {
            event.setCancelled(true);
        }
    }

    private void checkClearEffects(Entity target) {
        if(!trappedEntities.contains(target.getUniqueId())) {
            return;
        }
        trappedEntities.remove(target.getUniqueId());
        // TODO add the rest according to the newest api changes
    }

    private void depriveEntity(Living target) {
        if(trappedEntities.contains(target.getUniqueId())) {
            return;
        }
        target.offer(Keys.REMAINING_AIR, 40);
    }

    private void register(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    private void removeWater(int radius, World world) {
        createSphere(radius, world, BlockTypes.AIR, BlockTypes.WATER);
        createSphere(radius, world, BlockTypes.AIR, BlockTypes.FLOWING_WATER);
        Sponge.getEventManager().unregisterListeners(this);
    }

    private void createPrison(int radius, World world) {
        createSphere(radius, world, BlockTypes.WATER, ALL);
    }

    private void createSphere(int radius, World world, BlockType newType, BlockType toReplace) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    createBlock(center, radius, dx, dy, dz, world, newType, toReplace);
                }
            }
        }
        System.out.println("Successfully created sphere");
    }

    private void createBlock(Vector3i center, int radius, int dx, int dy, int dz,
                             World world, BlockType newType, BlockType toReplace) {
        Vector3i pos = center.add(dx, dy, dz);
        if(pos.distance(center) > radius + 0.5) {
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

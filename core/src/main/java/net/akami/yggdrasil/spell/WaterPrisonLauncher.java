package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.YggdrasilMain;
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
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class WaterPrisonLauncher implements SpellLauncher<WaterPrisonLauncher> {

    private static final BlockType ALL = null;
    private Set<UUID> trappedEntities;
    private int radius;
    private Vector3i center;
    private SpellCreationData<WaterPrisonLauncher> data;
    private int lifeSpan;
    private PotionEffect slownessEffect;
    private boolean dead = false;

    public WaterPrisonLauncher() {
        this.trappedEntities = new HashSet<>();
    }

    @Override
    public LaunchResult commonLaunch(SpellCreationData<WaterPrisonLauncher> data, Player caster) {

        PropertyMap map = data.getPropertyMap();
        this.data = data;
        this.radius = map.getProperty("radius", Integer.class);
        this.center = map.getProperty("location", Vector3d.class).toInt();
        this.lifeSpan = map.getProperty("life_span", Integer.class);
        setSlownessEffect();
        World world = caster.getWorld();
        Object plugin = YggdrasilMain.getPlugin();

        createParticles(world);
        createPrison(radius, world);
        register(plugin);
        Task.builder()
                .delay(lifeSpan, TimeUnit.SECONDS)
                .execute(() -> removeWater(radius, world))
                .submit(plugin);
        return LaunchResult.SUCCESS;
    }

    private void createParticles(World world) {
        world.spawnParticles(ParticleEffect.builder()
                .type(ParticleTypes.WATER_DROP)
                .quantity(30)
                .offset(new Vector3d(0.6, 0.6, 0.6))
                .build(), center.toDouble());
    }

    private void setSlownessEffect() {
        this.slownessEffect = PotionEffect.builder()
                .potionType(PotionEffectTypes.SLOWNESS)
                .duration(lifeSpan)
                .build();
    }

    @Listener
    public void onMove(MoveEntityEvent event) {
        Entity target = event.getTargetEntity();

        if(!(target instanceof Living)) {
            return;
        }

        Vector3d position = target.getLocation().getPosition();
        if(position.distance(center.toDouble()) > radius) {
            clearEffects(target);
        } else {
            depriveEntity((Living) target);
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

    private void clearEffects(Entity target) {
        UUID targetID = target.getUniqueId();
        if(!trappedEntities.contains(targetID)) {
            return;
        }
        data.restoreSpellAccess((magicUser) -> magicUser.getUUID().equals(targetID));
        setEffects(target, List::remove);
    }

    private void depriveEntity(Living target) {
        if(trappedEntities.contains(target.getUniqueId())) {
            return;
        }
        trappedEntities.add(target.getUniqueId());
        target.offer(Keys.REMAINING_AIR, 40);
        setEffects(target, List::add);
        data.excludeTargetSpells();
    }

    private void setEffects(Entity target, BiConsumer<List<PotionEffect>, PotionEffect> action) {
        List<PotionEffect> effects = target.get(Keys.POTION_EFFECTS).orElse(new ArrayList<>(1));
        action.accept(effects, slownessEffect);
        target.offer(Keys.POTION_EFFECTS, effects);
    }

    private void register(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    private void removeWater(int radius, World world) {
        createSphere(radius, world, BlockTypes.AIR, BlockTypes.WATER);
        createSphere(radius, world, BlockTypes.AIR, BlockTypes.FLOWING_WATER);
        this.dead = true;
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
    }

    private void createBlock(Vector3i center, int radius, int dx, int dy, int dz,
                             World world, BlockType newType, BlockType toReplace) {
        Vector3i pos = center.add(dx, dy, dz);
        if(pos.distance(center) > radius + 0.5) {
            return;
        }
        if(toReplace == null || world.getBlockType(pos) == toReplace) {
            world.setBlockType(pos, newType);
            System.out.println("Successfully placed block of " + newType.getName());
        }
    }

    public Set<UUID> getTrappedEntities() {
        return trappedEntities;
    }

    public Vector3i getCenter() {
        return center;
    }

    public boolean isSpellDead() {
        return dead;
    }
}

package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.ArmorEquipable;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MistSpellLauncher implements SpellLauncher<MistSpellLauncher> {

    private Set<UUID> rides;
    private Set<UUID> trappedEntities;
    private Vector3i center;
    private double radius;
    private Iterator<UUID> iterator;
    private World world;

    public MistSpellLauncher() {
        this.rides = new HashSet<>();
        this.trappedEntities = new HashSet<>();
    }

    @Override
    public LaunchResult commonLaunch(SpellCreationData<MistSpellLauncher> data, Player caster) {

        PropertyMap map = data.getPropertyMap();
        this.center = map.getProperty("location", Vector3d.class).toInt();
        double factor = 2.5;
        this.radius = map.getProperty("radius", Integer.class) * factor;
        this.world = caster.getWorld();
        Random random = new Random();
        for (double dx = -radius; dx <= radius; dx+=factor) {
            for (double dy = 0; dy <= 2 * factor; dy+=factor) {
                for (double dz = -radius; dz <= radius; dz+=factor) {
                    double flatDistance = Math.sqrt(dx*dx + dz*dz);
                    if(flatDistance > radius) continue;

                    if(Math.abs(flatDistance - radius) < 2.5 || dy == 0 || dy >= 2 * factor
                            || random.nextFloat() > 0.85) {
                        summonMistPiece(center, dx, dy, dz, world, dy == 0);
                    }
                }
            }
        }
        this.iterator = rides.iterator();
        scheduleDestruction();
        register();
        return LaunchResult.SUCCESS;
    }

    @Listener
    public void onEntityRemoved(DestructEntityEvent event) {
        Entity entity = event.getTargetEntity();
        UUID id = entity.getUniqueId();
        if(rides.contains(id)) {
            entity.getPassengers().forEach(Entity::remove);
        }
    }

    @Listener
    public void onMove(MoveEntityEvent event) {
        Entity target = event.getTargetEntity();

        if(!(target instanceof Player)) {
            return;
        }
        ArmorEquipable armorEquipable = (ArmorEquipable) target;

        Vector3d position = target.getLocation().getPosition();
        if(position.distance(center.toDouble()) < radius) {
            addPumpkin(armorEquipable);
        } else {
            removePumpkin(armorEquipable);
        }
    }

    private void addPumpkin(ArmorEquipable target) {
        if(trappedEntities.contains(target.getUniqueId())) {
            return;
        }
        target.setHelmet(ItemStack.of(ItemTypes.PUMPKIN));
        trappedEntities.add(target.getUniqueId());
    }

    private void removePumpkin(ArmorEquipable target) {
        if(!trappedEntities.contains(target.getUniqueId())) {
            return;
        }
        target.setHelmet(null);
        trappedEntities.remove(target.getUniqueId());
    }

    private void register() {
        Sponge.getEventManager().registerListeners(YggdrasilMain.getPlugin(), this);
    }

    private void unregister() {
        trappedEntities.forEach(id -> Sponge.getServer().getPlayer(id).ifPresent(p -> p.setHelmet(null)));
        Sponge.getEventManager().unregisterListeners(this);
    }

    private void scheduleDestruction() {
        Task.builder()
                .interval(500, TimeUnit.MILLISECONDS)
                .execute(this::removeEntity)
                .submit(YggdrasilMain.getPlugin());
    }

    private void removeEntity(Task task) {
        if(!iterator.hasNext()) {
            task.cancel();
            unregister();
            return;
        }
        UUID target = iterator.next();
        iterator.remove();
        world.getEntity(target).ifPresent(ent -> {
            ent.getPassengers().forEach(Entity::remove);
            ent.remove();
        });
    }

    private void summonMistPiece(Vector3i location, double dx, double dy, double dz, World world, boolean bottom) {
        Vector3d targetLoc = location.toDouble().add(dx, dy, dz);
        if(world.getBlockType(targetLoc.toInt()) != BlockTypes.AIR) {
            return;
        }

        ArmorStand armorStand = (ArmorStand) world.createEntity(EntityTypes.ARMOR_STAND, targetLoc);
        armorStand.setHelmet(ItemStack.builder()
                .itemType(ItemTypes.SNOW)
                .build());
        armorStand.offer(Keys.INVISIBLE, true);
        Entity fireBall = world.createEntity(EntityTypes.SMALL_FIREBALL, targetLoc);
        fireBall.offer(Keys.INVISIBLE, true);
        //arrow.offer(Keys.VANISH, true);
        //arrow.offer(Keys.VANISH_IGNORES_COLLISION, true);
        if(!bottom) {
            fireBall.offer(Keys.HAS_GRAVITY, false);
        }

        fireBall.addPassenger(armorStand);

        world.spawnEntity(armorStand);
        world.spawnEntity(fireBall);

        rides.add(fireBall.getUniqueId());
    }
}

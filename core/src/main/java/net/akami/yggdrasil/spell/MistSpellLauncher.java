package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MistSpellLauncher implements SpellLauncher<MistSpellLauncher> {

    private Set<UUID> armorStands;
    private World world;

    public MistSpellLauncher() {
        this.armorStands = new HashSet<>();
    }

    @Override
    public LaunchResult commonLaunch(SpellCreationData<MistSpellLauncher> data, Player caster) {

        PropertyMap map = data.getPropertyMap();
        Vector3i location = map.getProperty("location", Vector3d.class).toInt();
        double factor = 2.5;
        double radius = map.getProperty("radius", Integer.class) * factor;
        this.world = caster.getWorld();
        Random random = new Random();
        for (double dx = -radius; dx <= radius; dx+=factor) {
            for (double dy = -factor; dy <= 2 * factor; dy+=factor) {
                for (double dz = -radius; dz <= radius; dz+=factor) {
                    if(isOnEdge(dx, dy, dz, radius)|| random.nextFloat() > 0.65) {
                        summonMistPiece(location, dx, dy, dz, world);
                    }
                }
            }
        }
        scheduleNoGravity();
        scheduleDestruction();
        return LaunchResult.SUCCESS;
    }

    private boolean isOnEdge(double dx, double dy, double dz, double radius) {
        return Math.abs(dx) == radius || dy == -2.5 || dy == 5 || Math.abs(dz) == radius;
    }

    private void scheduleNoGravity() {
        Task.builder()
                .interval(100, TimeUnit.MILLISECONDS)
                .execute(this::setAcceleration)
                .submit(YggdrasilMain.getPlugin());
    }

    private void scheduleDestruction() {
        Task.builder()
                .interval(500, TimeUnit.MILLISECONDS)
                .execute(this::removeEntity)
                .submit(YggdrasilMain.getPlugin());
    }

    private void setAcceleration() {
        /*this.armorStands = armorStands
                .stream()
                .filter((id) -> world.getEntity(id).isPresent())
                .collect(Collectors.toSet());
        List<Entity> entities = armorStands
                .stream()
                .map(id -> world.getEntity(id).get())
                .collect(Collectors.toList());
        for(Entity entity : entities) {
            entity.offer(Keys.FLYING_SPEED, 0.0d);
        }*/
    }

    private void removeEntity(Task task) {
        Iterator<UUID> iterator = armorStands.iterator();
        if(!iterator.hasNext()) {
            task.cancel();
            return;
        }
        UUID target = iterator.next();
        armorStands.remove(target);
        world.getEntity(target).ifPresent(Entity::remove);
    }

    private void summonMistPiece(Vector3i location, double dx, double dy, double dz, World world) {
        Vector3d targetLoc = location.toDouble().add(dx, dy, dz);
        if(world.getBlockType(targetLoc.toInt()) != BlockTypes.AIR) {
            return;
        }

        ArmorStand armorStand = (ArmorStand) world.createEntity(EntityTypes.ARMOR_STAND, targetLoc);
        armorStand.setHelmet(ItemStack.builder()
                .itemType(ItemTypes.SNOW)
                .build());
        armorStand.offer(Keys.INVISIBLE, true);
        Entity arrow = world.createEntity(EntityTypes.SMALL_FIREBALL, targetLoc);
        //arrow.offer(Keys.VANISH, true);
        //arrow.offer(Keys.VANISH_IGNORES_COLLISION, true);
        arrow.offer(Keys.HAS_GRAVITY, false);

        arrow.addPassenger(armorStand);

        world.spawnEntity(armorStand);
        world.spawnEntity(arrow);
    }
}

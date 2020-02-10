package net.akami.yggdrasil.api.item;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.input.UUIDHolder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.entity.projectile.arrow.Arrow;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.CollideEvent;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

public abstract class InteractiveAimingItem implements InteractiveItem {

    private World world;
    protected UUIDHolder holder;
    private UUID arrowShooter;
    private UUID arrowID;

    public InteractiveAimingItem(UUIDHolder holder) {
        this.holder = holder;
        Object plugin = Sponge.getPluginManager().getPlugin("yggdrasil").get();
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    protected abstract void applyEffect(Vector3d location);

    @Listener
    public void onProjectileLaunched(SpawnEntityEvent event) {
        Entity entity = event.getEntities().get(0);
        if(entity.getType() != EntityTypes.TIPPED_ARROW) {
            return;
        }
        Projectile projectile = (Projectile) entity;
        ProjectileSource shooter = projectile.getShooter();
        if(!(shooter instanceof Entity)) {
            return;
        }
        Entity entityShooter = (Entity) shooter;
        if(entityShooter.getUniqueId().equals(holder.getUUID()) && arrowID == null) {
            this.world = projectile.getWorld();
            this.arrowID = projectile.getUniqueId();
            this.arrowShooter = entityShooter.getUniqueId();
        }
    }

    @Listener
    public void onProjectileLanded(CollideEvent.Impact event, @First Arrow arrow) {
        if(arrow == null || world == null || arrowShooter == null
                || !arrow.getUniqueId().equals(arrowID)) {
            return;
        }
        Vector3d loc = event.getImpactPoint().getPosition();
        System.out.println("Impact location : " + loc);
        event.setCancelled(true);
        arrow.remove();

        reset();
        applyEffect(loc);
    }

    @Override
    public void onLeftClicked(InteractEvent event, GameItemClock clock) { }

    @Override
    public void onRightClicked(InteractEvent event, GameItemClock clock) {

        if(arrowID == null || world == null || arrowShooter == null) {
            return;
        }
        System.out.println("Stopping arrow's trajectory");
        Optional<Entity> optEntity = world.getEntity(arrowID);
        if(!optEntity.isPresent()) {
            return;
        }
        Entity arrow = optEntity.get();
        Vector3d position = arrow.getLocation().getPosition();
        arrow.remove();

        reset();
        applyEffect(position);
    }

    private void reset() {
        this.arrowID = null;
        this.world = null;
        this.arrowShooter = null;
    }
}

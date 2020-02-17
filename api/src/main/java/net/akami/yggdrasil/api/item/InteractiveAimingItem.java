package net.akami.yggdrasil.api.item;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.utils.ItemUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.entity.projectile.arrow.Arrow;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.CollideEvent;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

public abstract class InteractiveAimingItem implements InteractiveItem {

    private World world;
    private UUID arrowID;

    protected UUIDHolder holder;
    protected boolean ready = true;

    public InteractiveAimingItem(UUIDHolder holder) {
        this.holder = holder;
        Object plugin = Sponge.getPluginManager().getPlugin("yggdrasil").get();
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    protected abstract void applyEffect(Vector3d location, World world);

    @Listener
    public void onProjectileLaunched(SpawnEntityEvent event) {
        if(event.getEntities().size() == 0) {
            return;
        }

        Entity entity = event.getEntities().get(0);
        if(entity.getType() != EntityTypes.TIPPED_ARROW) {
            return;
        }
        Projectile projectile = (Projectile) entity;
        ProjectileSource shooter = projectile.getShooter();
        if(!(shooter instanceof Player)) {
            return;
        }
        Player entityShooter = (Player) shooter;
        HandType hand = ItemUtils.getMatchingHand(entityShooter, matchingItem());
        if(hand != null && entityShooter.getUniqueId().equals(holder.getUUID()) && arrowID == null) {
            launched(projectile);
        }
    }

    private void launched(Projectile projectile) {
        this.world = projectile.getWorld();
        this.arrowID = projectile.getUniqueId();
        ready = false;
    }

    @Listener
    public void onProjectileLanded(CollideEvent.Impact event, @First Arrow arrow) {
        if(arrow == null || world == null || !arrow.getUniqueId().equals(arrowID)) {
            return;
        }
        Vector3d loc = event.getImpactPoint().getPosition();
        event.setCancelled(true);
        arrow.remove();

        apply(loc, arrow.getWorld());
    }

    @Listener
    public void onProjectileKilled(DestructEntityEvent event) {
        if(arrowID != null && event.getTargetEntity().getUniqueId().equals(arrowID)) {
            reset();
        }
    }

    @Override
    public void onRightClicked(InteractEvent event, GameItemClock clock) {

        if(arrowID == null || world == null) {
            return;
        }
        Optional<Entity> optEntity = world.getEntity(arrowID);
        if(!optEntity.isPresent()) {
            return;
        }
        Entity arrow = optEntity.get();
        Vector3d position = arrow.getLocation().getPosition();
        arrow.remove();

        apply(position, arrow.getWorld());
    }

    private void apply(Vector3d position, World world) {
        reset();
        applyEffect(position, world);
    }

    private void reset() {
        this.arrowID = null;
        this.world = null;
        ready = true;
    }

    @Override
    public void onLeftClicked(InteractEvent event, GameItemClock clock) { }
}

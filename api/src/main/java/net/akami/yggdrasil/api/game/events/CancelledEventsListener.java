package net.akami.yggdrasil.api.game.events;

import net.akami.yggdrasil.api.data.YggdrasilKeys;
import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayerManager;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;

public class CancelledEventsListener {

    private AbstractYggdrasilPlayerManager manager;

    public CancelledEventsListener(AbstractYggdrasilPlayerManager manager) {
        this.manager = manager;
    }

    @Listener(order = Order.LAST)
    public void onBlockChange(ChangeBlockEvent event) {
        Cause cause = event.getCause();
        cause.getContext().get(EventContextKeys.PLAYER).ifPresent((player) -> {
            if(player.gameMode().get() != GameModes.CREATIVE) {
                event.setCancelled(true);
            }
        });
    }

    @Listener(order = Order.LAST)
    public void onPlayerDamage(DamageEntityEvent event) {
        if(event.getTargetEntity().getType() == EntityTypes.PLAYER) {
            event.setBaseDamage(0);
        }
    }

    @Listener(order = Order.LAST)
    public void onPlayerDeath(DestructEntityEvent.Death event) {
        if(event.getTargetEntity().getType() != EntityTypes.PLAYER) {
            return;
        }
        Player target = (Player) event.getTargetEntity();
        manager.removeExistingPlayer(target.getUniqueId());
        target.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
    }

    @Listener(order = Order.LAST)
    public void onExpSpawn(SpawnEntityEvent event) {
        event
                .filterEntities(entity -> entity.getType() != EntityTypes.EXPERIENCE_ORB)
                .forEach(Entity::remove);
    }

    @Listener(order = Order.LAST)
    public void onDropItem(DropItemEvent event) {
        event.setCancelled(true);
    }

    @Listener(order = Order.LAST)
    public void onItemSpawned(SpawnEntityEvent event) {
        event.getEntities().stream()
                .filter(entity -> entity.getType() == EntityTypes.ITEM && !entity.get(YggdrasilKeys.PERSISTENT).orElse(false))
                .forEach(Entity::remove);
    }
    // TODO : add hit, craft, use anvils, enchantment tables
}

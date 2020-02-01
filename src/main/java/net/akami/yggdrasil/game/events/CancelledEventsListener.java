package net.akami.yggdrasil.game.events;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;

public class CancelledEventsListener {

    @Listener(order = Order.LAST)
    public void onBlockChange(ChangeBlockEvent event) {
        Cause cause = event.getCause();
        cause.first(Player.class).ifPresent((player) -> {
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
        event.
                filterEntities(entity -> entity.getType() != EntityTypes.ITEM)
                .forEach(Entity::remove);
    }
    // TODO : add hit, craft, use anvils, enchantment tables
}

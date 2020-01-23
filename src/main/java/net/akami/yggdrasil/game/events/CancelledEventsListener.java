package net.akami.yggdrasil.game.events;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;

public class CancelledEventsListener {

    @Listener
    public void onBreak(ChangeBlockEvent event) {
        Cause cause = event.getCause();
        cause.first(Player.class).ifPresent((player) -> event.setCancelled(true));
    }
}

package net.akami.yggdrasil.core;

import com.google.inject.Inject;
import net.akami.yggdrasil.game.events.CancelledEventsListener;
import net.akami.yggdrasil.input.InventoryInteractionsListener;
import net.akami.yggdrasil.input.PlayerConnectionListener;
import net.akami.yggdrasil.player.YggdrasilPlayerManager;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id="yggdrasil", name="Yggdrasil", version="alpha", description="Core plugin for Yggdrasil")
public class YggdrasilMain {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Plugin successfully initialized");
        YggdrasilPlayerManager playerManager = new YggdrasilPlayerManager();
        EventManager eventManager = Sponge.getEventManager();
        eventManager.registerListeners(this, new PlayerConnectionListener(playerManager));
        eventManager.registerListeners(this, new InventoryInteractionsListener(playerManager.getPlayers()));
        // eventManager.registerListeners(this, new CancelledEventsListener());
    }
}

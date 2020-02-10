package net.akami.yggdrasil.core;

import com.google.inject.Inject;
import net.akami.yggdrasil.api.game.events.CancelledEventsListener;
import net.akami.yggdrasil.api.game.events.DamageEventListener;
import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.game.task.YggdrasilScheduler;
import net.akami.yggdrasil.api.input.ItemInteractionsListener;
import net.akami.yggdrasil.api.input.PlayerConnectionListener;
import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayer;
import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayerManager;
import net.akami.yggdrasil.player.YggdrasilPlayerManager;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.util.List;

@Plugin(id="yggdrasil", name="Yggdrasil", version="alpha", description="Core plugin for Yggdrasil")
public class YggdrasilMain {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        logger.info("Plugin successfully initialized");
        AbstractYggdrasilPlayerManager playerManager = new YggdrasilPlayerManager();
        List<AbstractYggdrasilPlayer> players = playerManager.getPlayers();

        GameItemClock clock = YggdrasilScheduler.scheduleItemClock(this);
        YggdrasilScheduler.scheduleManaRestoring(this, players);
        YggdrasilScheduler.scheduleFoodRestoring(this, players);

        register(Sponge.getEventManager(),
                new PlayerConnectionListener(playerManager),
                new ItemInteractionsListener(players, clock),
                new DamageEventListener(players),
                new CancelledEventsListener());
    }

    private void register(EventManager manager, Object... listeners) {
        for(Object listener : listeners) {
            manager.registerListeners(this, listener);
        }
    }

    @Listener
    public void onServerStop(GameStoppedServerEvent event) {
        logger.info("Plugin successfully stopped");
    }
}

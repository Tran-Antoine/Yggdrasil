package net.akami.yggdrasil.game.events;

import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayerManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.UUID;

public class PlayerConnectionListener {

    private AbstractYggdrasilPlayerManager manager;

    public PlayerConnectionListener(AbstractYggdrasilPlayerManager manager) {
        this.manager = manager;
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        UUID targetID = event.getTargetEntity().getUniqueId();

        if(!manager.exists(targetID)) {
            manager.createNewPlayer(targetID);
        }
    }
}

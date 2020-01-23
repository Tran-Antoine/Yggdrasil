package net.akami.yggdrasil.input;

import net.akami.yggdrasil.player.YggdrasilPlayerManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;

import java.util.UUID;

public class PlayerConnectionListener {

    private YggdrasilPlayerManager manager;

    public PlayerConnectionListener(YggdrasilPlayerManager manager) {
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

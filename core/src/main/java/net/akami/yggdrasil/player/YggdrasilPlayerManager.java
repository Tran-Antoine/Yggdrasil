package net.akami.yggdrasil.player;

import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayerManager;

import java.util.UUID;

public class YggdrasilPlayerManager extends AbstractYggdrasilPlayerManager {

    public YggdrasilPlayerManager() {
        super();
    }

    public void createNewPlayer(UUID id) {
        YggdrasilPlayer player = new YggdrasilPlayer(id);
        this.players.add(player);
    }
}

package net.akami.yggdrasil.player;

import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayer;
import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayerManager;

import java.util.UUID;

public class YggdrasilPlayerManager extends AbstractYggdrasilPlayerManager {

    public YggdrasilPlayerManager() {
        super();
    }

    public void createNewPlayer(UUID id) {
        YggdrasilPlayer player = new YggdrasilPlayer(id);
        if(players.contains(player)) {
            removeExistingPlayer(id);
        }
        this.players.add(player);
    }

    @Override
    public void removeExistingPlayer(UUID id) {
        AbstractYggdrasilPlayer player = getById(id);
        player.getMana().remove();
        players.remove(player);
    }
}

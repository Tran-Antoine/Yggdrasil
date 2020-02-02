package net.akami.yggdrasil.api.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractYggdrasilPlayerManager {

    protected List<AbstractYggdrasilPlayer> players;

    public AbstractYggdrasilPlayerManager() {
        this.players = new ArrayList<>();
    }

    private AbstractYggdrasilPlayer getById(UUID id) {
        for(AbstractYggdrasilPlayer player : players) {
            if(player.getUUID().equals(id)) {
                return player;
            }
        }
        return null;
    }

    public boolean exists(UUID id) {
        return getById(id) != null;
    }

    public abstract void createNewPlayer(UUID id);

    public List<AbstractYggdrasilPlayer> getPlayers() {
        return players;
    }
}

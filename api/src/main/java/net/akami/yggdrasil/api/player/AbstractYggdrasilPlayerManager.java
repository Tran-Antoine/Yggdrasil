package net.akami.yggdrasil.api.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractYggdrasilPlayerManager {

    protected final List<AbstractYggdrasilPlayer> players = new ArrayList<>();

    public boolean exists(UUID id) {
        return getById(id) != null;
    }

    protected AbstractYggdrasilPlayer getById(UUID id) {
        for(AbstractYggdrasilPlayer player : players) {
            if(player.getUUID().equals(id)) {
                return player;
            }
        }
        return null;
    }

    public abstract void createNewPlayer(UUID uniqueId);

    public abstract void removeExistingPlayer(UUID uniqueId);

    public List<AbstractYggdrasilPlayer> getPlayers() {
        return players;
    }

}

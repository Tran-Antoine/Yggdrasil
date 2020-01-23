package net.akami.yggdrasil.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class YggdrasilPlayerManager {

    private List<YggdrasilPlayer> players;

    public YggdrasilPlayerManager() {
        this.players = new ArrayList<>();
    }

    private YggdrasilPlayer getById(UUID id) {
        for(YggdrasilPlayer player : players) {
            if(player.getUUID().equals(id)) {
                return player;
            }
        }
        return null;
    }

    public boolean exists(UUID id) {
        return getById(id) != null;
    }

    public void createNewPlayer(UUID id) {
        this.players.add(new YggdrasilPlayer(id));
    }

    public List<YggdrasilPlayer> getPlayers() {
        return players;
    }
}

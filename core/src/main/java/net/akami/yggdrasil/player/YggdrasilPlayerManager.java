package net.akami.yggdrasil.player;

import net.akami.yggdrasil.api.display.DisplayGroup;
import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayer;
import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayerManager;
import net.akami.yggdrasil.api.spell.ExcludedSpellHandler;

import java.util.UUID;

public class YggdrasilPlayerManager extends AbstractYggdrasilPlayerManager {

    private ExcludedSpellHandler handler;
    private DisplayGroup displayGroup;

    public YggdrasilPlayerManager() {
        super();
        this.handler = new ExcludedSpellHandler();
        displayGroup = new DisplayGroup();
        displayGroup.start();
    }

    public void createNewPlayer(UUID id) {
        YggdrasilPlayer player = new YggdrasilPlayer(id, handler, displayGroup);
        handler.add(player);
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

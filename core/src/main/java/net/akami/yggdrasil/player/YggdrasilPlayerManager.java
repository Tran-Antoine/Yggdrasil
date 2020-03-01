package net.akami.yggdrasil.player;

import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayer;
import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayerManager;
import net.akami.yggdrasil.api.spell.ExcludedSpellHandler;

import java.util.UUID;

public class YggdrasilPlayerManager extends AbstractYggdrasilPlayerManager {

    private final ExcludedSpellHandler handler;

    public YggdrasilPlayerManager() {
        super();
        this.handler = new ExcludedSpellHandler();
    }

    public void createNewPlayer(UUID uniqueId) {
        YggdrasilPlayer player = new YggdrasilPlayer(uniqueId, handler);
        handler.add(player);
        if(super.players.contains(player)) {
            removeExistingPlayer(uniqueId);
        }
        super.players.add(player);
    }

    @Override
    public void removeExistingPlayer(UUID id) {
        AbstractYggdrasilPlayer player = super.getById(id);
        player.getMana().remove();
        super.players.remove(player);
    }

}

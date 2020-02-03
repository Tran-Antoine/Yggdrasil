package net.akami.yggdrasil.api.spell;

import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public interface Spell {

    List<SpellTier> getTiers();

    default SpellTier getTier(int tier) {
        return getTiers().get(tier-1);
    }
}

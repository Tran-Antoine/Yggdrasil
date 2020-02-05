package net.akami.yggdrasil.api.spell;

import org.spongepowered.api.entity.living.player.Player;

public interface SpellTier {

    void definePreLaunchProperties(Player caster, SpellCreationData data);
}

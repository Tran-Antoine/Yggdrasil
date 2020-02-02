package net.akami.yggdrasil.api.spell;

import org.spongepowered.api.entity.living.player.Player;

public interface Spell {

    void cast(Player caster);
}

package net.akami.yggdrasil.api.spell;

import org.spongepowered.api.entity.living.player.Player;

public interface SpellTier<T extends SpellLauncher<T>> {

    void definePreLaunchProperties(Player caster, SpellCreationData<T> data);
}

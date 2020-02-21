package net.akami.yggdrasil.api.spell;

import org.spongepowered.api.entity.living.player.Player;

public class EmptySpellTier<T extends SpellLauncher<T>> implements SpellTier<T> {

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<T> data) {

    }
}

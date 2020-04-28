package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class FreezingSpellBaseTier implements SpellTier<FreezingSpellLauncher> {

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<FreezingSpellLauncher> data) {
        data.setProperty("time", (int) 5);
        data.setProperty("radius", (int) 7);
    }
}

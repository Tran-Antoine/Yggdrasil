package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class LevitationBaseTier implements SpellTier<LevitationSpellLauncher> {

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<LevitationSpellLauncher> data) {
        data.setProperty("speed", 0.3);
    }
}

package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class PhoenixArrowExplosiveTier implements SpellTier<PhoenixArrowLauncher> {

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<PhoenixArrowLauncher> data) {
        data.addProperty("explosive");
    }
}

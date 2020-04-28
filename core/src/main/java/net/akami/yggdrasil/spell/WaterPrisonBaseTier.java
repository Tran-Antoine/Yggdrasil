package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCaster.SpellType;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class WaterPrisonBaseTier implements SpellTier<WaterPrisonLauncher> {

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<WaterPrisonLauncher> data) {
        data.setProperty("radius", 3);
        data.setProperty("life_span", 5);
        data.setProperty("excluded_type", SpellType.HIGH_MOTION);
    }
}

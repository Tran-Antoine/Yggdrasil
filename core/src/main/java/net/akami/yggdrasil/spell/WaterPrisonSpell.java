package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class WaterPrisonSpell implements Spell {

    @Override
    public List<SpellTier> getTiers() {
        return Arrays.asList(
                new WaterPrisonBaseTier(),
                new WaterPrisonRadiusTier(4),
                new WaterPrisonDurationTier(8),
                new WaterPrisonRadiusTier(5),
                new WaterPrisonDurationTier(11),
                new WaterPrisonRadiusTier(7),
                new WaterPrisonCurrentTier()
        );
    }

    @Override
    public SpellLauncher getLauncher() {
        return new WaterPrisonLauncher();
    }
}

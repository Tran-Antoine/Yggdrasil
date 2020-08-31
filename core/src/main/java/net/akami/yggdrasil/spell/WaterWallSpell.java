package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class WaterWallSpell implements Spell {

    @Override
    public List<SpellTier> getTiers() {
        return Arrays.asList(
                new WaterWallBaseTier(),
                new WaterWallSizeTier(5, 3, 2),
                new WaterWallSizeTier(5, 3, 2),
                new WaterWallSizeTier(5, 5, 2),
                new WaterWallDurationTier(4),
                new WaterWallSizeTier(6, 7, 3),
                new WaterWallSizeTier(8, 8, 4)
        );
    }

    @Override
    public SpellLauncher getLauncher() {
        return new WaterWallLauncher();
    }
}

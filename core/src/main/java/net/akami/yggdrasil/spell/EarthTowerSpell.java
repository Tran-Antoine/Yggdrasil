package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.EmptySpellTier;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellRadiusTier;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class EarthTowerSpell implements Spell<EarthTowerLauncher> {

    @Override
    public List<SpellTier<EarthTowerLauncher>> getTiers() {
        return Arrays.asList(
                new EarthTowerBaseTier(),
                new SpellRadiusTier<>(2),
                new EarthTowerHeightTier(8),
                new EarthTowerUnderneathTier(),
                new EmptySpellTier<>()
        );
    }

    @Override
    public EarthTowerLauncher getLauncher() {
        return new EarthTowerLauncher();
    }
}

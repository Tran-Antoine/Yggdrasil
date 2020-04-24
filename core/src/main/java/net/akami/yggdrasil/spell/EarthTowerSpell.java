package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class EarthTowerSpell implements Spell<EarthTowerLauncher> {

    @Override
    public List<SpellTier<EarthTowerLauncher>> getTiers() {
        return Arrays.asList(

        );
    }

    @Override
    public EarthTowerLauncher getLauncher() {
        return new EarthTowerLauncher();
    }
}

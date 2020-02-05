package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class EarthTowerSpell implements Spell {

    private List<SpellTier> tiers;

    public EarthTowerSpell() {
        this.tiers = Arrays.asList(
                new EarthTowerTestTier(),
                new EarthTowerTestTier(),
                new EarthTowerTestTier());
    }

    @Override
    public List<SpellTier> getTiers() {
        return tiers;
    }
}

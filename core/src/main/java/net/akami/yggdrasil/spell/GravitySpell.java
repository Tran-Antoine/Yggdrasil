package net.akami.yggdrasil.spell;


import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class GravitySpell implements Spell {

    private List<SpellTier> tiers;

    public GravitySpell() {
        this.tiers = Arrays.asList(
                new GravityTestTier(),
                new GravityTestTier(),
                new GravityTestTier());
    }

    @Override
    public List<SpellTier> getTiers() {
        return tiers;
    }

    @Override
    public SpellLauncher getLauncher() {
        return null;
    }
}

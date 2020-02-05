package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class WindOfFireSpell implements Spell {

    private List<SpellTier> tiers;

    public WindOfFireSpell() {
        this.tiers = Arrays.asList(
                new WindOfFireTestTier(),
                new WindOfFireTestTier(),
                new WindOfFireTestTier());
    }

    @Override
    public List<SpellTier> getTiers() {
        return tiers;
    }
}

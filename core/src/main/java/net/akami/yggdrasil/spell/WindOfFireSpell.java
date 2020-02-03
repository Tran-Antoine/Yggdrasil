package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Collections;
import java.util.List;

public class WindOfFireSpell implements Spell {

    private List<SpellTier> tiers;

    public WindOfFireSpell() {
        this.tiers = Collections.singletonList(new WindOfFireTestTier());
    }

    @Override
    public List<SpellTier> getTiers() {
        return tiers;
    }
}

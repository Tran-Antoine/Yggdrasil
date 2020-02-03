package net.akami.yggdrasil.spell;


import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Collections;
import java.util.List;

public class GravitySpell implements Spell {

    private List<SpellTier> tiers;

    public GravitySpell() {
        this.tiers = Collections.singletonList(new GravityTestTier());
    }

    @Override
    public List<SpellTier> getTiers() {
        return tiers;
    }
}

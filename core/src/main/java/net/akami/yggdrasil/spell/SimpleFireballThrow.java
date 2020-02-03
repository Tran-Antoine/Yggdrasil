package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Collections;
import java.util.List;

public class SimpleFireballThrow implements Spell {

    private List<SpellTier> tiers;

    public SimpleFireballThrow() {
        this.tiers = Collections.singletonList(new SimpleFireballFirstTier());
    }

    @Override
    public List<SpellTier> getTiers() {
        return tiers;
    }
}

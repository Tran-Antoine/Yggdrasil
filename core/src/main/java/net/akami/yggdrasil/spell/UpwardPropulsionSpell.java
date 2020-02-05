package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class UpwardPropulsionSpell implements Spell {

    private List<SpellTier> tiers;
    private InteractiveItemHandler handler;

    public UpwardPropulsionSpell(InteractiveItemHandler handler) {
        this.handler = handler;
        this.tiers = Arrays.asList(

        );
    }

    @Override
    public List<SpellTier> getTiers() {
        return tiers;
    }
}

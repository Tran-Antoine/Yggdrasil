package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.List;

public class CounterVelocitySpell implements Spell {

    @Override
    public List<SpellTier> getTiers() {
        return null;
    }

    @Override
    public SpellLauncher getLauncher() {
        return new CounterVelocityLauncher();
    }
}

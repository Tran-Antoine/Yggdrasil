package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.EmptySpellTier;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class DefenseDepriverSpell implements Spell {

    @Override
    public List<SpellTier> getTiers() {
        return Arrays.asList(
                new EmptySpellTier<>(),
                new EmptySpellTier<>(),
                new EmptySpellTier<>()
        );
    }

    @Override
    public SpellLauncher getLauncher() {
        return new DefenseDepriverLauncherTest();
    }
}

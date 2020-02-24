package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class LevitationSpell implements Spell<LevitationSpellLauncher> {

    // TODO : Give item to manually cancel, + non instant cost function
    @Override
    public List<SpellTier<LevitationSpellLauncher>> getTiers() {
        return Arrays.asList(
                new LevitationBaseTier(),
                new LevitationBaseTier(),
                new LevitationBaseTier()
        );
    }

    @Override
    public LevitationSpellLauncher getLauncher() {
        return new LevitationSpellLauncher();
    }
}

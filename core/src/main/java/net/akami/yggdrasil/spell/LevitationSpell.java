package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class LevitationSpell implements Spell<LevitationSpellLauncher> {

    private InteractiveItemHandler handler;

    public LevitationSpell(InteractiveItemHandler handler) {
        this.handler = handler;
    }

    @Override
    public List<SpellTier<LevitationSpellLauncher>> getTiers() {
        return Arrays.asList(
                new LevitationBaseTier(0.2, handler),
                new LevitationBaseTier(0.3),
                new LevitationBaseTier(0.4),
                new LevitationBaseTier(0.5),
                new LevitationBaseTier(0.6),
                new LevitationBaseTier(0.7),
                new LevitationBaseTier(0.8)
        );
    }

    @Override
    public LevitationSpellLauncher getLauncher() {
        return new LevitationSpellLauncher();
    }
}

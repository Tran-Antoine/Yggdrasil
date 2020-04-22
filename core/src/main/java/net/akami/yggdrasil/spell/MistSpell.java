package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellRadiusTier;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class MistSpell implements Spell<MistSpellLauncher> {

    @Override
    public List<SpellTier<MistSpellLauncher>> getTiers() {
        return Arrays.asList(
                new SpellRadiusTier<>(2),
                new SpellRadiusTier<>(3),
                new SpellRadiusTier<>(4),
                new SpellRadiusTier<>(5),
                new SpellRadiusTier<>(6),
                new SpellRadiusTier<>(7),
                new SpellRadiusTier<>(8)
        );
    }

    @Override
    public MistSpellLauncher getLauncher() {
        return new MistSpellLauncher();
    }
}

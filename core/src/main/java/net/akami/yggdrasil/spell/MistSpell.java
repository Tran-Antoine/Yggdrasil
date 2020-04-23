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
                new SpellRadiusTier<>(5),
                new SpellRadiusTier<>(6),
                new SpellRadiusTier<>(7),
                new SpellRadiusTier<>(8),
                new SpellRadiusTier<>(9),
                new SpellRadiusTier<>(10),
                new SpellRadiusTier<>(11)
        );
    }

    @Override
    public MistSpellLauncher getLauncher() {
        return new MistSpellLauncher();
    }
}

package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellRadiusTier;
import net.akami.yggdrasil.api.spell.SpellTier;
import net.akami.yggdrasil.api.spell.SpellTimeTier;

import java.util.Arrays;
import java.util.List;

public class FreezingSpell implements Spell<FreezingSpellLauncher> {

    // TODO : Store list of placed blocks instead of relooping
    @Override
    public List<SpellTier<FreezingSpellLauncher>> getTiers() {
        return Arrays.asList(
                new FreezingSpellBaseTier(),
                new SpellTimeTier<>(8),
                new SpellRadiusTier<>(10),
                new FreezingAdditionalIceTier(),
                new SpellTimeTier<>(12),
                new SpellRadiusTier<>(15),
                new FreezingPermanentIceTier()
        );
    }

    @Override
    public FreezingSpellLauncher getLauncher() {
        return new FreezingSpellLauncher();
    }
}

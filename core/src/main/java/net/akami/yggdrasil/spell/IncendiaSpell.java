package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class IncendiaSpell implements Spell<IncendiaSpellLauncher> {

    @Override
    public List<SpellTier<IncendiaSpellLauncher>> getTiers() {
        return Arrays.asList(
                new IncendiaRadiusTier(3),
                new IncendiaRadiusTier(4),
                new IncendiaRadiusTier(5),
                new IncendiaExplosionTier(2),
                new IncendiaRadiusTier(6),
                new IncendiaExplosionTier(4),
                new IncendiaCraterTier()
        );
    }

    @Override
    public IncendiaSpellLauncher getLauncher() {
        return new IncendiaSpellLauncher();
    }
}

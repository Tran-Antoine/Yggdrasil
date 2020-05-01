package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.EmptySpellTier;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class AirBulletSpell implements Spell<AirBulletLauncher> {

    @Override
    public List<SpellTier<AirBulletLauncher>> getTiers() {
        return Arrays.asList(
                new EmptySpellTier<>(),
                new EmptySpellTier<>(),
                new EmptySpellTier<>()
        );
    }

    @Override
    public AirBulletLauncher getLauncher() {
        return new AirBulletLauncher();
    }
}

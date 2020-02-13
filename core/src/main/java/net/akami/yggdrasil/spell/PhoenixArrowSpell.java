package net.akami.yggdrasil.spell;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;

import java.util.Arrays;
import java.util.List;

public class PhoenixArrowSpell implements Spell<PhoenixArrowLauncher> {

    @Override
    public List<SpellTier<PhoenixArrowLauncher>> getTiers() {
        return Arrays.asList(
                new PhoenixArrowBaseTier(),
                new PhoenixArrowDamageTier(3),
                new PhoenixArrowCountTier(2),
                new PhoenixArrowExplosiveTier(),
                new PhoenixArrowCountTier(3),
                new PhoenixArrowDamageTier(5),
                new PhoenixArrowGuidanceTier()
        );
    }

    @Override
    public PhoenixArrowLauncher getLauncher() {
        return new PhoenixArrowLauncher();
    }
}

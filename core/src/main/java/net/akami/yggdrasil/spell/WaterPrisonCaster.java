package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.AbstractSpellCaster;
import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.utils.YggdrasilMath;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class WaterPrisonCaster extends AbstractSpellCaster<WaterPrisonLauncher> {

    @Override
    protected Supplier<Spell<WaterPrisonLauncher>> loadGenerator() {
        return WaterPrisonSpell::new;
    }

    @Override
    protected List<ElementType> loadSequence() {
        return Arrays.asList(
                ElementType.WATER,
                ElementType.WATER,
                ElementType.AIR
        );
    }

    @Override
    protected BiFunction<Float, Integer, Float> loadManaUsage() {
        return YggdrasilMath.instantStandardPolynomialFunction(40);
    }

    @Override
    protected List<Integer> loadLocationRequiredTiers() {
        return Arrays.asList(1,2,3,4,5,6,7);
    }
}

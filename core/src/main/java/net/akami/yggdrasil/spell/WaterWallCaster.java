package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.AbstractSpellCaster;
import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.utils.YggdrasilMath;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class WaterWallCaster extends AbstractSpellCaster {
    @Override
    protected Supplier<Spell> loadGenerator() {
        return WaterWallSpell::new;
    }

    @Override
    protected List<ElementType> loadSequence() {
        return Arrays.asList(
                ElementType.WATER,
                ElementType.WATER,
                ElementType.EARTH
        );
    }

    @Override
    protected BiFunction<Float, Integer, Float> loadManaUsage() {
        return YggdrasilMath.instantStandardPolynomialFunction(10);
    }

    protected List<Integer> loadLocationRequiredTiers() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7);
    }

}

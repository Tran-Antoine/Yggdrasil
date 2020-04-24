package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.AbstractSpellCaster;
import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.utils.YggdrasilMath;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class EarthTowerCaster extends AbstractSpellCaster {

    @Override
    protected Supplier<Spell> loadGenerator() {
        return EarthTowerSpell::new;
    }

    @Override
    protected List<ElementType> loadSequence() {
        return Arrays.asList(
                ElementType.EARTH,
                ElementType.WATER,
                ElementType.EARTH
        );
    }

    @Override
    protected List<Integer> loadLocationRequiredTiers() {
        return Collections.singletonList(7);
    }

    @Override
    protected BiFunction<Float, Integer, Float> loadManaUsage() {
        return YggdrasilMath.instantStandardPolynomialFunction(40);
    }
}

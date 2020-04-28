package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.AbstractSpellCaster;
import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.utils.YggdrasilMath;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class IncendiaCaster extends AbstractSpellCaster<IncendiaSpellLauncher> {

    @Override
    protected Supplier<Spell<IncendiaSpellLauncher>> loadGenerator() {
        return IncendiaSpell::new;
    }

    @Override
    protected List<ElementType> loadSequence() {
        return Arrays.asList(
                ElementType.FIRE,
                ElementType.FIRE,
                ElementType.AIR
        );
    }

    @Override
    protected BiFunction<Float, Integer, Float> loadManaUsage() {
        return YggdrasilMath.instantStandardPolynomialFunction(30);
    }

    @Override
    protected List<Integer> loadLocationRequiredTiers() {
        return Arrays.asList(1,2,3,4,5,6,7);
    }
}

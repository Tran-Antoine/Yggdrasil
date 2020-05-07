package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.AbstractSpellCaster;
import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.utils.YggdrasilMath;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class LevitationCaster extends AbstractSpellCaster {

    private InteractiveItemHandler handler;

    public LevitationCaster(InteractiveItemHandler handler) {
        this.handler = handler;
    }

    @Override
    protected Supplier<Spell> loadGenerator() {
        return () -> new LevitationSpell(handler);
    }

    @Override
    protected List<ElementType> loadSequence() {
        return Arrays.asList(
                ElementType.AIR,
                ElementType.AIR,
                ElementType.FIRE
        );
    }

    @Override
    protected BiFunction<Float, Integer, Float> loadManaUsage() {
        Function<Integer, Float> usageByTier = YggdrasilMath.standardPolynomialFunction(2);
        return (time, tier) -> usageByTier.apply(tier);
    }
}

package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.AbstractSpellCaster;
import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.utils.YggdrasilMath;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class FireballCaster extends AbstractSpellCaster<FireballSpellLauncher> {

    private final InteractiveItemHandler handler;

    public FireballCaster(InteractiveItemHandler handler) {
        this.handler = handler;
    }

    @Override
    protected Supplier<Spell<FireballSpellLauncher>> loadGenerator() {
        return () -> new FireballSpell(handler);
    }

    @Override
    protected List<ElementType> loadSequence() {
        return Arrays.asList(ElementType.FIRE, ElementType.EARTH);
    }

    @Override
    protected BiFunction<Float, Integer, Float> loadManaUsage() {
        return YggdrasilMath.instantStandardPolynomialFunction(15);
    }

    @Override
    protected SpellType loadSpellType() {
        return SpellType.OFFENSIVE;
    }
}

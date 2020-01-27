package net.akami.yggdrasil.spell;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class SpellCaster {

    private Supplier<Spell> generator;
    private Function<Float, Float> manaUsage;
    private List<Element> sequence;

    private SpellCaster() {}

    public SpellCaster(Supplier<Spell> generator, Function<Float, Float> manaUsage, List<Element> sequence) {
        this.generator = generator;
        this.manaUsage = manaUsage;
        this.sequence = sequence;
    }

    public boolean matches(List<Element> other) {
        return other.equals(sequence);
    }

    public boolean canBeCasted(float currentMana) {
        return currentMana >= manaUsage.apply(0f);
    }

    public Spell createSpell() {
        return generator.get();
    }

    public static class Builder {

        private SpellCaster caster;

        public Builder() {
            this.caster = new SpellCaster();
        }

        public Builder withGenerator(Supplier<Spell> generator) {
            caster.generator = generator;
            return this;
        }

        public Builder withManaUsage(Function<Float, Float> manaUsage) {
            caster.manaUsage = manaUsage;
            return this;
        }

        public Builder withSequence(Element... sequence) {
            return withSequence(Arrays.asList(sequence));
        }

        public Builder withSequence(List<Element> sequence) {
            caster.sequence = sequence;
            return this;
        }

        public SpellCaster build() {
            return caster;
        }
    }
}

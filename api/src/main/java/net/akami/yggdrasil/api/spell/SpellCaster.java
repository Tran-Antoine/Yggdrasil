package net.akami.yggdrasil.api.spell;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpellCaster {

    private Supplier<Spell> generator;
    private BiFunction<Float, Integer, Float> manaUsage;
    private List<ElementType> baseSequence;
    private int currentMaxTier;

    private SpellCaster() {}

    public SpellCaster(Supplier<Spell> generator, BiFunction<Float, Integer, Float> manaUsage,
                       List<ElementType> sequence) {
        this.generator = generator;
        this.manaUsage = manaUsage;
        this.baseSequence = sequence;
        this.currentMaxTier = 1;
    }

    public void enhance() {
        if (currentMaxTier < 7) {
            currentMaxTier++;
        }
    }

    /**
     * @return 0 if the sequence doesn't match any tier of the caster
     */
    public int matchingTier(List<ElementType> playerSequence) {
        List<ElementType> testSequence = Stream
                .of(baseSequence, baseSequence, baseSequence)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        switch (currentMaxTier) {
            case 7:
            case 6:
                if(playerSequence.equals(testSequence)) return currentMaxTier;
            case 5:
            case 4:
                testSequence.removeAll(baseSequence);
                if(playerSequence.equals(testSequence)) return currentMaxTier;
            default:
                testSequence.removeAll(baseSequence);
                return playerSequence.equals(baseSequence) ? currentMaxTier : 0;
        }
    }

    public boolean canCreateSpell(float currentMana, int tier) {
        return currentMana >= manaUsage.apply(0f, tier);
    }

    public float getCastingCost(int tier) {
        return manaUsage.apply(0f, tier);
    }

    public SpellTier createSpell(int tier) {
        return generator
                .get()
                .getTier(tier);
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

        public Builder withManaUsage(BiFunction<Float, Integer, Float> manaUsage) {
            caster.manaUsage = manaUsage;
            return this;
        }

        public Builder withSequence(ElementType... sequence) {
            return withSequence(Arrays.asList(sequence));
        }

        public Builder withSequence(List<ElementType> sequence) {
            caster.baseSequence = sequence;
            return this;
        }

        public SpellCaster build() {
            return caster;
        }
    }
}

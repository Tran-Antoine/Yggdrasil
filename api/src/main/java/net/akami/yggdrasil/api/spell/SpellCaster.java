package net.akami.yggdrasil.api.spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class SpellCaster {

    private Supplier<Spell> generator;
    private BiFunction<Float, Integer, Float> manaUsage;
    private List<ElementType> baseSequence;
    private int currentMaxTier = 7;

    private SpellCaster() {}

    public SpellCaster(Supplier<Spell> generator, BiFunction<Float, Integer, Float> manaUsage,
                       List<ElementType> sequence) {
        this.generator = generator;
        this.manaUsage = manaUsage;
        this.baseSequence = sequence;
    }

    public void enhance() {
        if (currentMaxTier < 7) {
            currentMaxTier++;
        }
    }

    public Optional<Integer> matchingTier(List<ElementType> playerSequence) {
        return findTier(playerSequence, 3, 5, 7);
    }

    private Optional<Integer> findTier(List<ElementType> playerSequence, int... endSequenceTiers) {
        List<ElementType> testSequence = new ArrayList<>(baseSequence);
        int previous = 0;
        for(int level : endSequenceTiers) {
            /*System.out.println("Currently looking for tier max " + level);
            System.out.println("Test sequence : "+testSequence);
            System.out.println("Player sequence : " + playerSequence);
            System.out.println("Current max tier : " + currentMaxTier);
            System.out.println("Minimal level : " + (previous + 1));*/
            if(testSequence.equals(playerSequence) && currentMaxTier > previous) {
                return Optional.of(Math.min(currentMaxTier, level));
            }
            previous = level;
            testSequence.addAll(baseSequence);
        }
        return Optional.empty();
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

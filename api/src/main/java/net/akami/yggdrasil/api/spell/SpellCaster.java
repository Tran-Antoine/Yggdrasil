package net.akami.yggdrasil.api.spell;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class SpellCaster {

    protected Supplier<Spell> generator;
    protected BiFunction<Float, Integer, Float> manaUsage;
    protected List<ElementType> baseSequence;
    protected List<Integer> locationRequiredTiers = Collections.emptyList();
    protected SpellType spellType;
    private int currentMaxTier = 7;

    protected SpellCaster() { }

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

    public boolean requiresLocation(int tier) {
        return locationRequiredTiers.contains(tier);
    }

    public float getCastingCost(int tier) {
        return manaUsage.apply(0f, tier);
    }

    public Spell createSpell() {
        return generator.get();
    }

    public SpellType getSpellType() {
        return spellType;
    }

    public enum SpellType {
        OFFENSIVE,
        DEFENSIVE,
        MOTION,
        UNDEFINED,
        NONE
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

        public Builder withLocationRequiredTiers(Integer... tiers) {
            caster.locationRequiredTiers = Arrays.asList(tiers);
            return this;
        }

        public Builder withSpellType(SpellType spellType) {
            caster.spellType = spellType;
            return this;
        }

        public SpellCaster build() {
            return caster;
        }
    }
}

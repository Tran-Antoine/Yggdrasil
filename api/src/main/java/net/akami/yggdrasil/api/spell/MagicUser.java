package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.mana.ManaHolder;
import net.akami.yggdrasil.api.spell.SpellCaster.SpellType;

import java.util.List;
import java.util.Optional;

public interface MagicUser extends ManaHolder, UUIDHolder {

    List<SpellCaster> getSpells();
    ExcludedSpellHandler getExclusionHandler();
    List<SpellType> currentlyExcludedTypes();
    List<ElementType> currentSequence();

    default Optional<SpellCastContext> findBySequence() {
        for(SpellCaster caster : getSpells()) {
            if(currentlyExcludedTypes().contains(caster.getSpellType())) {
                continue;
            }
            Optional<Integer> tierChosen = caster.matchingTier(currentSequence());
            if(tierChosen.isPresent()) {
                int tier = tierChosen.get();
                boolean requiresLocation = caster.requiresLocation(tier);
                return Optional.of(new SpellCastContext(caster, tier, requiresLocation));
            }
        }
        return Optional.empty();
    }

    default Optional<SpellCastContext> findBySequenceThenClear() {
        Optional<SpellCastContext> result = findBySequence();
        clearSequence();
        return result;
    }

    default void addExcludedType(SpellType type) {
        currentlyExcludedTypes().add(type);
    }

    default void removeExcludedType(SpellType type) {
        currentlyExcludedTypes().remove(type);
    }

    default void clearSequence() {
        currentSequence().clear();
    }
}
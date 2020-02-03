package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.mana.ManaHolder;

import java.util.List;
import java.util.Optional;

public interface MagicUser extends ManaHolder, UUIDHolder {

    List<SpellCaster> getSpells();
    List<ElementType> currentSequence();

    default Optional<SpellCastResult> findBySequence() {
        for(SpellCaster caster : getSpells()) {
            Optional<Integer> tierChosen = caster.matchingTier(currentSequence());
            if(tierChosen.isPresent()) {
                return Optional.of(new SpellCastResult(caster, tierChosen.get()));
            }
        }
        return Optional.empty();
    }

    default void clearSequence() {
        currentSequence().clear();
    }
}
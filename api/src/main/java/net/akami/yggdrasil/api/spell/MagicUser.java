package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.mana.ManaHolder;

import java.util.List;
import java.util.Optional;

public interface MagicUser extends ManaHolder, UUIDHolder {

    List<SpellCaster> getSpells();
    List<ElementType> currentSequence();

    default Optional<SpellCastContext> findBySequence() {
        for(SpellCaster caster : getSpells()) {
            Optional<Integer> tierChosen = caster.matchingTier(currentSequence());
            if(tierChosen.isPresent()) {
                int tier = tierChosen.get();
                boolean requiresLocation = caster.requiresLocation(tier);
                return Optional.of(new SpellCastContext(caster, tier, requiresLocation));
            }
        }
        return Optional.empty();
    }

    default void clearSequence() {
        currentSequence().clear();
    }
}
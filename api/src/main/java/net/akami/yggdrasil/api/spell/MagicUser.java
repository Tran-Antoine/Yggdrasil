package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.mana.ManaHolder;

import java.util.List;
import java.util.Optional;

public interface MagicUser extends ManaHolder, UUIDHolder {

    List<SpellCaster> getSpells();
    List<ElementType> currentSequence();

    default Optional<SpellCaster> findBySequence() {
        return getSpells()
                .stream()
                .filter((caster) -> caster.matchingTier(currentSequence()))
                .findAny();
    }

    default void clearSequence() {
        currentSequence().clear();
    }
}
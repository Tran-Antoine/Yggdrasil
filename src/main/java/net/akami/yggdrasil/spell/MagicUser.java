package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.input.UUIDHolder;
import net.akami.yggdrasil.mana.ManaHolder;

import java.util.List;
import java.util.Optional;

public interface MagicUser extends ManaHolder, UUIDHolder {

    List<SpellCaster> getSpells();
    List<ElementType> currentSequence();

    default Optional<SpellCaster> findBySequence() {
        return getSpells()
                .stream()
                .filter((caster) -> caster.matches(currentSequence()))
                .findAny();
    }

    default void clearSequence() {
        currentSequence().clear();
    }
}
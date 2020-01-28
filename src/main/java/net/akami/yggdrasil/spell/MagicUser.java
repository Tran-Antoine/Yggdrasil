package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.mana.ManaHolder;

import java.util.List;
import java.util.Optional;

public interface MagicUser extends ManaHolder {

    List<SpellCaster> getSpells();
    List<Element> currentSequence();

    default Optional<SpellCaster> findBySequence() {
        return getSpells()
                .stream()
                .filter((caster) -> caster.matches(currentSequence()))
                .findAny();
    }
}
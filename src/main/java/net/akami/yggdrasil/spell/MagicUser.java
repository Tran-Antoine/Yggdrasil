package net.akami.yggdrasil.spell;

import java.util.List;

public interface MagicUser {

    List<SpellCaster> getSpells();
    List<Element> currentSequence();

    default SpellCaster findBySequence() {
        return getSpells()
                .stream()
                .filter((caster) -> caster.matches(currentSequence()))
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }
}
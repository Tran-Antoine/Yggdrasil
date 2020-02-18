package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.spell.SpellCaster.SpellType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class ExcludedSpellHandler {

    private List<MagicUser> users;

    public ExcludedSpellHandler() {
        this.users = new ArrayList<>();
    }

    public void add(MagicUser user) {
        users.add(user);
    }

    public void remove(MagicUser user) {
        users.remove(user);
    }

    public void addExcludingType(SpellType excluded, Predicate<MagicUser> condition) {
        actionExcludingType(excluded, condition, MagicUser::addExcludedType);
    }
    public void removeExcludingType(SpellType excluded, Predicate<MagicUser> condition) {
        actionExcludingType(excluded, condition, MagicUser::removeExcludedType);
    }

    private void actionExcludingType(SpellType excluded, Predicate<MagicUser> condition, BiConsumer<MagicUser, SpellType> action) {
        for(MagicUser user : users) {
            if(condition.test(user)) {
                action.accept(user, excluded);
            }
        }
    }
}

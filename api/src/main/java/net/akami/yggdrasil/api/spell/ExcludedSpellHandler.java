package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.spell.SpellCaster.SpellType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class ExcludedSpellHandler {

    private Set<MagicUser> users;

    public ExcludedSpellHandler() {
        this.users = new HashSet<>();
    }

    public void add(MagicUser user) {
        users.add(user);
    }

    public void remove(MagicUser user) {
        users.remove(user);
    }

    public Set<MagicUser> addExcludingType(SpellType excluded, Predicate<MagicUser> condition) {
        return actionExcludingType(excluded, condition, MagicUser::addExcludedType);
    }

    public void removeExcludingType(SpellType excluded, List<MagicUser> users) {
        removeExcludingType(excluded, users::contains);
    }

    public void removeExcludingType(SpellType excluded, Predicate<MagicUser> condition) {
        actionExcludingType(excluded, condition, MagicUser::removeExcludedType);
    }

    private Set<MagicUser> actionExcludingType(SpellType excluded, Predicate<MagicUser> condition,
                                               BiConsumer<MagicUser, SpellType> action) {
        Set<MagicUser> concernedUsers = new HashSet<>();
        for(MagicUser user : users) {
            if(condition.test(user)) {
                action.accept(user, excluded);
                concernedUsers.add(user);
            }
        }
        return concernedUsers;
    }
}

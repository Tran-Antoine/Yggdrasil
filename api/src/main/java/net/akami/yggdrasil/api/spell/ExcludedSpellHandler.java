package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.spell.SpellCaster.SpellType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

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

    public <T extends SpellLauncher<T>> Set<MagicUser> addExcludingType(SpellType excluded, T launcher, BiPredicate<T, MagicUser> condition) {
        return actionExcludingType(excluded, launcher, condition, MagicUser::addExcludedType);
    }

    public void removeExcludingType(SpellType excluded, List<MagicUser> users) {
        removeExcludingType(excluded, null, (l, user) -> users.contains(user));
    }

    public <T extends SpellLauncher<T>> void removeExcludingType(SpellType excluded, T launcher, BiPredicate<T, MagicUser> condition) {
        actionExcludingType(excluded, launcher, condition, MagicUser::removeExcludedType);
    }

    private <T extends SpellLauncher<T>> Set<MagicUser> actionExcludingType(SpellType excluded, T launcher, BiPredicate<T, MagicUser> condition,
                                                                            BiConsumer<MagicUser, SpellType> action) {
        Set<MagicUser> concernedUsers = new HashSet<>();
        for(MagicUser user : users) {
            if(condition.test(launcher, user)) {
                action.accept(user, excluded);
                concernedUsers.add(user);
            }
        }
        return concernedUsers;
    }
}

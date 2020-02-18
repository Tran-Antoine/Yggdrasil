package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.spell.SpellCaster.SpellType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

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

    public <T extends SpellLauncher<T>> void addExcludingType(SpellType excluded, T launcher, BiPredicate<T, MagicUser> condition) {
        actionExcludingType(excluded, launcher, condition, MagicUser::addExcludedType);
    }
    public <T extends SpellLauncher<T>> void removeExcludingType(SpellType excluded, T launcher, BiPredicate<T, MagicUser> condition) {
        actionExcludingType(excluded, launcher, condition, MagicUser::removeExcludedType);
    }

    private <T extends SpellLauncher<T>> void actionExcludingType(SpellType excluded, T launcher, BiPredicate<T, MagicUser> condition, BiConsumer<MagicUser, SpellType> action) {
        for(MagicUser user : users) {
            if(condition.test(launcher, user)) {
                action.accept(user, excluded);
            }
        }
    }
}

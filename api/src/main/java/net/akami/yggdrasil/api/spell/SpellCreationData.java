package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.SpellCaster.SpellType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class SpellCreationData<T extends SpellLauncher<T>> {

    private final List<BiConsumer<Player, T>> preActions;
    private final List<BiConsumer<Player, T>> postActions;
    private final PropertyMap propertyMap;

    private boolean isStorable;
    private ItemStack item;
    private InteractiveItemHandler handler;
    private final Set<MagicUser> deprivedUsers;

    public SpellCreationData() {
        this.preActions = new ArrayList<>();
        this.postActions = new ArrayList<>();
        this.propertyMap = new PropertyMap();
        this.deprivedUsers = new HashSet<>();
        this.isStorable = false;
    }


    public static class PropertyMap {

        private final Map<String, Object> properties;

        private PropertyMap() {
            this.properties = new HashMap<>();
        }

        public Object getProperty(String name) {
            return properties.get(name);
        }

        public <R> R getProperty(String name, Class<R> type) {
            Object result = properties.get(name);
            if(result != null && type.isAssignableFrom(result.getClass())) {
                return (R) result;
            }
            return null;
        }

        public <R> R getPropertyOrElse(String name, R orElse) {
            R result = (R) getProperty(name, orElse.getClass());
            return result != null ? result : orElse;
        }
    }

    public PropertyMap getPropertyMap() {
        return propertyMap;
    }

    public boolean hasProperty(String name) {
        return propertyMap.properties.containsKey(name);
    }

    public void addProperty(String name) {
        setProperty(name, null);
    }

    public void setProperty(String name, Object value) {
        propertyMap.properties.put(name, value);
    }

    public void addPreAction(BiConsumer<Player, T> action) {
        preActions.add(action);
    }

    public void addPostAction(BiConsumer<Player, T> action) {
        postActions.add(action);
    }

    public boolean isStorable() {
        return isStorable;
    }

    public void setStorable(boolean storable) {
        isStorable = storable;
    }

    public void performPreActions(Player caster, T launcher) {
        performActions(caster, launcher, preActions);
    }

    public void performPostActions(Player caster, T launcher) {
        performActions(caster, launcher, postActions);
    }

    private void performActions(Player caster, T launcher, Iterable<BiConsumer<Player, T>> sequence) {
        for(BiConsumer<Player, T> consumer : sequence) {
            consumer.accept(caster, launcher);
        }
    }

    public void excludeTargetSpells() {
        PropertyMap map = getPropertyMap();
        Predicate<MagicUser> condition = (Predicate<MagicUser>) map.getProperty("exclusion_condition");
        excludeTargetSpells(condition);
    }

    public void excludeTargetSpells(Predicate<MagicUser> condition) {

        PropertyMap map = getPropertyMap();
        MagicUser user = map.getProperty("caster", MagicUser.class);
        ExcludedSpellHandler spellHandler = user.getExclusionHandler();
        if(condition == null) return;

        System.out.println(map);
        System.out.println(map.getProperty("excluded_type"));
        SpellType excluded = map.getPropertyOrElse("excluded_type", SpellType.NONE);
        this.deprivedUsers.addAll(spellHandler.addExcludingType(excluded, condition));
    }

    public void restoreSpellAccess() {
        restoreSpellAccess((user) -> true);
    }

    public void restoreSpellAccess(Predicate<MagicUser> condition) {
        Predicate<MagicUser> combination = condition.and(deprivedUsers::contains);
        PropertyMap map = getPropertyMap();
        SpellType type = map.getPropertyOrElse("excluded_type", SpellType.NONE);
        map.getProperty("caster", MagicUser.class).getExclusionHandler().removeExcludingType(type, combination);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public InteractiveItemHandler getHandler() {
        return handler;
    }

    public void setHandler(InteractiveItemHandler handler) {
        this.handler = handler;
    }
}

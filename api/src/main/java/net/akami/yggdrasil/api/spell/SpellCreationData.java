package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class SpellCreationData<T extends SpellLauncher> {

    private List<BiConsumer<Player, T>> preActions;
    private List<BiConsumer<Player, T>> postActions;
    private PropertyMap propertyMap;

    private boolean isStorable;
    private ItemStack item;
    private InteractiveItemHandler handler;

    public SpellCreationData() {
        this.preActions = new ArrayList<>();
        this.postActions = new ArrayList<>();
        this.propertyMap = new PropertyMap();
        this.isStorable = false;
    }

    public static class PropertyMap {

        private Map<String, Object> properties;

        private PropertyMap() {
            this.properties = new HashMap<>();
        }

        public <R> R getProperty(String name, Class<R> type) {
            Object result = properties.get(name);
            if(result != null && type.isAssignableFrom(result.getClass())) {
                return (R) result;
            }
            return null;
        }

        public <R> R getPropertyOrElse(String name, Class<R> type, R orElse) {
            R result = getProperty(name, type);
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

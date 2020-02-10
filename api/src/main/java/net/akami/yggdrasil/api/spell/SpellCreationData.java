package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SpellCreationData {

    private List<Consumer<Player>> actions;
    private Map<String, Object> properties;

    private boolean isStorable;
    private ItemStack item;
    private InteractiveItemHandler handler;

    public SpellCreationData() {
        this.actions = new ArrayList<>();
        this.properties = new HashMap<>();
        this.isStorable = false;
    }

    public <T> T getProperty(String name, Class<T> type) {
        Object result = properties.get(name);
        if(result != null && type.isAssignableFrom(result.getClass())) {
            return (T) result;
        }
        return null;
    }

    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    public void addAction(Consumer<Player> action) {
        actions.add(action);
    }

    public boolean isStorable() {
        return isStorable;
    }

    public void setStorable(boolean storable) {
        isStorable = storable;
    }

    public void performActions(Player caster) {
        for(Consumer<Player> consumer : actions) {
            consumer.accept(caster);
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

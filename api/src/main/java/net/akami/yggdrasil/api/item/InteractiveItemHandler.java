package net.akami.yggdrasil.api.item;

import net.akami.yggdrasil.api.input.CancellableEvent;
import net.akami.yggdrasil.api.task.AbstractGameItemClock;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;

import java.util.List;
import java.util.function.Consumer;

public interface InteractiveItemHandler {

    List<InteractiveItem> getItems();

    default void leftClick(ItemStack item, CancellableEvent<?> event, AbstractGameItemClock clock) {
        click(item, (interactiveItem) -> interactiveItem.onLeftClicked(event, clock));
    }

    default void rightClick(ItemStack item, CancellableEvent<?> event, AbstractGameItemClock clock) {
        click(item, (interactiveItem) -> interactiveItem.onRightClicked(event, clock));
    }

    default void addItem(InteractiveItem item) {
        getItems().add(item);
    }

    default void click(ItemStack item, Consumer<InteractiveItem> call) {
        InteractiveItem result = null;
        for(InteractiveItem interactiveItem : getItems()) {
            if(ItemStackComparators.IGNORE_SIZE.compare(interactiveItem.matchingItem(), item) == 0) {
                result = interactiveItem;
                break;
            }
        }
        // To avoid modifying the list while iterating it, since the call.accept() might add new
        // items to the list
        if(result != null) {
            call.accept(result);
            if(result.isDestroyed()) {
                getItems().remove(result);
            }
        }
    }

    default boolean hasItem(InteractiveItem item) {
        return getItems().contains(item);
    }
}

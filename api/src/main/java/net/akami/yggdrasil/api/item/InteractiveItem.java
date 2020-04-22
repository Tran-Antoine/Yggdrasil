package net.akami.yggdrasil.api.item;

import net.akami.yggdrasil.api.input.CancellableEvent;
import net.akami.yggdrasil.api.task.AbstractGameItemClock;
import org.spongepowered.api.item.inventory.ItemStack;

public interface InteractiveItem {

    ItemStack matchingItem();

    void onLeftClicked(CancellableEvent<?> event, AbstractGameItemClock clock);

    void onRightClicked(CancellableEvent<?> event, AbstractGameItemClock clock);

    default boolean isDestroyed() {
        return false;
    }

    default void onReady() {
    }
}

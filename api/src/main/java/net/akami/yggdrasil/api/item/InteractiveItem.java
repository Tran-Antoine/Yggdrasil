package net.akami.yggdrasil.api.item;

import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.input.CancellableEvent;
import org.spongepowered.api.item.inventory.ItemStack;

public interface InteractiveItem {

    ItemStack matchingItem();

    void onLeftClicked(CancellableEvent<?> event, GameItemClock clock);

    void onRightClicked(CancellableEvent<?> event, GameItemClock clock);

    default boolean isDestroyed() {
        return false;
    }

    default void onReady() {
    }
}

package net.akami.yggdrasil.api.item;

import net.akami.yggdrasil.api.game.task.GameItemClock;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.item.inventory.ItemStack;

public interface InteractiveItem {

    ItemStack matchingItem();

    void onLeftClicked(ItemStack item, InteractEvent event, GameItemClock clock);
    void onRightClicked(ItemStack item, InteractEvent event, GameItemClock clock);

    default boolean isDestroyed() { return false; }
    default void onReady(){}
}

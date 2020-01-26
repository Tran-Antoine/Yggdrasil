package net.akami.yggdrasil.item;

import net.akami.yggdrasil.game.task.GameItemClock;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;

public interface InteractiveItem {

    ItemStack matchingItem();

    void onLeftClicked(InteractItemEvent event, GameItemClock clock);
    void onRightClicked(InteractItemEvent event, GameItemClock clock);
    default void onReady(){}
}

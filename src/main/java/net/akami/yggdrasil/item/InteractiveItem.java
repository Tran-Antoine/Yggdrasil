package net.akami.yggdrasil.item;

import net.akami.yggdrasil.game.task.GameItemClock;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.item.inventory.ItemStack;

public interface InteractiveItem {

    ItemStack matchingItem();

    void onLeftClicked(InteractEvent event, GameItemClock clock);
    void onRightClicked(InteractEvent event, GameItemClock clock);
    default void onReady(){}
}

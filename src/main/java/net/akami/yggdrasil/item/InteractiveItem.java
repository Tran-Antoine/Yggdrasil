package net.akami.yggdrasil.item;

import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;

public interface InteractiveItem {

    ItemStack matchingItem();

    void onLeftClicked(InteractItemEvent event);
    void onRightClicked(InteractItemEvent event);
}

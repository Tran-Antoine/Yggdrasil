package net.akami.yggdrasil.player;

import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;

public interface InteractiveItemHandler {

    void leftClick(ItemStack item, InteractItemEvent.Primary event);
    void rightClick(ItemStack item, InteractItemEvent.Secondary event);
}

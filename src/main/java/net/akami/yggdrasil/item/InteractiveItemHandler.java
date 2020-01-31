package net.akami.yggdrasil.item;

import net.akami.yggdrasil.game.task.GameItemClock;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.item.inventory.ItemStack;

public interface InteractiveItemHandler {

    void leftClick(ItemStack item, InteractEvent event, GameItemClock clock);
    void rightClick(ItemStack item, InteractEvent event, GameItemClock clock);
}

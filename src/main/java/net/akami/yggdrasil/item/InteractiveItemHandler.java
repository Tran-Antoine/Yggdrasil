package net.akami.yggdrasil.item;

import net.akami.yggdrasil.game.task.GameItemClock;
import org.spongepowered.api.event.item.inventory.InteractItemEvent.Primary;
import org.spongepowered.api.event.item.inventory.InteractItemEvent.Secondary;
import org.spongepowered.api.item.inventory.ItemStack;

public interface InteractiveItemHandler {

    void leftClick(ItemStack item, Primary event, GameItemClock clock);
    void rightClick(ItemStack item, Secondary event, GameItemClock clock);
}

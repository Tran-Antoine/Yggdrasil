package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.input.CancellableEvent;
import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.task.AbstractGameItemClock;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.function.Consumer;

public class LevitationCancelItem implements InteractiveItem {

    private ItemStack item;
    private Consumer<Player> resetAction;

    public LevitationCancelItem(Consumer<Player> resetAction) {
        this.resetAction = resetAction;
        this.item = ItemStack.of(ItemTypes.REDSTONE);
    }
    @Override
    public ItemStack matchingItem() {
        return item;
    }

    @Override
    public void onLeftClicked(CancellableEvent<?> event, AbstractGameItemClock clock) {
        resetAction.accept(event.getCause().first(Player.class).get());
    }

    @Override
    public void onRightClicked(CancellableEvent<?> event, AbstractGameItemClock clock) { }

}

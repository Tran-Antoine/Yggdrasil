package net.akami.yggdrasil.api.item;

import net.akami.yggdrasil.api.input.CancellableEvent;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.task.AbstractGameItemClock;
import net.akami.yggdrasil.api.utils.ItemUtils;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.function.Supplier;

public class LaunchableSpellItem implements InteractiveItem {

    private ItemStack modelItem;
    private SpellCreationData finalData;
    private Supplier<SpellLauncher> generator;

    public LaunchableSpellItem(ItemStack item, SpellCreationData finalData, Supplier<SpellLauncher> generator) {
        this.modelItem = item;
        this.finalData = finalData;
        this.generator = generator;
    }

    @Override
    public ItemStack matchingItem() {
        return modelItem;
    }

    @Override
    public void onLeftClicked(CancellableEvent<?> event, AbstractGameItemClock clock) {
    }

    @Override
    public void onRightClicked(CancellableEvent<?> event, AbstractGameItemClock clock) {
        Player player = event.getCause().first(Player.class).get();
        HandType chosen = ItemUtils.getMatchingHand(player, this.modelItem);

        int nextQuantity = this.modelItem.getQuantity() - 1;
        this.modelItem.setQuantity(nextQuantity);

        player.setItemInHand(chosen, this.modelItem);
        generator.get().launch(finalData, player);
    }

    @Override
    public boolean isDestroyed() {
        return modelItem.getQuantity() == 0;
    }
}
package net.akami.yggdrasil.api.item;

import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;

import java.util.Optional;

public class LaunchableSpellItem implements InteractiveItem {

    private ItemStack item;
    private SpellCreationData finalData;
    private SpellLauncher launcher;

    public LaunchableSpellItem(ItemStack item, SpellCreationData finalData, SpellLauncher launcher) {
        this.item = item;
        this.finalData = finalData;
        this.launcher = launcher;
    }

    @Override
    public ItemStack matchingItem() {
        return item;
    }

    @Override
    public void onLeftClicked(ItemStack item, InteractEvent event, GameItemClock clock) { }

    @Override
    public void onRightClicked(ItemStack item, InteractEvent event, GameItemClock clock) {
        Player player = event.getCause().first(Player.class).get();
        int nextQuantity = this.item.getQuantity() - 1;
        this.item.setQuantity(nextQuantity);

        Optional<ItemStack> optItem = player.getItemInHand(HandTypes.MAIN_HAND);
        HandType chosen = optItem.isPresent() && ItemStackComparators.IGNORE_SIZE.compare(optItem.get(), item) == 0
                ? HandTypes.MAIN_HAND
                : HandTypes.OFF_HAND;
        player.setItemInHand(chosen, this.item);
        launcher.launch(finalData, player);
    }

    @Override
    public boolean isDestroyed() {
        return item.getQuantity() == 0;
    }
}

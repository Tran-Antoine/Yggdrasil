package net.akami.yggdrasil.api.item;

import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.input.CancellableEvent;
import net.akami.yggdrasil.api.spell.MagicUser;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.utils.ItemUtils;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

public class LaunchableSpellItem implements InteractiveItem {

    private ItemStack modelItem;
    private SpellCreationData finalData;
    private SpellLauncher launcher;
    private MagicUser user;

    public LaunchableSpellItem(ItemStack item, SpellCreationData finalData, SpellLauncher launcher, MagicUser user) {
        this.modelItem = item;
        this.finalData = finalData;
        this.launcher = launcher;
        this.user = user;
    }

    @Override
    public ItemStack matchingItem() {
        return modelItem;
    }

    @Override
    public void onLeftClicked(CancellableEvent<?> event, GameItemClock clock) {
    }

    @Override
    public void onRightClicked(CancellableEvent<?> event, GameItemClock clock) {
        Player player = event.getCause().first(Player.class).get();
        HandType chosen = ItemUtils.getMatchingHand(player, this.modelItem);

        int nextQuantity = this.modelItem.getQuantity() - 1;
        this.modelItem.setQuantity(nextQuantity);

        player.setItemInHand(chosen, this.modelItem);
        launcher.launch(finalData, player);
    }

    @Override
    public boolean isDestroyed() {
        return modelItem.getQuantity() == 0;
    }
}
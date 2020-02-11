package net.akami.yggdrasil.api.utils;

import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.item.InteractiveItemUser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.item.inventory.Slot;

import java.util.Optional;

public class ItemUtils {

    public static void fitItem(InteractiveItemUser user, InteractiveItem item) {
        Player player = Sponge.getServer().getPlayer(user.getUUID()).orElseThrow(IllegalStateException::new);
        fitItem(player, user, item);
    }

    public static void fitItem(Player player, InteractiveItemHandler handler, InteractiveItem item) {
        if(item == null) {
            throw new IllegalStateException("Cannot add null item to inventory");
        }
        ItemStack itemToProvide = item.matchingItem();
        for(Slot slot : player.getInventory().<Slot>slots()) {
            if(slot.canFit(itemToProvide)) {
                handler.addItem(item);
                slot.set(itemToProvide);
                return;
            }
        }
    }

    public static HandType getMatchingHand(Player target, ItemStack item) {
        Optional<ItemStack> optItem = target.getItemInHand(HandTypes.MAIN_HAND);
        if(optItem.isPresent() && ItemStackComparators.IGNORE_SIZE.compare(optItem.get(), item) == 0) {
            return HandTypes.MAIN_HAND;
        }
        optItem = target.getItemInHand(HandTypes.OFF_HAND);
        return optItem.isPresent() && ItemStackComparators.IGNORE_SIZE.compare(optItem.get(), item) == 0
                ? HandTypes.OFF_HAND
                : null;
    }
}

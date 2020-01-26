package net.akami.yggdrasil.item.list;

import net.akami.yggdrasil.game.events.GameItemClock;
import net.akami.yggdrasil.item.InteractiveItem;
import net.akami.yggdrasil.player.LifeComponent;
import net.akami.yggdrasil.player.LivingUser;
import net.akami.yggdrasil.player.UUIDHolder;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.UUID;

public class InstantHealItem implements InteractiveItem {

    private LivingUser user;
    private ItemStack item;

    public InstantHealItem(LivingUser user) {
        this.user = user;
        this.item = ItemStack
                .builder()
                .itemType(ItemTypes.EXPERIENCE_BOTTLE)
                .quantity(1)
                .build();
    }

    @Override
    public ItemStack matchingItem() {
        return item;
    }

    @Override
    public void onLeftClicked(InteractItemEvent event, GameItemClock clock) {
        LifeComponent component = user.getLife();
        component.heal(15);
    }

    @Override
    public void onRightClicked(InteractItemEvent event, GameItemClock clock) {
        event.setCancelled(true);
    }
}

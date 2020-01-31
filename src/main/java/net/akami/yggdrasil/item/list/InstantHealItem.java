package net.akami.yggdrasil.item.list;

import net.akami.yggdrasil.game.task.GameItemClock;
import net.akami.yggdrasil.item.InteractiveItem;
import net.akami.yggdrasil.life.LifeComponent;
import net.akami.yggdrasil.life.LivingUser;
import net.akami.yggdrasil.mana.ManaContainer;
import net.akami.yggdrasil.mana.ManaHolder;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

public class InstantHealItem implements InteractiveItem {

    private LivingUser livingUser;
    private ManaHolder manaHolder;
    private ItemStack item;

    public <T extends LivingUser & ManaHolder> InstantHealItem(T user) {
        this.livingUser = user;
        this.manaHolder = user;
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
    public void onLeftClicked(InteractEvent event, GameItemClock clock) {
        LifeComponent component = livingUser.getLife();
        ManaContainer mana = manaHolder.getMana();
        int healValue = 5;
        mana.ifEnoughMana(2 * healValue, () -> component.heal(healValue));
    }

    @Override
    public void onRightClicked(InteractEvent event, GameItemClock clock) {
        event.setCancelled(true);
    }
}

package net.akami.yggdrasil.item;

import net.akami.yggdrasil.api.input.CancellableEvent;
import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.item.InteractiveItemUser;
import net.akami.yggdrasil.api.task.AbstractGameItemClock;
import net.akami.yggdrasil.api.utils.ItemUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collections;

public class ControlModeSwitchItem implements InteractiveItem {

    private InteractiveItemUser handler;
    private boolean expertMode = false;

    private static final ItemStack DISABLED = ItemStack.builder()
            .itemType(ItemTypes.FIREWORK_CHARGE)
            .add(Keys.DISPLAY_NAME, Text.builder()
                    .color(TextColors.RED)
                    .append(Text.of("Expert mode disabled")).build())
            .add(Keys.ITEM_LORE, Collections.singletonList(Text.builder()
                    .color(TextColors.GRAY)
                    .append(Text.of("Click to switch to expert mode")).build()))
            .add(Keys.HIDE_ATTRIBUTES, true)
            .build();

    private static final ItemStack ENABLED = ItemStack.builder()
            .itemType(ItemTypes.SLIME_BALL)
            .add(Keys.DISPLAY_NAME, Text.builder()
                    .color(TextColors.GREEN)
                    .append(Text.of("Expert mode enabled")).build())
            .add(Keys.ITEM_LORE, Collections.singletonList(Text.builder()
                    .color(TextColors.GRAY)
                    .append(Text.of("Click to switch to classic mode")).build()))
            .add(Keys.HIDE_ATTRIBUTES, true)
            .add(Keys.HIDE_MISCELLANEOUS, true)
            .build();

    public ControlModeSwitchItem(InteractiveItemUser handler) {
        this.handler = handler;
    }

    @Override
    public ItemStack matchingItem() {
        return expertMode
                ? ENABLED
                : DISABLED;
    }

    @Override
    public void onLeftClicked(CancellableEvent<?> event, AbstractGameItemClock clock) {
        ItemStack current = matchingItem();
        expertMode = !expertMode;
        ItemStack replacement = matchingItem();

        if(expertMode) handler.enableExpertMode();
        else handler.disableExpertMode();

        Player target = Sponge.getServer().getPlayer(handler.getUUID()).get();
        HandType hand = ItemUtils.getMatchingHand(target, current);
        target.setItemInHand(hand, replacement);
    }

    @Override
    public void onRightClicked(CancellableEvent<?> event, AbstractGameItemClock clock) { }
}

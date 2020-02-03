package net.akami.yggdrasil.item;

import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.spell.*;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class SpellTriggerItem implements InteractiveItem {

    private ItemStack item;
    private MagicUser user;

    public SpellTriggerItem(MagicUser user) {
        this.user = user;
        this.item = ItemStack
                .builder()
                .itemType(ItemTypes.BARRIER)
                .quantity(1)
                .build();
    }

    @Override
    public ItemStack matchingItem() {
        return item;
    }

    @Override
    public void onLeftClicked(ItemStack item, InteractEvent event, GameItemClock clock) { }

    @Override
    public void onRightClicked(ItemStack item, InteractEvent event, GameItemClock clock) {
        Optional<SpellCastResult> optResult = user.findBySequence();
        user.clearSequence();
        if(!optResult.isPresent()) {
            System.out.println("Unable to launch spell from current sequence");
            return;
        }

        SpellCastResult result = optResult.get();
        SpellCaster caster = result.getCaster();
        int tier = result.getChosenTier();

        user.getMana().ifEnoughMana(caster.getCastingCost(tier), () -> {
            SpellTier spell = caster.createSpell(tier);
            spell.cast(event.getCause().first(Player.class).get());
        });
    }

}

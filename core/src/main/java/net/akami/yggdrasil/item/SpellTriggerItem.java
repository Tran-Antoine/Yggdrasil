package net.akami.yggdrasil.item;

import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.spell.MagicUser;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellCaster;
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
    public void onLeftClicked(InteractEvent event, GameItemClock clock) {

    }

    @Override
    public void onRightClicked(InteractEvent event, GameItemClock clock) {
        Optional<SpellCaster> optCaster = user.findBySequence();
        user.clearSequence();
        if(!optCaster.isPresent()) {
            System.out.println("Unable to launch spell from current sequence");
            return;
        }
        SpellCaster caster = optCaster.get();
        user.getMana().ifEnoughMana(caster.getCastingCost(), () -> {
            Spell spell = caster.createSpell();
            spell.cast(event.getCause().first(Player.class).get());
        });
    }
}

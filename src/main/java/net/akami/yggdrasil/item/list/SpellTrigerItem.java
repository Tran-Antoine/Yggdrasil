package net.akami.yggdrasil.item.list;

import net.akami.yggdrasil.game.task.GameItemClock;
import net.akami.yggdrasil.item.InteractiveItem;
import net.akami.yggdrasil.mana.ManaHolder;
import net.akami.yggdrasil.spell.MagicUser;
import net.akami.yggdrasil.spell.Spell;
import net.akami.yggdrasil.spell.SpellCaster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class SpellTrigerItem implements InteractiveItem {

    private ItemStack item;
    private MagicUser user;

    public SpellTrigerItem(MagicUser user) {
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
    public void onLeftClicked(InteractItemEvent event, GameItemClock clock) {

    }

    @Override
    public void onRightClicked(InteractItemEvent event, GameItemClock clock) {
        Optional<SpellCaster> optCaster = user.findBySequence();
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

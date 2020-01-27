package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.item.InteractiveItem;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;

public abstract class StorableSpell implements Spell, InteractiveItem {

    @Override
    public void cast(Player caster) {
        ItemStack item = matchingItem();
        for(Slot slot : caster.getInventory().<Slot>slots()) {
            if(slot.canFit(item)) {
                slot.set(item);
                return;
            }
        }
        System.out.println("Failed to add spell to inventory");
    }
}

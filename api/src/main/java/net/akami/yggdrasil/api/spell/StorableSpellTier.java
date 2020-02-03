package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.item.SpellTierItem;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;

import java.util.Objects;
import java.util.function.Supplier;

public class StorableSpellTier implements SpellTier {

    private InteractiveItem interactiveItem;
    private InteractiveItemHandler handler;

    public StorableSpellTier(ItemStack itemToProvide, Supplier<SpellTier> previous, InteractiveItemHandler handler,
                             int quantity) {
        this(ItemStack.builder()
                .from(itemToProvide)
                .quantity(quantity)
                .build(), previous, handler);
    }

    public StorableSpellTier(ItemStack itemToProvide, Supplier<SpellTier> previous, InteractiveItemHandler handler) {
        this.handler = Objects.requireNonNull(handler, "InteractiveItemHandler cannot be null if one ore more tiers are storable");
        this.interactiveItem = new SpellTierItem(itemToProvide, previous);
    }


    @Override
    public void cast(Player caster) {

        ItemStack itemToProvide = interactiveItem.matchingItem();

        for(Slot slot : caster.getInventory().<Slot>slots()) {
            if(slot.canFit(itemToProvide)) {
                handler.addItem(interactiveItem);
                slot.set(itemToProvide);
                return;
            }
        }
        System.out.println("Failed to add spell to inventory");
    }
}

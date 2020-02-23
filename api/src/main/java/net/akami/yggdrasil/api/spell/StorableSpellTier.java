package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

public class StorableSpellTier<T extends SpellLauncher<T>> implements SpellTier<T> {

    private ItemStack itemToProvide;
    private InteractiveItemHandler handler;
    private int quantity;

    public StorableSpellTier(int quantity) {
        this(null, null, quantity);
    }

    public StorableSpellTier(ItemStack itemToProvide, InteractiveItemHandler handler, int quantity) {
        this.itemToProvide = itemToProvide;
        this.handler = handler;
        this.quantity = quantity;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<T> data) {
        data.setStorable(true);
        if(itemToProvide != null && handler != null) {
            data.setItem(itemToProvide);
            data.setHandler(handler);
        }
        data.getItem().setQuantity(quantity);
    }
}

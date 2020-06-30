package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.*;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class GlassShieldSpell implements Spell<GlassShieldLauncher> {

    private InteractiveItemHandler handler;

    public GlassShieldSpell(InteractiveItemHandler handler) {
        this.handler = handler;
    }

    @Override
    public List<SpellTier<GlassShieldLauncher>> getTiers() {
        return Arrays.asList(
                new StorableSpellTier<>(ItemStack.of(ItemTypes.BARRIER), handler, 1),
                new SpellRadiusTier<>(2),
                new SpellTimeTier<>(10),
                new StorableSpellTier<>(2),
                new SpellTimeTier<>(15),
                new SpellRadiusTier<>(4),
                new SpellRadiusTier<>(5)
        );
    }

    @Override
    public GlassShieldLauncher getLauncher() {
        return new GlassShieldLauncher();
    }
}

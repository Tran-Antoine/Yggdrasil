package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;
import net.akami.yggdrasil.api.spell.StorableSpellTier;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CounterVelocitySpell implements Spell<CounterVelocityLauncher> {

    private final InteractiveItemHandler handler;
    private final ItemStack item;

    public CounterVelocitySpell(InteractiveItemHandler handler) {
        this.handler = handler;
        this.item = ItemStack.of(ItemTypes.FEATHER);
    }

    @Override
    public List<SpellTier<CounterVelocityLauncher>> getTiers() {
        return Arrays.asList(
                new CounterVelocityMaxTier(0.7),
                new CounterVelocityMaxTier(1.3),
                new CounterVelocityMaxTier(1.5),
                new CounterVelocityPushBackTier(),
                new CounterVelocityMaxTier(1.8),
                new StorableSpellTier<>(item, handler, 2),
                new StorableSpellTier<>(3)
        );
    }

    @Override
    public CounterVelocityLauncher getLauncher() {
        return new CounterVelocityLauncher();
    }
}

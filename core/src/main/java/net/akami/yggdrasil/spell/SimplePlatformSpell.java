package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.*;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class SimplePlatformSpell implements Spell<SimplePlatformLauncher> {

    private InteractiveItemHandler handler;

    public SimplePlatformSpell(InteractiveItemHandler handler) {
        this.handler = handler;
    }

    @Override
    public List<SpellTier<SimplePlatformLauncher>> getTiers() {
        return Arrays.asList(
                new SimplePlatformBaseTier(),
                new SpellRadiusTier<>(3),
                new SpellDurationTier<>(8),
                new EmptySpellTier<>(),
                new SpellRadiusTier<>(4),
                new StorableSpellTier<>(ItemStack.of(ItemTypes.BRICK), handler,1),
                new StorableSpellTier<>(2)
        );
    }

    @Override
    public SimplePlatformLauncher getLauncher() {
        return new SimplePlatformLauncher();
    }
}

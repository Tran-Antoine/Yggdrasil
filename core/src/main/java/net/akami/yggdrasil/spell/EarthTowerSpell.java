package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.*;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class EarthTowerSpell implements Spell<EarthTowerLauncher> {


    private InteractiveItemHandler handler;

    public EarthTowerSpell(InteractiveItemHandler handler) {
        this.handler = handler;
    }

    @Override
    public List<SpellTier<EarthTowerLauncher>> getTiers() {
        return Arrays.asList(
                new EarthTowerBaseTier(),
                new SpellRadiusTier<>(2),
                new EarthTowerHeightTier(8),
                new EarthTowerUnderneathTier(),
                new EarthTowerHeightTier(11),
                new StorableSpellTier(ItemStack.of(ItemTypes.DIRT), handler, 1),
                new EmptySpellTier<>()
        );
    }

    @Override
    public EarthTowerLauncher getLauncher() {
        return new EarthTowerLauncher();
    }
}

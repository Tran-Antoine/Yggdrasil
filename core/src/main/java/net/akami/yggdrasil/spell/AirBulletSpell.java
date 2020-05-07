package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellRadiusTier;
import net.akami.yggdrasil.api.spell.SpellTier;
import net.akami.yggdrasil.api.spell.StorableSpellTier;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class AirBulletSpell implements Spell<AirBulletLauncher> {

    private InteractiveItemHandler handler;

    public AirBulletSpell(InteractiveItemHandler handler) {
        this.handler = handler;
    }

    @Override
    public List<SpellTier<AirBulletLauncher>> getTiers() {
        return Arrays.asList(
                new SpellRadiusTier<>(0),
                new SpellRadiusTier<>(1),
                new AirBulletVelocityTier(1.5),
                new StorableSpellTier(ItemStack.of(ItemTypes.GHAST_TEAR), handler, 2),
                new StorableSpellTier(3),
                new AirBulletCountTier(2),
                new AirBulletCountTier(3)
        );
    }

    @Override
    public AirBulletLauncher getLauncher() {
        return new AirBulletLauncher();
    }
}

package net.akami.yggdrasil.spell.test;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class UpwardPropulsionSpell implements Spell {

    private List<SpellTier> tiers;
    private ItemStack item;
    private InteractiveItemHandler handler;

    @Override
    public SpellLauncher getLauncher() {
        return null;
    }

    public UpwardPropulsionSpell(InteractiveItemHandler handler) {
        this.item = ItemStack.of(ItemTypes.SLIME_BALL);
        this.handler = handler;
        this.tiers = Arrays.asList(
                new UpwardPropulsionStandardTier(2.5),
                new UpwardPropulsionStandardTier(4.0),
                new UpwardPropulsionFallDamageTier(4.0)
        );
    }

    @Override
    public List<SpellTier> getTiers() {
        return tiers;
    }
}

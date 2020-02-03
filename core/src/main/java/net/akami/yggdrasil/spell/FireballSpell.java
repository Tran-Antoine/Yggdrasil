package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.SpellTier;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.StorableSpellTier;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class FireballSpell implements Spell {

    private List<SpellTier> tiers;
    private ItemStack fireBall;
    private InteractiveItemHandler handler;

    public FireballSpell(InteractiveItemHandler handler) {
        this.handler = handler;
        this.fireBall = ItemStack.of(ItemTypes.FIRE_CHARGE);
        this.tiers = loadTiers();
    }

    private List<SpellTier> loadTiers() {
        return Arrays.asList(
                new FireballDamageTier(1, 3),
                new FireballDamageTier(2, 3),
                new FireballDamageTier(2, 4),
                storableSpellTier(2, 4),
                storableSpellTier(3, 3.5),
                storableSpellTier(2, 5, 2),
                storableSpellTier(2, 5, 3));
    }

    private SpellTier storableSpellTier(int radius, double damage) {
        return new StorableSpellTier(fireBall, () -> new FireballDamageTier(radius, damage), handler);
    }

    private SpellTier storableSpellTier(int radius, double damage, int quantity) {
        return new StorableSpellTier(fireBall, () -> new FireballDamageTier(radius, damage), handler, quantity);
    }

    @Override
    public List<SpellTier> getTiers() {
        return tiers;
    }
}

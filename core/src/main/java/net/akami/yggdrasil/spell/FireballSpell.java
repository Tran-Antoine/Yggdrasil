package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;
import net.akami.yggdrasil.api.spell.StorableSpellTier;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class FireballSpell implements Spell<FireballSpellLauncher> {

    private final List<SpellTier<FireballSpellLauncher>> tiers;
    private final ItemStack fireBall;
    private final InteractiveItemHandler handler;

    public FireballSpell(InteractiveItemHandler handler) {
        this.handler = handler;
        this.fireBall = ItemStack.of(ItemTypes.FIRE_CHARGE);
        this.tiers = loadTiers();
    }

    private List<SpellTier<FireballSpellLauncher>> loadTiers() {
        return  Arrays.asList(
                new FireballDamageTier(1, 3),
                new FireballDamageTier(2, 3),
                new FireballDamageTier(2, 4),
                new StorableSpellTier<>(fireBall, handler, 1),
                new FireballDamageTier(2, 4.5),
                new StorableSpellTier<>(2),
                new StorableSpellTier<>(3));
    }

    @Override
    public List<SpellTier<FireballSpellLauncher>> getTiers() {
        return tiers;
    }

    @Override
    public FireballSpellLauncher getLauncher() {
        return new FireballSpellLauncher();
    }
}

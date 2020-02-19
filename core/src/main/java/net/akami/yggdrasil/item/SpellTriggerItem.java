package net.akami.yggdrasil.item;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.input.CancellableEvent;
import net.akami.yggdrasil.api.item.InteractiveAimingItem;
import net.akami.yggdrasil.api.spell.MagicUser;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellCastContext;
import net.akami.yggdrasil.api.spell.SpellCaster;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SpellTriggerItem extends InteractiveAimingItem {

    private ItemStack item;
    private MagicUser user;

    public SpellTriggerItem(MagicUser user) {
        super(user);
        this.user = user;
        List<Enchantment> enchantments = Collections.singletonList(
                Enchantment.of(EnchantmentTypes.INFINITY, 1));
        Text name = Text.builder()
                .color(TextColors.GOLD)
                .append(Text.of("Launch Spell"))
                .build();
        this.item = ItemStack
                .builder()
                .itemType(ItemTypes.BOW)
                .add(Keys.DISPLAY_NAME, name)
                .add(Keys.UNBREAKABLE, true)
                .add(Keys.ITEM_ENCHANTMENTS, enchantments)
                .add(Keys.HIDE_ENCHANTMENTS, true)
                .add(Keys.HIDE_ATTRIBUTES, true)
                .add(Keys.HIDE_UNBREAKABLE, true)
                .quantity(1)
                .build();
    }

    @Override
    protected void applyEffect(Vector3d location, World world) {
        Optional<SpellCastContext> optResult = user.findBySequence();
        user.clearSequence();
        optResult.ifPresent(result -> applyEffect(location, result));
    }

    @Override
    public void onRightClicked(CancellableEvent<?> event, GameItemClock clock) {
        if(!aimlessSpell(event)) {
            super.onRightClicked(event, clock);
        }
    }

    private boolean aimlessSpell(Cancellable event) {

        if(!ready) {
            return false;
        }
        Optional<SpellCastContext> optResult = user.findBySequence();

        if(optResult.isPresent()) {
            SpellCastContext result = optResult.get();
            if(!result.requiresLocation()) {
                user.clearSequence();
                applyEffect(null, result);
                event.setCancelled(true);
                ready = true;
                return true;
            }
        }
        return false;
    }

    private void applyEffect(Vector3d location, SpellCastContext context) {
        SpellCaster caster = context.getCaster();
        int tier = context.getChosenTier();
        castSpell(caster, location, tier);
    }

    private void castSpell(SpellCaster caster, Vector3d location, int tier) {
        user.getMana().ifEnoughMana(caster.getCastingCost(tier), () -> {
            Spell spell = caster.createSpell();
            Player wizard = Sponge.getServer().getPlayer(user.getUUID()).get();
            spell.cast(wizard, location, tier);
        });
    }

    @Override
    public ItemStack matchingItem() {
        return item;
    }
}

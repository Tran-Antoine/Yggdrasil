package net.akami.yggdrasil.item;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.display.SimpleTextDisplayer;
import net.akami.yggdrasil.api.input.CancellableEvent;
import net.akami.yggdrasil.api.item.InteractiveAimingItem;
import net.akami.yggdrasil.api.mana.ManaDrainTask;
import net.akami.yggdrasil.api.spell.MagicUser;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellCastContext;
import net.akami.yggdrasil.api.spell.SpellCaster;
import net.akami.yggdrasil.api.task.AbstractGameItemClock;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
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
    private SimpleTextDisplayer textDisplayer;

    public SpellTriggerItem(MagicUser user, SimpleTextDisplayer textDisplayer) {
        super(user);
        this.user = user;
        this.textDisplayer = textDisplayer;
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
        Optional<SpellCastContext> optResult = user.findBySequenceThenClear();
        optResult.ifPresent(result -> applyEffect(location, result));
    }

    @Override
    public void onRightClicked(CancellableEvent<?> event, AbstractGameItemClock clock) {
        textDisplayer.clearActionBarDisplay();
        if (isAimingSpellOtherwiseLaunch(event)) {
            // the super method is for aiming spells
            super.onRightClicked(event, clock);
        }
    }

    private boolean isAimingSpellOtherwiseLaunch(CancellableEvent<?> event) {

        if (!ready) {
            // meaning an arrow is flying, thus no other spell can be cast.
            // It obviously means that this is an aiming spell
            return true;
        }

        Optional<SpellCastContext> optResult = user.findBySequence();
        if (optResult.isPresent()) {
            SpellCastContext result = optResult.get();

            if (result.requiresLocation()) {
                // Then the click is just about launching the arrow. No need for sequence clearing yet
                return true;
            }

            textDisplayer.clearActionBarDisplay();
            applyEffect(null, result);
            event.setCancelled(true);
            ready = true;
        }

        user.clearSequence();
        return false;
    }


    private void applyEffect(Vector3d location, SpellCastContext context) {
        SpellCaster caster = context.getCaster();
        int tier = context.getChosenTier();
        castSpell(caster, location, tier);
    }

    private void castSpell(SpellCaster caster, Vector3d location, int tier) {
        boolean worked = user.getMana().ifEnoughMana(caster.getCastingCost(tier), () -> {
            Spell spell = caster.createSpell();
            ManaDrainTask manaDrain = caster.scheduleConstantLoss(user, tier);
            spell.cast(user, location, tier, manaDrain);
        });
        playSound(worked);
    }

    private void playSound(boolean worked) {
        Player player = Sponge.getServer().getPlayer(user.getUUID()).get();
        SoundType sound = worked ? SoundTypes.ENTITY_EXPERIENCE_ORB_PICKUP : SoundTypes.ITEM_BOTTLE_FILL_DRAGONBREATH;
        player.playSound(sound, player.getPosition(), 1);
    }

    @Override
    public ItemStack matchingItem() {
        return item;
    }
}
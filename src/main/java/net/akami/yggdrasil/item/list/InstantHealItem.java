package net.akami.yggdrasil.item.list;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.game.task.GameItemClock;
import net.akami.yggdrasil.item.InteractiveItem;
import net.akami.yggdrasil.life.LifeComponent;
import net.akami.yggdrasil.life.LivingUser;
import net.akami.yggdrasil.mana.ManaContainer;
import net.akami.yggdrasil.mana.ManaHolder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

public class InstantHealItem implements InteractiveItem {

    private LivingUser livingUser;
    private ManaHolder manaHolder;
    private ItemStack item;

    public <T extends LivingUser & ManaHolder> InstantHealItem(T user) {
        this.livingUser = user;
        this.manaHolder = user;
        this.item = ItemStack
                .builder()
                .itemType(ItemTypes.TOTEM_OF_UNDYING)
                .quantity(1)
                .build();
    }

    @Override
    public ItemStack matchingItem() {
        return item;
    }

    @Override
    public void onLeftClicked(InteractEvent event, GameItemClock clock) {
        LifeComponent component = livingUser.getLife();
        ManaContainer mana = manaHolder.getMana();
        int healValue = 5;
        mana.ifEnoughMana(3.5f * healValue, () -> {
            component.heal(healValue);
            Player player = Sponge.getServer().getPlayer(livingUser.getUUID()).get();
            ParticleEffect effect = ParticleEffect.builder()
                    .type(ParticleTypes.HAPPY_VILLAGER)
                    .quantity(70)
                    .offset(new Vector3d(0.8, 0.8, 0.8))
                    .build();
            player.spawnParticles(effect, player.getPosition());
        });
    }

    @Override
    public void onRightClicked(InteractEvent event, GameItemClock clock) {
        event.setCancelled(true);
    }
}

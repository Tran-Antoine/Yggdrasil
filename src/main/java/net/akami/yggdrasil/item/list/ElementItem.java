package net.akami.yggdrasil.item.list;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.game.task.GameItemClock;
import net.akami.yggdrasil.item.InteractiveItem;
import net.akami.yggdrasil.spell.ElementType;
import net.akami.yggdrasil.spell.MagicUser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

public abstract class ElementItem implements InteractiveItem {

    private MagicUser user;
    private ItemStack item;

    public ElementItem(MagicUser user) {
        this.user = user;
        this.item = ItemStack
                .builder()
                .itemType(ItemTypes.DYE)
                .add(Keys.DYE_COLOR, getColor())
                .quantity(1)
                .build();
    }

    protected abstract ElementType getType();
    protected abstract DyeColor getColor();
    protected abstract ParticleType getParticleType();
    protected int getQuantity() { return 70; }

    @Override
    public ItemStack matchingItem() {
        return item;
    }

    @Override
    public void onLeftClicked(InteractEvent event, GameItemClock clock) {
        click();
    }

    @Override
    public void onRightClicked(InteractEvent event, GameItemClock clock) {
        click();
    }

    private void click() {
        user.currentSequence().add(getType());
        // Since it is called right after a click, we know that the player is there
        Player player = Sponge.getServer().getPlayer(user.getUUID()).get();
        BlockState blockState = BlockState
                .builder()
                .blockType(BlockTypes.DIRT)
                .build();
        ParticleEffect effect = ParticleEffect.builder()
                .type(getParticleType())
                .option(ParticleOptions.BLOCK_STATE, blockState)
                .quantity(getQuantity())
                .offset(new Vector3d(0.8, 0.8, 0.8))
                .build();
        player.spawnParticles(effect, player.getPosition().add(0, 1, 0));
    }
}

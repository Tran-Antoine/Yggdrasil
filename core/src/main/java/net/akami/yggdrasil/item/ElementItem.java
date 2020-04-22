package net.akami.yggdrasil.item;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.input.CancellableEvent;
import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.MagicUser;
import net.akami.yggdrasil.api.display.SimpleTextDisplayer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public abstract class ElementItem implements InteractiveItem {

    private MagicUser user;
    private ItemStack item;
    private SimpleTextDisplayer textDisplayer;

    public ElementItem(MagicUser user, SimpleTextDisplayer textDisplayer) {
        this.user = user;
        this.item = ItemStack
                .builder()
                .itemType(ItemTypes.DYE)
                .add(Keys.DISPLAY_NAME, getName())
                .add(Keys.DYE_COLOR, getColor())
                .quantity(1)
                .build();
        this.textDisplayer = textDisplayer;
    }

    protected abstract ElementType getType();
    protected abstract Text getName();
    protected abstract Text getSymbol();
    protected abstract DyeColor getColor();
    protected abstract ParticleType getParticleType();
    protected int getParticleQuantity() { return 70; }

    @Override
    public ItemStack matchingItem() {
        return item;
    }

    @Override
    public void onLeftClicked(CancellableEvent<?> event, GameItemClock clock) {
        click();
        event.setCancelled(true);
    }

    @Override
    public void onRightClicked(CancellableEvent<?> event, GameItemClock clock) {

    }

    private void click() {
        user.currentSequence().add(getType());
        textDisplayer.addActionBarDisplayElement(getSymbol());
        // Since it is called right after a click, we know that the player is there
        Player player = Sponge.getServer().getPlayer(user.getUUID()).get();
        BlockState blockState = BlockState
                .builder()
                .blockType(BlockTypes.DIRT)
                .build();
        ParticleEffect effect = ParticleEffect.builder()
                .type(getParticleType())
                .option(ParticleOptions.BLOCK_STATE, blockState)
                .quantity(getParticleQuantity())
                .offset(new Vector3d(0.8, 0.8, 0.8))
                .build();
        player.spawnParticles(effect, player.getPosition().add(0, 1, 0));
    }
}

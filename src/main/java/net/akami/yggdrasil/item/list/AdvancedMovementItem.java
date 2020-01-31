package net.akami.yggdrasil.item.list;

import com.flowpowered.math.vector.Vector3d;

import net.akami.yggdrasil.game.task.GameItemClock;
import net.akami.yggdrasil.item.InteractiveItem;
import net.akami.yggdrasil.utils.YggdrasilMath;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Color;

import java.util.Collections;
import java.util.List;

public class AdvancedMovementItem implements InteractiveItem {

    private Vector3d nextDirection;
    private ItemStack item;

    public AdvancedMovementItem() {
        List<PotionEffect> effects = Collections.singletonList(
                PotionEffect.of(PotionEffectTypes.SPEED, 1, 11 * 20)
        );
        this.item = ItemStack
                .builder()
                .itemType(ItemTypes.TIPPED_ARROW)
                .add(Keys.POTION_EFFECTS, effects)
                .add(Keys.COLOR, Color.GREEN)
                .quantity(1)
                .build();
    }

    @Override
    public ItemStack matchingItem() {
        return item;
    }

    @Override
    public void onLeftClicked(InteractEvent event, GameItemClock clock) {
        clickPerformed(event, 1, clock);
    }

    @Override
    public void onRightClicked(InteractEvent event, GameItemClock clock) {
        clickPerformed(event, 1.5, clock);
    }

    private void clickPerformed(InteractEvent event, double factor, GameItemClock clock) {
        Player target = event.getCause().first(Player.class).get();

        if (nextDirection == null) {
            double timeLeft = clock.timeLeft(this);
            if(timeLeft != 0) {
                return;
            }
            this.nextDirection = YggdrasilMath.headRotationToDirection(target.getHeadRotation());
        } else {
            performJump(target, factor);
            clock.queueItem(this, 40);
        }
    }


    private void performJump(Player target, double factor) {
        Vector3d targetVelocity = target.getVelocity().div(3);
        target.setVelocity(this.nextDirection.mul(factor).add(targetVelocity));
        this.nextDirection = null;
    }
}

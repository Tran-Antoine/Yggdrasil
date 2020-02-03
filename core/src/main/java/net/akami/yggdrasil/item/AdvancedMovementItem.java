package net.akami.yggdrasil.item;

import com.flowpowered.math.vector.Vector3d;

import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.data.key.Keys;
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
    public void onLeftClicked(ItemStack item, InteractEvent event, GameItemClock clock) {
        clickPerformed(event, 1, clock);
    }

    @Override
    public void onRightClicked(ItemStack item, InteractEvent event, GameItemClock clock) {
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
        Vector3d targetVelocity = target.getVelocity().div(1.2);
        target.setVelocity(this.nextDirection.mul(factor).add(targetVelocity));
        double yVelocity = - target.getVelocity().getY();
        if(!target.isOnGround()) {
            target.offer(Keys.FALL_DISTANCE, (float) Math.max(2.8 * Math.exp(1.298 * yVelocity) - 3, 0));
        }
        this.nextDirection = null;
    }
}

package net.akami.yggdrasil.item;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.item.InteractiveItemUser;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.potion.PotionType;
import org.spongepowered.api.item.potion.PotionTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Iterator;
import java.util.Optional;

public class AdvancedMovementItem implements InteractiveItem {

    private Vector3d nextDirection;
    private ItemStack item;
    private InteractiveItemUser user;

    public AdvancedMovementItem(InteractiveItemUser user) {
        this.user = user;
        Text name = Text.builder()
                .color(TextColors.GREEN)
                .append(Text.of("Advanced Movement"))
                .build();
        this.item = ItemStack
                .builder()
                .itemType(ItemTypes.TIPPED_ARROW)
                .add(Keys.DISPLAY_NAME, name)
                .add(Keys.POTION_TYPE, PotionTypes.LEAPING)
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
        double timeLeft = clock.timeLeft(this);
        if(timeLeft != 0) {
            return;
        }
        if (nextDirection == null) {
            this.nextDirection = YggdrasilMath.headRotationToDirection(target.getHeadRotation());
        } else {
            performJump(target, factor);
            changeItemColor(target, PotionTypes.SLOWNESS);
            clock.queueItem(this, 300);
        }
    }

    private void performJump(Player target, double factor) {
        Vector3d targetVelocity = target.getVelocity().div(1.2);
        target.setVelocity(this.nextDirection.mul(factor).add(targetVelocity).div(1, 1.4, 1));
        double yVelocity = - target.getVelocity().getY();
        if(!target.isOnGround()) {
            target.offer(Keys.FALL_DISTANCE, (float) Math.max(2.8 * Math.exp(1.298 * yVelocity) - 5, 0));
        }
        this.nextDirection = null;
    }


    @Override
    public void onReady() {
        if(!user.hasItem(this)) {
            return;
        }
        Optional<Player> optPlayer = Sponge.getServer().getPlayer(user.getUUID());
        optPlayer.ifPresent(player -> changeItemColor(player, PotionTypes.LEAPING));
    }

    private void changeItemColor(Player target, PotionType type) {
        Iterator<Slot> slots = target.getInventory().<Slot>slots().iterator();
        slots.forEachRemaining(slot -> {
            if(slot.contains(item)) {
                item.offer(Keys.POTION_TYPE, type);
                slot.set(item);
            }
        });
    }
}

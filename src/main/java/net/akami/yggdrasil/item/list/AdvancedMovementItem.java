package net.akami.yggdrasil.item.list;

import com.flowpowered.math.vector.Vector3d;

import net.akami.yggdrasil.game.task.GameItemClock;
import net.akami.yggdrasil.item.InteractiveItem;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

public class AdvancedMovementItem implements InteractiveItem {

    private Vector3d nextDirection;
    private ItemStack item;

    public AdvancedMovementItem() {
        this.item = ItemStack
                .builder()
                .itemType(ItemTypes.ARROW)
                .quantity(1)
                .build();
        //this.item.offer(Keys.DISPLAY_NAME, Text.of("Advanced movements"));
    }

    @Override
    public ItemStack matchingItem() {
        return item;
    }

    @Override
    public void onLeftClicked(InteractItemEvent event, GameItemClock clock) {
        clickPerformed(event, 1, clock);
    }

    @Override
    public void onRightClicked(InteractItemEvent event, GameItemClock clock) {
        clickPerformed(event, 1.5, clock);
    }

    private void clickPerformed(InteractItemEvent event, double factor, GameItemClock clock) {
        Player target = event.getCause().first(Player.class).get();

        if (nextDirection == null) {
            double timeLeft = clock.timeLeft(this);
            if(timeLeft != 0) {
                System.out.println("You must wait " + timeLeft + " ticks before using this item again");
                return;
            }
            setNextDirection(target.getHeadRotation());
        } else {
            performJump(target, factor);
            clock.queueItem(this, 40);
        }
    }

    private void setNextDirection(Vector3d headDir) {
        double pitch = Math.toRadians(-headDir.getX());
        double yaw = Math.toRadians(-headDir.getY());
        double x = Math.sin(yaw)*Math.cos(pitch);
        double z = Math.cos(yaw)*Math.cos(pitch);
        double y = Math.sin(pitch);
        this.nextDirection = new Vector3d(x, y/1.5, z);
    }

    private void performJump(Player target, double factor) {
        Vector3d targetVelocity = target.getVelocity().div(3);
        target.setVelocity(this.nextDirection.mul(factor).add(targetVelocity));
        this.nextDirection = null;
    }
}

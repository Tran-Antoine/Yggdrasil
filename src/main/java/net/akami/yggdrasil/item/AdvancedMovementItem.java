package net.akami.yggdrasil.item;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;

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
    public void onLeftClicked(InteractItemEvent event) {
        clickPerformed(event, 1);
    }

    @Override
    public void onRightClicked(InteractItemEvent event) {
        clickPerformed(event, 1.5);
    }

    private void clickPerformed(InteractItemEvent event, double factor) {
        Player target = event.getCause().first(Player.class).get();
        if (nextDirection == null) {
            setNextDirection(target.getHeadRotation());
        } else {
            performJump(target, factor);
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

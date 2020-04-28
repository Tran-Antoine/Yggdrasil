package net.akami.yggdrasil.item;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.item.InteractiveAimingItem;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.World;

public class LocationIndicatorItem extends InteractiveAimingItem {

    private final ItemStack item;

    public LocationIndicatorItem(UUIDHolder holder) {
        super(holder);
        this.item = ItemStack.of(ItemTypes.BOW);
        this.item.offer(Keys.UNBREAKABLE, true);
    }

    @Override
    protected void applyEffect(Vector3d location, World world) {
        YggdrasilMain.getLogger().info("Stopped location : " + location);
        YggdrasilMain.getLogger().info("Holder : " + holder.getUUID());
    }

    @Override
    public ItemStack matchingItem() {
        return item;
    }
}

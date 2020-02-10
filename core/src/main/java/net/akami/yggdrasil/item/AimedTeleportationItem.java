package net.akami.yggdrasil.item;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.item.InteractiveAimingItem;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

public class AimedTeleportationItem extends InteractiveAimingItem {

    private ItemStack item;

    public AimedTeleportationItem(UUIDHolder holder) {
        super(holder);
        this.item = ItemStack.builder()
                .itemType(ItemTypes.BOW)
                .add(Keys.DISPLAY_NAME, Text.of("Teleportation"))
                .add(Keys.UNBREAKABLE, true)
                .build();
    }

    @Override
    protected void applyEffect(Vector3d location, World world) {
        Sponge.getServer().getPlayer(holder.getUUID()).ifPresent(
                player -> player.setLocation(world.getLocation(location)));
    }

    @Override
    public ItemStack matchingItem() {
        return item;
    }
}

package net.akami.yggdrasil.item;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.item.InteractiveAimingItem;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.explosive.Explosive;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

public class AimedExplosionItem extends InteractiveAimingItem {

    private ItemStack item;

    public AimedExplosionItem(UUIDHolder holder) {
        super(holder);
        this.item = ItemStack.builder()
                .itemType(ItemTypes.BOW)
                .add(Keys.DISPLAY_NAME, Text.of("Boom"))
                .add(Keys.UNBREAKABLE, true)
                .build();
    }

    @Override
    protected void applyEffect(Vector3d location, World world) {
        Entity tnt = world.createEntity(EntityTypes.PRIMED_TNT, location);
        world.spawnEntity(tnt);
        ((Explosive) tnt).detonate();
    }

    @Override
    public ItemStack matchingItem() {
        return item;
    }
}

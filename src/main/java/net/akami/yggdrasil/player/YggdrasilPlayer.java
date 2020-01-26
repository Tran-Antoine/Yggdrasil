package net.akami.yggdrasil.player;

import net.akami.yggdrasil.item.AdvancedMovementItem;
import net.akami.yggdrasil.item.InteractiveItem;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class YggdrasilPlayer implements
        InteractiveItemUser, LivingUser {

    private UUID id;
    private LifeComponent life;
    private List<InteractiveItem> items;

    public YggdrasilPlayer(UUID id) {
        this.id = id;
        this.life = new PlayerLifeComponent(5, 50, this);
        this.items = new ArrayList<>();
        addDefaultItems();
    }

    private void addDefaultItems() {
        this.items.add(new AdvancedMovementItem());
    }

    @Override
    public void leftClick(ItemStack item, InteractItemEvent.Primary event) {
        click(item, event, InteractiveItem::onLeftClicked);
    }

    @Override
    public void rightClick(ItemStack item, InteractItemEvent.Secondary event) {
        click(item, event, InteractiveItem::onRightClicked);
    }

    private void click(ItemStack item, InteractItemEvent event, BiConsumer<InteractiveItem, InteractItemEvent> call) {
        for(InteractiveItem interactiveItem : items) {
            if(interactiveItem.matchingItem().equalTo(item)) {
                call.accept(interactiveItem, event);
            }
        }
    }

    @Override
    public UUID getUUID() {
        return id;
    }

    @Override
    public LifeComponent getLife() {
        return life;
    }
}

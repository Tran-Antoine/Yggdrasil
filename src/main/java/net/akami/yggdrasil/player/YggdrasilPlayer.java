package net.akami.yggdrasil.player;

import net.akami.yggdrasil.game.task.GameItemClock;
import net.akami.yggdrasil.item.InteractiveItemUser;
import net.akami.yggdrasil.item.list.AdvancedMovementItem;
import net.akami.yggdrasil.item.InteractiveItem;
import net.akami.yggdrasil.item.list.InstantHealItem;
import net.akami.yggdrasil.life.LifeComponent;
import net.akami.yggdrasil.life.LivingUser;
import net.akami.yggdrasil.life.PlayerLifeComponent;
import net.akami.yggdrasil.mana.ManaContainer;
import net.akami.yggdrasil.mana.ManaHolder;
import net.akami.yggdrasil.mana.PlayerManaContainer;
import org.spongepowered.api.event.item.inventory.InteractItemEvent.Primary;
import org.spongepowered.api.event.item.inventory.InteractItemEvent.Secondary;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class YggdrasilPlayer implements
        InteractiveItemUser, LivingUser, ManaHolder {

    private UUID id;
    private ManaContainer mana;
    private LifeComponent life;
    private List<InteractiveItem> items;

    public YggdrasilPlayer(UUID id) {
        this.id = id;
        this.mana = new PlayerManaContainer(100, 0.5f, this);
        this.life = new PlayerLifeComponent(5, 50, this);
        this.items = new ArrayList<>();
        addDefaultItems();
    }

    private void addDefaultItems() {
        this.items.addAll(Arrays.asList(
                new AdvancedMovementItem(),
                new InstantHealItem(this)));
    }

    @Override
    public void leftClick(ItemStack item, Primary event, GameItemClock clock) {
        click(item, (interactiveItem) -> interactiveItem.onLeftClicked(event, clock));
    }

    @Override
    public void rightClick(ItemStack item, Secondary event, GameItemClock clock) {
        click(item, (interactiveItem) -> interactiveItem.onRightClicked(event, clock));
    }

    private void click(ItemStack item, Consumer<InteractiveItem> call) {
        for(InteractiveItem interactiveItem : items) {
            if(interactiveItem.matchingItem().equalTo(item)) {
                call.accept(interactiveItem);
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

    @Override
    public ManaContainer getMana() {
        return mana;
    }
}

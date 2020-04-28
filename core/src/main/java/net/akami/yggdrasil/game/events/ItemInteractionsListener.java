package net.akami.yggdrasil.game.events;

import net.akami.yggdrasil.api.input.CancellableEvent;
import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.item.InteractiveItemUser;
import net.akami.yggdrasil.api.task.AbstractGameItemClock;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.filter.type.Include;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ItemInteractionsListener {

    private AbstractGameItemClock clock;
    private List<? extends InteractiveItemUser> users;

    public ItemInteractionsListener(List<? extends InteractiveItemUser> users, AbstractGameItemClock clock) {
        this.users = users;
        this.clock = clock;
    }

    @Listener
    public void onInteract(InteractItemEvent.Primary event) {
        ItemStack item = event.getItemStack().createStack();
        getHandler(event).ifPresent((handler) -> handler.leftClick(item, CancellableEvent.of(event), clock));
    }

    @Listener
    public void onInteract(InteractItemEvent.Secondary event) {
        ItemStack item = event.getItemStack().createStack();
        getHandler(event).ifPresent((handler) -> handler.rightClick(item, CancellableEvent.of(event), clock));
    }

    @Listener
    public void onInteract(InteractBlockEvent.Primary event) {
        if(event.getTargetBlock().getState().getType() == BlockTypes.AIR) {
            // Meaning that InteractItemEvent.Primary is already being fired
            return;
        }
        Optional<ItemStack> optItem = getItem(event);
        optItem.ifPresent((item) ->
                getHandler(event).ifPresent((handler) -> handler.leftClick(item, CancellableEvent.of(event), clock)));
    }

    @Listener
    public void onItemSelected(ChangeInventoryEvent event) {
        Optional<ItemStack> optItem = getItem(event);
        optItem.ifPresent(item -> getHandler(event).ifPresent((handler) -> handler.select(item, CancellableEvent.of(event), clock)));
    }

    @Listener(order = Order.LAST)
    @Include({ChangeBlockEvent.Place.class, ChangeBlockEvent.Break.class})
    public void onChangeBlock(ChangeBlockEvent event, @Root Player player) {

        if(player.gameMode().get() != GameModes.CREATIVE) event.setCancelled(true);

        if(!(event instanceof ChangeBlockEvent.Place)) return;

        getItem(event).ifPresent(item -> getHandler(event).ifPresent(handler -> { //If item and handler are present
            handler.rightClick(item, CancellableEvent.of(event), clock); //Trigger rightClick
        }));
    }

    private Optional<ItemStack> getItem(Event event) {
        Cause cause = event.getCause();
        Optional<Player> optPlayer = cause.first(Player.class);
        if(!optPlayer.isPresent()) {
            return Optional.empty();
        }
        Player player = optPlayer.get();
        return player.getItemInHand(HandTypes.MAIN_HAND);
    }

    private Optional<InteractiveItemHandler> getHandler(Event event) {
        Cause cause = event.getCause();
        Optional<Player> potentialTarget = cause.first(Player.class);
        if(!potentialTarget.isPresent()) {
            return Optional.empty();
        }
        UUID playerID = potentialTarget.map(Player::getUniqueId).get();
        InteractiveItemHandler handler = UUIDHolder.getByUUID(users, playerID);
        return Optional.ofNullable(handler);
    }

}

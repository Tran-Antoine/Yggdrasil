package net.akami.yggdrasil.api.input;

import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.game.task.TestVelocityTask;
import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.item.InteractiveItemUser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ItemInteractionsListener {

    private GameItemClock clock;
    private List<? extends InteractiveItemUser> users;

    public ItemInteractionsListener(List<? extends InteractiveItemUser> users, GameItemClock clock) {
        this.users = users;
        this.clock = clock;
    }

    @Listener
    public void onInteract(InteractItemEvent.Primary event) {
        ItemStack item = event.getItemStack().createStack();
        getHandler(event).ifPresent((handler) -> handler.leftClick(item, event, clock));
    }

    @Listener
    public void onInteract(InteractItemEvent.Secondary event) {
        ItemStack item = event.getItemStack().createStack();
        getHandler(event).ifPresent((handler) -> handler.rightClick(item, event, clock));
    }

    @Listener
    public void onInteract(InteractBlockEvent.Primary event) {
        if(event.getTargetBlock().getState().getType() == BlockTypes.AIR) {
            return;
        }
        Optional<ItemStack> optItem = getItem(event);
        optItem.ifPresent((item) ->
                getHandler(event).ifPresent((handler) -> handler.leftClick(item, event, clock)));
    }

    @Listener
    public void onInteract(InteractBlockEvent.Secondary event) {
        Optional<ItemStack> optItem = getItem(event);
        if(!optItem.isPresent()) {
            return;
        }
        ItemStack item = optItem.get();
        if(!item.get(Keys.ITEM_BLOCKSTATE).isPresent()) {
            return;
        }
        getHandler(event).ifPresent((handler) -> handler.rightClick(item, event, clock));
    }

    private Optional<ItemStack> getItem(InteractBlockEvent event) {
        Cause cause = event.getCause();
        Optional<Player> optPlayer = cause.first(Player.class);
        if(!optPlayer.isPresent()) {
            return Optional.empty();
        }
        Player player = optPlayer.get();
        return player.getItemInHand(HandTypes.MAIN_HAND);
    }

    private Optional<InteractiveItemHandler> getHandler(InteractEvent event) {
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

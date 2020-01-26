package net.akami.yggdrasil.input;

import net.akami.yggdrasil.game.task.GameItemClock;
import net.akami.yggdrasil.item.InteractiveItemHandler;
import net.akami.yggdrasil.item.InteractiveItemUser;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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


    private Optional<InteractiveItemHandler> getHandler(InteractItemEvent event) {
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

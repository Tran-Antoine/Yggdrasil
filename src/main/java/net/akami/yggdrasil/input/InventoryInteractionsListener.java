package net.akami.yggdrasil.input;

import net.akami.yggdrasil.player.InteractiveItemHandler;
import net.akami.yggdrasil.player.InteractiveItemUser;
import net.akami.yggdrasil.player.UUIDHolder;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InventoryInteractionsListener {

    private List<? extends InteractiveItemUser> users;

    public InventoryInteractionsListener(List<? extends InteractiveItemUser> users) {
        this.users = users;
    }

    @Listener
    public void onInteract(InteractItemEvent.Primary event) {
        ItemStack item = event.getItemStack().createStack();
        getHandler(event).ifPresent((handler) -> handler.leftClick(item, event));
    }

    @Listener
    public void onInteract(InteractItemEvent.Secondary event) {
        ItemStack item = event.getItemStack().createStack();
        getHandler(event).ifPresent((handler) -> handler.rightClick(item, event));
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

package net.akami.yggdrasil.api.game.task;

import net.akami.yggdrasil.api.input.UUIDHolder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;
import java.util.Optional;

public class FoodRestoringTask implements Runnable {

    private List<? extends UUIDHolder> users;

    public FoodRestoringTask(List<? extends UUIDHolder> users) {
        this.users = users;
    }

    @Override
    public void run() {

        this.users.forEach(user -> getSpongePlayer(user).ifPresent(this::restoreFoodToUser));

    }

    private Optional<Player> getSpongePlayer(UUIDHolder user) {
        return Sponge.getServer().getPlayer(user.getUUID());
    }

    private void restoreFoodToUser(Player player) {

        int currentLevel = player.get(Keys.FOOD_LEVEL).orElse(10);

        int diff = 20 - currentLevel;
        int bonus = Math.min(diff, 2);
        player.offer(Keys.FOOD_LEVEL, currentLevel + bonus);

    }

}

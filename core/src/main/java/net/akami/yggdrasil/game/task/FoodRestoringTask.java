package net.akami.yggdrasil.game.task;

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
        for (UUIDHolder holder : users) {
            Optional<Player> optPlayer = Sponge.getServer().getPlayer(holder.getUUID());
            optPlayer.ifPresent((player) -> {
                int currentLevel = player.get(Keys.FOOD_LEVEL).get();
                int diff = 20 - currentLevel;
                int bonus = diff <= 2 ? diff : 2;
                player.offer(Keys.FOOD_LEVEL, currentLevel + bonus);
            });
        }
    }
}

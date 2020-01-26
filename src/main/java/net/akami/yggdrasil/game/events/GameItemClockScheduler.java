package net.akami.yggdrasil.game.events;

import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

public class GameItemClockScheduler {

    public static GameItemClock schedule(Object plugin) {
        GameItemClock clock = new GameItemClock();
        Task
                .builder()
                .interval(100, TimeUnit.MILLISECONDS)
                .execute(clock::update)
                .submit(plugin);
        return clock;
    }
}

package net.akami.yggdrasil.game.task;

import net.akami.yggdrasil.mana.ManaHolder;
import org.spongepowered.api.scheduler.Task;

import java.util.List;
import java.util.concurrent.TimeUnit;

public final class YggdrasilScheduler {

    private YggdrasilScheduler(){}

    public static GameItemClock scheduleItemClock(Object plugin) {
        GameItemClock clock = new GameItemClock();
        Task
                .builder()
                .interval(100, TimeUnit.MILLISECONDS)
                .execute(clock::update)
                .submit(plugin);
        return clock;
    }

    public static ManaRestoringTask scheduleManaRestoring(Object plugin, List<? extends ManaHolder> users) {
        float deltaTime = 500f;
        ManaRestoringTask task = new ManaRestoringTask(users, deltaTime / 1000);
        Task
                .builder()
                .interval((int) deltaTime, TimeUnit.MILLISECONDS)
                .execute(task)
                .submit(plugin);
        return task;
    }
}

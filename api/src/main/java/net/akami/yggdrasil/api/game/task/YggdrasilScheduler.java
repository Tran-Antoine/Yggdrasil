package net.akami.yggdrasil.api.game.task;

import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.mana.ManaHolder;
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

    public static FoodRestoringTask scheduleFoodRestoring(Object plugin, List<? extends UUIDHolder> users) {
        FoodRestoringTask task = new FoodRestoringTask(users);
        Task
                .builder()
                .interval(30, TimeUnit.SECONDS)
                .execute(task)
                .submit(plugin);
        return task;
    }
}

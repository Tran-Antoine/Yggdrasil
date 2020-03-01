package net.akami.yggdrasil.api.game.task;

import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.mana.ManaHolder;
import org.spongepowered.api.scheduler.Task;

import java.util.List;
import java.util.concurrent.TimeUnit;

public final class YggdrasilScheduler {

    public static final float DELTA_TIME_MANA_RESTORE = 500f;

    private YggdrasilScheduler(){}

    public static GameItemClock scheduleItemClock(Object plugin) {
        GameItemClock clock = new GameItemClock();

        Task.builder()
                .interval(10, TimeUnit.MILLISECONDS)
                .execute(clock::update)
                .submit(plugin);

        return clock;
    }

    public static ManaRestoringTask scheduleManaRestoring(Object plugin, List<? extends ManaHolder> users) {
        ManaRestoringTask task = new ManaRestoringTask(users, DELTA_TIME_MANA_RESTORE / 1000);

        Task.builder()
                .interval((int) DELTA_TIME_MANA_RESTORE, TimeUnit.MILLISECONDS)
                .execute(task)
                .submit(plugin);

        return task;
    }

    public static FoodRestoringTask scheduleFoodRestoring(Object plugin, List<? extends UUIDHolder> users) {
        FoodRestoringTask task = new FoodRestoringTask(users);

        Task.builder()
                .interval(30, TimeUnit.SECONDS)
                .execute(task)
                .submit(plugin);

        return task;
    }
}

package net.akami.yggdrasil.game.task;

import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.item.TimedItemData;
import net.akami.yggdrasil.api.task.AbstractGameItemClock;
import org.spongepowered.api.Sponge;

import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Function;

public class GameItemClock implements AbstractGameItemClock {

    private double gameTime;
    private Queue<TimedItemData> queue;
    private TimedItemData currentHead;

    public GameItemClock() {
        this.queue = new PriorityQueue<>();
        this.gameTime = -1;
    }

    @Override
    public void update() {
        gameTime = Sponge.getServer().getRunningTimeTicks();
        if(!reloadHead()) {
            return;
        }
        double headEndTime = currentHead.getEndingTime();
        if (gameTime >= headEndTime) {
            currentHead.getItem().onReady();
            currentHead = null;
            queue.remove();
        }
    }

    private boolean reloadHead() {
        if(currentHead != null) {
            return true;
        }
        if(queue.isEmpty()) {
            return false;
        }
        this.currentHead = queue.element();
        return true;
    }

    @Override
    public void queueItem(InteractiveItem item, double time) {
        queue.add(new TimedItemData(item, time, gameTime));
    }

    @Override
    public boolean isInQueue(InteractiveItem item) {
        return reduceMatch(item, (data) -> true).orElse(false);
    }

    @Override
    public double timeLeft(InteractiveItem item) {
        return reduceMatch(item, (data) -> data.getEndingTime() - gameTime).orElse(0D);
    }

    private <T> Optional<T> reduceMatch(InteractiveItem item, Function<TimedItemData, T> supplier) {
        for(TimedItemData data : queue) {
            if(data.getItem().equals(item)) {
                return Optional.of(supplier.apply(data));
            }
        }
        return Optional.empty();
    }
}

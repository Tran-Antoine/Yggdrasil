package net.akami.yggdrasil.api.game.task;

import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.item.TimedItemData;
import org.spongepowered.api.Sponge;

import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Function;

public class GameItemClock {

    private double gameTime = -1;
    private Queue<TimedItemData> queue = new PriorityQueue<>();

    private TimedItemData currentHead;

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

    public void queueItem(InteractiveItem item, double time) {
        TimedItemData timedItemData = new TimedItemData(item, time, gameTime);
        queue.add(timedItemData);
    }

    public boolean isInQueue(InteractiveItem item) {
        return reduceMatch(item, (data) -> true)
                .orElse(false);
    }

    public double timeLeft(InteractiveItem item) {
        return reduceMatch(item, (data) -> data.getEndingTime() - gameTime)
                .orElse(0D);
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

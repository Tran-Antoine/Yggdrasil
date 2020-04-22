package net.akami.yggdrasil.api.task;

import net.akami.yggdrasil.api.item.InteractiveItem;

public interface AbstractGameItemClock {

    void update();
    void queueItem(InteractiveItem item, double time);
    boolean isInQueue(InteractiveItem item);
    double timeLeft(InteractiveItem item);
}

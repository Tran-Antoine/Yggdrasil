package net.akami.yggdrasil.item;

import net.akami.yggdrasil.game.events.GameItemClock;

public class TimedItemData implements Comparable<TimedItemData> {

    private InteractiveItem item;
    private double startingTime;
    private double endingTime;

    public TimedItemData(InteractiveItem item, double startingTime, double time) {
        this.item = item;
        this.startingTime = startingTime;
        this.endingTime = startingTime + time;
    }

    @Override
    public int compareTo(TimedItemData other) {
        return (int) (endingTime - other.endingTime);
    }

    public double getEndingTime() {
        return endingTime;
    }

    public InteractiveItem getItem() {
        return item;
    }
}

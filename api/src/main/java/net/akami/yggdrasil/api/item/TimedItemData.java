package net.akami.yggdrasil.api.item;


public class TimedItemData implements Comparable<TimedItemData> {

    private final InteractiveItem item;
    private final double endingTime;

    public TimedItemData(InteractiveItem item, double startingTime, double time) {
        this.item = item;
        this.endingTime = startingTime + time;
    }

    public InteractiveItem getItem() {
        return item;
    }

    public double getEndingTime() {
        return endingTime;
    }

    @Override
    public int compareTo(TimedItemData other) {
        return (int) (endingTime - other.endingTime);
    }

}

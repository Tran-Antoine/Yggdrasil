package net.akami.yggdrasil.api.item;


public class TimedItemData implements Comparable<TimedItemData> {

    private InteractiveItem item;
    private double endingTime;

    public TimedItemData(InteractiveItem item, double startingTime, double time) {
        this.item = item;
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

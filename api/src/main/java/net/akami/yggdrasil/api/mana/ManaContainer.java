package net.akami.yggdrasil.api.mana;

public class ManaContainer {

    protected final float max;
    protected final float gainPerSecond;

    protected float current;

    public ManaContainer(float max, float gainPerSecond) {
        this.max = max;
        this.current = max;
        this.gainPerSecond = gainPerSecond;
    }

    public void ifEnough(float cost, Runnable action) {
        if(hasEnough(cost)) {
            use(cost);
            action.run();
        }
    }

    public boolean hasEnough(float cost) {
        return current >= cost;
    }

    public void use(float used) {
        current -= used;
    }

    public void remove() {}

    public void autoRestore(float deltaTime) {
        restore(gainPerSecond * deltaTime);
    }

    public void restore(float mana) {
        this.current = Math.min(this.current + mana, this.max);
    }

}

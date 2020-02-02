package net.akami.yggdrasil.api.mana;

public class ManaContainer {

    protected float currentMana;
    protected float maxMana;
    protected float gainPerSecond;

    public ManaContainer(float maxMana, float gainPerSecond) {
        this.maxMana = maxMana;
        this.currentMana = maxMana;
        this.gainPerSecond = gainPerSecond;
    }

    public boolean hasEnoughMana(float cost) {
        return currentMana >= cost;
    }

    public void use(float used) {
        currentMana -= used;
    }

    public void ifEnoughMana(float cost, Runnable action) {
        if(hasEnoughMana(cost)) {
            use(cost);
            action.run();
        }
    }

    public void restore(float mana) {
        this.currentMana += mana;
        if(currentMana > maxMana) {
            currentMana = maxMana;
        }
    }

    public void autoRestore(float deltaTime) {
        restore(gainPerSecond * deltaTime);
    }
}

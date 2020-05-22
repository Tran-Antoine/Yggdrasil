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
        if(currentMana < 0) {
            System.err.print("Warning : a ManaContainer has been registered to have negative mana");
            currentMana = 0;
        }
    }

    public void setPreciseAmount(float amount) {
        currentMana = amount > maxMana ? maxMana : amount;
    }

    public void setPreciseMaxMana(float maxMana) {
        this.maxMana = maxMana;
    }

    public void enhanceRegeneration(float additionalGainPerSecond) {
        this.gainPerSecond += additionalGainPerSecond;
    }

    public void slowdownRegeneration(float slowDownValue) {
        this.gainPerSecond -= slowDownValue > gainPerSecond ? gainPerSecond : slowDownValue;
    }

    public void setPreciseRegeneration(float gainPerSecond) {
        this.gainPerSecond = gainPerSecond;
    }

    public void addMaxMana(float value) {
        this.maxMana += value;
    }

    public void removeMaxMana(float value) {
        this.maxMana -= value;
    }

    public void remove() {}

    public boolean ifEnoughMana(float cost, Runnable action) {
        if(hasEnoughMana(cost)) {
            use(cost);
            action.run();
            return true;
        }
        return false;
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

package net.akami.yggdrasil.api.life;

public abstract class LifeComponent {

    protected final int max;

    protected int lives;
    protected int current;

    public LifeComponent(int lives, int max) {
        this.lives = lives;
        this.max = max;
        this.current = max;
    }

    public void damage(float damage) {

        current -= damage;

        if(current <= 0) {
            lives -= 1;
            current = max;
            checkElimination();
        }

    }

    private void checkElimination() {

        if(lives <= 0) {
            onKilled();
        }

    }

    protected abstract void onKilled();

    public void heal(float heal) {

        current = (int) Math.max(current + heal, max);

    }
}

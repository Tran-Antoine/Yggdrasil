package net.akami.yggdrasil.api.life;

public abstract class LifeComponent {

    protected int lives;
    protected int lifeLength;
    protected int currentLife;

    public LifeComponent(int lives, int lifeLength) {
        this.lives = lives;
        this.lifeLength = lifeLength;
        this.currentLife = lifeLength;
    }

    protected abstract void onKilled();

    public void damage(float damage) {
        currentLife -= damage;
        if(currentLife <= 0) {
            lives -= 1;
            currentLife = lifeLength;
            if(lives <= 0) {
                onKilled();
            }
        }
    }

    public void heal(float heal) {
        currentLife += heal;
        if(currentLife > lifeLength) {
            currentLife = lifeLength;
        }
    }
}

package net.akami.yggdrasil.life;

import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.life.LifeComponent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

public class PlayerLifeComponent extends LifeComponent {

    private final UUIDHolder idHolder;

    public PlayerLifeComponent(int lives, int lifeLength, UUIDHolder idHolder) {
        super(lives, lifeLength);
        this.idHolder = idHolder;
        update();
    }

    @Override
    public void damage(float damage) {
        super.damage(damage);
        update();
    }

    @Override
    public void heal(float heal) {
        super.heal(heal);
        update();
    }

    private void update() {
        Optional<Player> optGamePlayer = Sponge.getServer().getPlayer(idHolder.getUUID());
        if(!optGamePlayer.isPresent()) {
            YggdrasilMain.getLogger().warn("Warning : a non yggdrasil player got damaged");
            return;
        }
        Player gamePlayer = optGamePlayer.get();
        float lifePercentage = (float) currentLife / (float) lifeLength;
        updateLifeBar(gamePlayer);
        updateExpLevel(gamePlayer, lifePercentage);
        updateExpBar(gamePlayer, lifePercentage);
    }

    private void updateExpLevel(Player gamePlayer, float lifePercentage) {
        gamePlayer.offer(Keys.EXPERIENCE_LEVEL, Math.round(lifePercentage * 100));

    }

    private void updateLifeBar(Player gamePlayer) {
        if(lives == 0) {
            gamePlayer.offer(Keys.HEALTH, 0D);
            return;
        }
        double halfHeartValue = gamePlayer.get(Keys.HEALTH_SCALE).orElse(1D);
        gamePlayer.offer(Keys.MAX_HEALTH, 2 * lives * halfHeartValue);
    }

    private void updateExpBar(Player gamePlayer, float lifePercentage) {
        float barLength = gamePlayer.get(Keys.EXPERIENCE_FROM_START_OF_LEVEL).get();
        float level =  lifePercentage * barLength;
        gamePlayer.offer(Keys.EXPERIENCE_SINCE_LEVEL, Math.round(level)-1);
    }

    @Override
    public void onKilled() {
        YggdrasilMain.getLogger().info("Player has been killed");
    }
}

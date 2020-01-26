package net.akami.yggdrasil.player;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

public class PlayerLifeComponent extends LifeComponent {

    private UUIDHolder idHolder;

    public PlayerLifeComponent(int lives, int lifeLength, UUIDHolder idHolder) {
        super(lives, lifeLength);
        this.idHolder = idHolder;
    }

    @Override
    public void damage(float damage) {
        super.damage(damage);
        Optional<Player> optGamePlayer = Sponge.getServer().getPlayer(idHolder.getUUID());
        if(!optGamePlayer.isPresent()) {
            System.out.println("Warning : a non yggdrasil idHolder got damaged");
            return;
        }
        Player gamePlayer = optGamePlayer.get();
        gamePlayer.offer(Keys.EXPERIENCE_LEVEL, lives);

        float barLength = gamePlayer.get(Keys.EXPERIENCE_FROM_START_OF_LEVEL).get();
        float level = (float) currentLife / (float) lifeLength * barLength;
        gamePlayer.offer(Keys.EXPERIENCE_SINCE_LEVEL, Math.round(level));
    }

    @Override
    public void onKilled() {
        System.out.println("Player has been killed");
    }
}

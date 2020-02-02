package net.akami.yggdrasil.mana;

import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.mana.ManaContainer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class PlayerManaContainer extends ManaContainer {

    private UUIDHolder idHolder;
    private ServerBossBar indicator;

    public PlayerManaContainer(float maxMana, float gainPerSecond, UUIDHolder idHolder) {
        super(maxMana, gainPerSecond);
        this.idHolder = idHolder;
        this.indicator = loadIndicator();
        update();
    }

    private ServerBossBar loadIndicator() {
        return ServerBossBar.builder()
                .color(BossBarColors.PURPLE)
                .name(Text.of("Current mana state"))
                .overlay(BossBarOverlays.PROGRESS)
                .percent(1f)
                .build();
    }

    @Override
    public void use(float used) {
        super.use(used);
        update();
    }

    @Override
    public void restore(float mana) {
        super.restore(mana);
        update();
    }

    private void update() {
        Optional<Player> optGamePlayer = Sponge.getServer().getPlayer(idHolder.getUUID());
        if(!optGamePlayer.isPresent()) {
            return;
        }
        Player gamePlayer = optGamePlayer.get();
        if(!indicator.getPlayers().contains(gamePlayer)) {
            indicator.addPlayer(gamePlayer);
        }
        float percentage = currentMana / maxMana;
        indicator.setPercent(percentage);
        if(percentage < 0.15) {
            indicator.setColor(BossBarColors.RED);
        } else if(percentage < 0.40) {
            indicator.setColor(BossBarColors.PINK);
        } else {
            indicator.setColor(BossBarColors.PURPLE);
        }
    }
}

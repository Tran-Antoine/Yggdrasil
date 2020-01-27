package net.akami.yggdrasil.mana;

import net.akami.yggdrasil.input.UUIDHolder;
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
                .color(BossBarColors.GREEN)
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
            System.out.println("Warning : a non yggdrasil player used mana");
            return;
        }
        Player gamePlayer = optGamePlayer.get();
        if(!indicator.getPlayers().contains(gamePlayer)) {
            indicator.addPlayer(gamePlayer);
        }
        indicator.setPercent(currentMana / maxMana);
    }
}

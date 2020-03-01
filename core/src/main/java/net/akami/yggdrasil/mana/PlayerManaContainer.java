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

    private final UUIDHolder idHolder;
    private final ServerBossBar indicator;

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
    public void remove() {
        getSpongePlayer().ifPresent(indicator::removePlayer);
    }

    private Optional<Player> getSpongePlayer() {
        return Sponge.getServer().getPlayer(idHolder.getUUID());
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

        getSpongePlayer().ifPresent(this::updateIndicator);
    }

    private void updateIndicator(Player player) {

        if(!indicator.getPlayers().contains(player)) {
            indicator.addPlayer(player);
        }

        setIndicatorProperties(current / max);

    }

    private void setIndicatorProperties(float percentage) {

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

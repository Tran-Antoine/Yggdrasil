package net.akami.yggdrasil.api.spell;

import org.spongepowered.api.entity.living.player.Player;

public class SpellTimeTier<T extends SpellLauncher<T>> implements SpellTier<T> {

    private int time;

    public SpellTimeTier(int time) {
        this.time = time;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<T> data) {
        data.setProperty("time", time);
    }
}

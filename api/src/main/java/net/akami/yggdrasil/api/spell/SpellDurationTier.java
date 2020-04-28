package net.akami.yggdrasil.api.spell;

import org.spongepowered.api.entity.living.player.Player;

import java.util.concurrent.TimeUnit;

public class SpellDurationTier<T extends SpellLauncher<T>> implements SpellTier<T> {

    private int value;
    private TimeUnit unit;

    public SpellDurationTier(int value) {
        this(value, TimeUnit.SECONDS);
    }

    public SpellDurationTier(int value, TimeUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<T> data) {
        data.setProperty("time", value);
        data.setProperty("time_unit", unit);
    }
}

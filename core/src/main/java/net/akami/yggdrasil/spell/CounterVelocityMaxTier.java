package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class CounterVelocityMaxTier implements SpellTier<CounterVelocityLauncher> {

    private final double maxVelocityCountered;

    public CounterVelocityMaxTier(double maxVelocityCountered) {
        this.maxVelocityCountered = maxVelocityCountered;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<CounterVelocityLauncher> data) {
        data.setProperty("max_velocity_countered", maxVelocityCountered);
    }
}

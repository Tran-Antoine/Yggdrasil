package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class AirBulletVelocityTier implements SpellTier<AirBulletLauncher> {

    private double velocity;

    public AirBulletVelocityTier(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<AirBulletLauncher> data) {
        data.setProperty("velocity_factor", velocity);
    }
}

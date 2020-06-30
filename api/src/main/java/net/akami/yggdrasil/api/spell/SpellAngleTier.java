package net.akami.yggdrasil.api.spell;

import org.spongepowered.api.entity.living.player.Player;

public class SpellAngleTier<T extends SpellLauncher<T>> implements SpellTier<T> {

    private float angle;

    public SpellAngleTier(float angle) {
        this.angle = angle;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<T> data) {
        data.setProperty("angle", angle);
    }
}

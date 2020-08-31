package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class WaterWallDurationTier implements SpellTier {

    private int duration;

    public WaterWallDurationTier(int duration) {
        this.duration = duration;
    }


    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData data) {
        data.setProperty("duration", this.duration);
    }
}

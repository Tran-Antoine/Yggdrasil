package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class WaterWallBaseTier implements SpellTier {

    private int length, width, height, duration;

    public WaterWallBaseTier() {
        this.length = 5;
        this.width = 1;
        this.height = 2;
        this.duration = 2;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData data) {
        data.setProperty("length", this.length);
        data.setProperty("width", this.width);
        data.setProperty("height", this.height);
        data.setProperty("duration", this.duration);
    }
}

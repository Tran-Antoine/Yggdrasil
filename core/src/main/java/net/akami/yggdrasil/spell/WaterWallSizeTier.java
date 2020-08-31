package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class WaterWallSizeTier implements SpellTier {

    private int length;
    private int width;
    private int height;

    public WaterWallSizeTier(int length, int height, int width) {
        this.length = length;
        this.width = width;
        this.height = height;

    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData data) {
        data.setProperty("length", this.length);
        data.setProperty("width", this.width);
        data.setProperty("height", this.height);


    }
}

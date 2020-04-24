package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class EarthTowerDimensionTier implements SpellTier<EarthTowerLauncher> {

    private int radius;
    private int height;

    public EarthTowerDimensionTier(int radius, int height) {
        this.radius = radius;
        this.height = height;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<EarthTowerLauncher> data) {
        data.setProperty("radius", radius);
        data.setProperty("height", height);
    }
}

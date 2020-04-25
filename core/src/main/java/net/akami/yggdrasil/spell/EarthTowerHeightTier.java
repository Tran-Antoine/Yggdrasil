package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class EarthTowerHeightTier implements SpellTier<EarthTowerLauncher> {

    private int height;

    public EarthTowerHeightTier(int height) {
        this.height = height;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<EarthTowerLauncher> data) {
        data.setProperty("height", height);
    }
}

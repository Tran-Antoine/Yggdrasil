package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import net.akami.yggdrasil.spell.EarthTowerLauncher.CenterRetriever;
import org.spongepowered.api.entity.living.player.Player;

public class EarthTowerBaseTier implements SpellTier<EarthTowerLauncher> {

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<EarthTowerLauncher> data) {
        data.setProperty("radius", 1);
        data.setProperty("height",  5);
        data.setProperty("center_retriever", CenterRetriever.DEFAULT);
    }
}

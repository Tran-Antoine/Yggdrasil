package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import net.akami.yggdrasil.spell.EarthTowerLauncher.CenterRetriever;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

public class EarthTowerUnderneathTier implements SpellTier<EarthTowerLauncher> {

    private static final CenterRetriever UNDERNEATH = (caster, map) -> Optional.of(caster.getPosition().toInt());

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<EarthTowerLauncher> data) {
        data.setProperty("center_retriever", CenterRetriever.DEFAULT.or(UNDERNEATH));
    }
}

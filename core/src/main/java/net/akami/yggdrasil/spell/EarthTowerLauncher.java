package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.entity.living.player.Player;

public class EarthTowerLauncher implements SpellLauncher<EarthTowerLauncher> {

    @Override
    public LaunchResult commonLaunch(SpellCreationData<EarthTowerLauncher> data, Player caster) {
        return null;
    }
}

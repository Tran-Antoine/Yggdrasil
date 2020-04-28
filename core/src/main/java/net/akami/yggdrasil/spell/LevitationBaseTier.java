package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class LevitationBaseTier implements SpellTier<LevitationSpellLauncher> {

    private final double speed;

    public LevitationBaseTier(double speed) {
        this.speed = speed;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<LevitationSpellLauncher> data) {
        data.setProperty("speed", speed);
    }
}

package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class WaterPrisonDurationTier implements SpellTier<WaterPrisonLauncher> {

    private final int lifeSpan;

    public WaterPrisonDurationTier(int lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<WaterPrisonLauncher> data) {
        data.setProperty("life_span", lifeSpan);
    }
}

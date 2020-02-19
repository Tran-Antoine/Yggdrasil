package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class WaterPrisonDurationTier implements SpellTier {

    private int lifeSpan;

    public WaterPrisonDurationTier(int lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData data) {
        data.setProperty("life_span", lifeSpan);
    }
}

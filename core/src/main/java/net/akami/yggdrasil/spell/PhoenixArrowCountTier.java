package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class PhoenixArrowCountTier implements SpellTier<PhoenixArrowLauncher> {

    private int count;

    public PhoenixArrowCountTier(int count) {
        this.count = count;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData data) {
        data.setProperty("arrowsCount", count);
    }
}

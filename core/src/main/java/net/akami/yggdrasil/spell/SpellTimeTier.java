package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class SpellTimeTier<T extends SpellLauncher<?>> implements SpellTier<T> {

    private long time;

    public SpellTimeTier(long time) {
        this.time = time;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<T> data) {
        data.setProperty("time", time);
    }
}

package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class IncendiaExplosionTier implements SpellTier<IncendiaSpellLauncher> {

    private final int radius;

    public IncendiaExplosionTier(int radius) {
        this.radius = radius;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<IncendiaSpellLauncher> data) {
        data.setProperty("explosion_radius", radius);
    }
}

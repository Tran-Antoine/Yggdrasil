package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class SpellRadiusTier<T extends SpellLauncher<?>> implements SpellTier<T> {

    private int radius;

    public SpellRadiusTier(int radius) {
        this.radius = radius;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<T> data) {
        data.setProperty("radius", radius);
    }
}

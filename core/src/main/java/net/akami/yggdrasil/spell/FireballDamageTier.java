package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class FireballDamageTier implements SpellTier<FireballSpellLauncher> {

    private final int radius;
    private final double damage;

    public FireballDamageTier(int radius, double damage) {
        this.radius = radius;
        this.damage = damage;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<FireballSpellLauncher> data) {
        data.setProperty("radius", radius);
        data.setProperty("damage", damage);
    }
}

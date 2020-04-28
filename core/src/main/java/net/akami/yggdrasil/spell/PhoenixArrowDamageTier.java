package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class PhoenixArrowDamageTier implements SpellTier<PhoenixArrowLauncher> {

    private final double damage;

    public PhoenixArrowDamageTier(double damage) {
        this.damage = damage;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<PhoenixArrowLauncher> data) {
        data.setProperty("damage", damage);
    }
}

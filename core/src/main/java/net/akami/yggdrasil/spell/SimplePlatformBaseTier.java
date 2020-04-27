package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellRadiusTier;
import net.akami.yggdrasil.api.spell.SpellTier;
import net.akami.yggdrasil.api.spell.SpellTimeTier;
import org.spongepowered.api.entity.living.player.Player;

public class SimplePlatformBaseTier implements SpellTier<SimplePlatformLauncher> {

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<SimplePlatformLauncher> data) {
        new SpellTimeTier<SimplePlatformLauncher>(5).definePreLaunchProperties(caster, data);
        new SpellRadiusTier<SimplePlatformLauncher>(2).definePreLaunchProperties(caster, data);
    }
}

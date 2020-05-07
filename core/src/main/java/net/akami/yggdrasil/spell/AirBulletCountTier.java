package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class AirBulletCountTier implements SpellTier<AirBulletLauncher> {

    private int bulletCount;

    public AirBulletCountTier(int bulletCount) {
        this.bulletCount = bulletCount;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<AirBulletLauncher> data) {
        data.setProperty("bullet_count", bulletCount);
    }
}

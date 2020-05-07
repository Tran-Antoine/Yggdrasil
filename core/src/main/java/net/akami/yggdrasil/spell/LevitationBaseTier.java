package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class LevitationBaseTier implements SpellTier<LevitationSpellLauncher> {

    private final double speed;
    private final InteractiveItemHandler handler;

    public LevitationBaseTier(double speed) {
        this(speed, null);
    }

    public LevitationBaseTier(double speed, InteractiveItemHandler handler) {
        this.speed = speed;
        this.handler = handler;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<LevitationSpellLauncher> data) {
        data.setProperty("speed", speed);
        if(handler != null) {
            data.setHandler(handler);
        }
    }
}

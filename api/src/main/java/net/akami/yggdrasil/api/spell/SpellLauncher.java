package net.akami.yggdrasil.api.spell;

import org.spongepowered.api.entity.living.player.Player;

public interface SpellLauncher {

    void commonLaunch(SpellCreationData data, Player caster);

    default void launch(SpellCreationData data, Player caster) {
        commonLaunch(data, caster);
        data.performActions(caster);
    }
}

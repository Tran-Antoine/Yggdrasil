package net.akami.yggdrasil.api.spell;

import org.spongepowered.api.entity.living.player.Player;

public interface SpellLauncher<SELF extends SpellLauncher<SELF>> {

    void commonLaunch(SpellCreationData<SELF> data, Player caster);

    default void launch(SpellCreationData<SELF> data, Player caster) {
        data.performPreActions(caster, (SELF) this);
        commonLaunch(data, caster);
        data.performPostActions(caster, (SELF) this);
    }
}

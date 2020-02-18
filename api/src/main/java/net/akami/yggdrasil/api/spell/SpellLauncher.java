package net.akami.yggdrasil.api.spell;

import org.spongepowered.api.entity.living.player.Player;

public interface SpellLauncher<SELF extends SpellLauncher<SELF>> {

    LaunchResult commonLaunch(SpellCreationData<SELF> data, Player caster);

    default void launch(SpellCreationData<SELF> data, Player caster, MagicUser user) {
        data.performPreActions(caster, (SELF) this);
        LaunchResult result = commonLaunch(data, caster);
        if(result == LaunchResult.SUCCESS) {
            data.excludeTargetSpells(user);
            data.performPostActions(caster, (SELF) this);
        }
    }

    enum LaunchResult {
        SUCCESS,
        FAIL
    }
}

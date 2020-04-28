package net.akami.yggdrasil.api.spell;

import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;

public interface SpellLauncher<SELF extends SpellLauncher<SELF>> {

    LaunchResult commonLaunch(SpellCreationData<SELF> data, Player caster);

    default void launch(SpellCreationData<SELF> data, Player caster) {
        data.performPreActions(caster, (SELF) this);

        LaunchResult result = commonLaunch(data, caster);
        if(result == LaunchResult.SUCCESS) {
            data.excludeTargetSpells();
            data.performPostActions(caster, (SELF) this);
        } else {
            caster.playSound(SoundTypes.ENTITY_WITCH_HURT, caster.getPosition(), 2);
        }
    }

    enum LaunchResult {
        SUCCESS,
        FAIL
    }
}

package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

public class LevitationSpellLauncher implements SpellLauncher<LevitationSpellLauncher> {
    @Override
    public LaunchResult commonLaunch(SpellCreationData<LevitationSpellLauncher> data, Player caster) {
        double speed = data.getPropertyMap().getProperty("speed", Double.class);
        if(Math.abs(caster.getVelocity().getY()) < 0.3) {
            Vector3d initV = caster.getVelocity();
            caster.setVelocity(new Vector3d(initV.getX(), speed, initV.getZ()));
            caster.offer(Keys.HAS_GRAVITY, false);
            Task.builder()
                    .delay(10, TimeUnit.SECONDS)
                    .execute(() -> caster.offer(Keys.HAS_GRAVITY, true))
                    .submit(YggdrasilMain.getPlugin());

            return LaunchResult.SUCCESS;
        }
        return LaunchResult.FAIL;
    }
}

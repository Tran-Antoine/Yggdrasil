package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;

public class CounterVelocityLauncher implements SpellLauncher<CounterVelocityLauncher> {

    private Vector3d counteredVelocity;

    @Override
    public LaunchResult commonLaunch(SpellCreationData<CounterVelocityLauncher> data, Player caster) {
        Vector3d currentVelocity = caster.getVelocity();
        PropertyMap map = data.getPropertyMap();
        double maxVelocityCountered = map.getProperty("max_velocity_countered", Double.class);
        this.counteredVelocity = currentVelocity.length() <= maxVelocityCountered
                ? currentVelocity
                : currentVelocity.normalize().mul(maxVelocityCountered);

        caster.setVelocity(currentVelocity.sub(counteredVelocity));
        float fakeFallingDistance = (float) YggdrasilMath.velocityToFallingDistance(caster.getVelocity().getY());
        caster.offer(Keys.FALL_DISTANCE, fakeFallingDistance);
        return LaunchResult.SUCCESS;
    }

    public Vector3d getCounteredVelocity() {
        return counteredVelocity;
    }
}

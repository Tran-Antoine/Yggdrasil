package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.utils.PlayerUtils;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class AirBulletLauncher implements SpellLauncher<AirBulletLauncher> {

    
    @Override
    public LaunchResult commonLaunch(SpellCreationData<AirBulletLauncher> data, Player caster) {
        Vector3d dir = YggdrasilMath.headRotationToDirection(caster.getHeadRotation());
        World world = caster.getWorld();
        Entity bullet = world.createEntity(EntityTypes.SHULKER_BULLET, caster
                .getPosition()
                .add(dir.mul(3))
                .add(0, 1.2, 0));

        Optional<Entity> optTarget = PlayerUtils.getNearestPlayer(caster);
        if(!optTarget.isPresent()) return LaunchResult.FAIL;

        world.spawnEntity(bullet);
        bullet.offer(Keys.VELOCITY, dir.add(0, 0.2, 0));
        bullet.offer(Keys.ACCELERATION, dir.mul(0.001));

        return LaunchResult.SUCCESS;
    }
}

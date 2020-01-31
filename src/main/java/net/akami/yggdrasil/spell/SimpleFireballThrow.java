package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.utils.YggdrasilMath;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

public class SimpleFireballThrow implements Spell {

    @Override
    public void cast(Player caster) {
        Vector3d dir = YggdrasilMath.headRotationToDirection(caster.getHeadRotation());
        World currentWorld = caster.getWorld();
        Vector3d ballLocation = caster.getPosition()
                .add(0, 1, 0)
                .add(dir.mul(2));
        Entity fireBall = currentWorld.createEntity(EntityTypes.FIREBALL, ballLocation);
        currentWorld.spawnEntity(fireBall);
        fireBall.offer(Keys.VELOCITY, dir.mul(1.3));
        fireBall.offer(Keys.ACCELERATION, dir.mul(0.05));
        System.out.println(fireBall.get(Keys.EXPLOSION_RADIUS));
        fireBall.offer(Keys.ATTACK_DAMAGE, 3D);
    }
}

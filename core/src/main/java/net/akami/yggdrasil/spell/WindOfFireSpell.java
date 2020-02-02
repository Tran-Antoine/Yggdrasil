package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class WindOfFireSpell implements Spell {

    @Override
    public void cast(Player caster) {
        Vector3d dir = YggdrasilMath.headRotationToDirection(caster.getHeadRotation());
        World currentWorld = caster.getWorld();
        Vector3d ballLocation = caster.getPosition()
                .add(0, 3, 0);

        for (int i = -3; i <= 3; i += 3) {
            for (int j = -3; j <= 3; j+=3) {
                for (int k = 0; k <= 3; k += 3) {
                    Vector3d location = ballLocation.add(dir.add(i, k, j));
                    Entity fireBall = currentWorld.createEntity(EntityTypes.FIREBALL, location);
                    currentWorld.spawnEntity(fireBall);
                    fireBall.offer(Keys.VELOCITY, dir.mul(1.3));
                    fireBall.offer(Keys.ACCELERATION, dir.mul(0.05));
                    fireBall.offer(Keys.EXPLOSION_RADIUS, Optional.of(2));
                    fireBall.offer(Keys.ATTACK_DAMAGE, 3D);
                }
            }
        }
    }
}

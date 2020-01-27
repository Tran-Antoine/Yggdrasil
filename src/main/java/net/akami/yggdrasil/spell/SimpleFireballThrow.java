package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.utils.YggdrasilMath;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

public class SimpleFireballThrow implements Spell {

    @Override
    public void cast(Player caster) {
        Vector3d dir = YggdrasilMath.headRotationToDirection(caster.getHeadRotation());
        World currentWorld = caster.getWorld();
        Entity fireBall = currentWorld.createEntity(EntityTypes.FIREBALL, caster.getPosition().add(0, 2, 0));
        fireBall.setVelocity(dir.mul(10));
    }
}

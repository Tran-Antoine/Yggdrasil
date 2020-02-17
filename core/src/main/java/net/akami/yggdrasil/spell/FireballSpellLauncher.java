package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class FireballSpellLauncher implements SpellLauncher {

    @Override
    public void commonLaunch(SpellCreationData data, Player caster) {

        Entity fireBall = createBaseEntity(caster);

        SpellCreationData.PropertyMap propertyMap = data.getPropertyMap();
        int radius = propertyMap.getProperty("radius", Integer.class);
        double damage = propertyMap.getProperty("damage", Double.class);
        fireBall.offer(Keys.EXPLOSION_RADIUS, Optional.of(radius));
        fireBall.offer(Keys.ATTACK_DAMAGE, damage);
    }

    private Entity createBaseEntity(Player caster) {

        Vector3d dir = YggdrasilMath.headRotationToDirection(caster.getHeadRotation());
        World currentWorld = caster.getWorld();
        Vector3d ballLocation = caster.getPosition()
                .add(0, 0.7, 0)
                .add(dir.mul(3));
        Entity fireBall = currentWorld.createEntity(EntityTypes.FIREBALL, ballLocation);
        fireBall.offer(Keys.VELOCITY, dir.mul(1.3));
        fireBall.offer(Keys.ACCELERATION, dir.mul(0.05));
        currentWorld.spawnEntity(fireBall);
        return fireBall;
    }
}

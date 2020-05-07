package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.ShulkerBullet;
import org.spongepowered.api.entity.explosive.Explosive;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.CollideEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AirBulletLauncher implements SpellLauncher<AirBulletLauncher> {

    private Set<UUID> bullets;
    private int explosionRadius;
    private int maxCount;
    private int currentCount = 0;

    public AirBulletLauncher() {
        this.bullets = new HashSet<>();
    }

    @Listener
    public void onProjectileLanded(CollideEvent.Impact event, @First ShulkerBullet bullet) {
        UUID id = bullet.getUniqueId();
        if(!bullets.contains(id)) return;

        bullets.remove(id);
        World world = bullet.getWorld();
        Explosive tnt = (Explosive) world.createEntity(EntityTypes.PRIMED_TNT, bullet.getLocation().getPosition());
        tnt.offer(Keys.EXPLOSION_RADIUS, Optional.of(explosionRadius));
        world.spawnEntity(tnt);
        tnt.detonate();

        if(bullets.isEmpty()) unregister();
    }

    @Override
    public LaunchResult commonLaunch(SpellCreationData<AirBulletLauncher> data, Player caster) {

        PropertyMap map = data.getPropertyMap();
        this.explosionRadius = map.getPropertyOrElse("radius", 1);
        this.maxCount = map.getPropertyOrElse("bullet_count", 1);
        double velocityFactor = map.getPropertyOrElse("velocity_factor", 1.0);

        Task.builder()
                .interval(200, TimeUnit.MILLISECONDS)
                .execute((task) -> this.createAttack(task, caster.getUniqueId(), velocityFactor))
                .submit(YggdrasilMain.getPlugin());

        register();

        return LaunchResult.SUCCESS;
    }

    private void createAttack(Task task, UUID casterID, double velocityFactor) {

        Optional<Player> optPlayer = Sponge.getServer().getPlayer(casterID);
        if(!optPlayer.isPresent()) {
            task.cancel();
            return;
        }
        Player caster = optPlayer.get();
        spawnEntity(caster, velocityFactor);

        currentCount++;
        if(currentCount >= maxCount) {
            task.cancel();
        }
    }

    private void spawnEntity(Player caster, double velocityFactor) {
        Vector3d dir = YggdrasilMath.headRotationToDirection(caster.getHeadRotation());
        World world = caster.getWorld();
        Entity bullet = world.createEntity(EntityTypes.SHULKER_BULLET, caster
                .getPosition()
                .add(dir.mul(3))
                .add(0, 1.2, 0));

        world.spawnEntity(bullet);
        bullet.offer(Keys.VELOCITY, dir.add(0, 0.2, 0).mul(velocityFactor));
        bullet.offer(Keys.ACCELERATION, dir.mul(0.001));

        bullets.add(bullet.getUniqueId());
    }

    private void register() {
        Sponge.getEventManager().registerListeners(YggdrasilMain.getPlugin(), this);
    }

    private void unregister() {
        Sponge.getEventManager().unregisterListeners(this);
    }
}

package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.explosive.Explosive;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.arrow.Arrow;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.CollideEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PhoenixArrowLauncher implements SpellLauncher<PhoenixArrowLauncher> {

    private int arrowsCount;
    private int arrowsSummoned = 0;
    List<UUID> arrows;

    public PhoenixArrowLauncher() {
        this.arrows = new ArrayList<>();
    }

    @Listener
    public void onProjectileLanded(CollideEvent.Impact event, @First Arrow arrow) {

        if(!arrows.remove(arrow.getUniqueId())) {
            return;
        }
        World world = arrow.getWorld();
        arrow.remove();
        Explosive tnt = (Explosive) world.createEntity(EntityTypes.PRIMED_TNT, arrow.getLocation().getPosition());
        world.spawnEntity(tnt);
        tnt.detonate();

        checkUnregistration();
    }

    @Listener
    public void onProjectileKilled(DestructEntityEvent event) {
        arrows.remove(event.getTargetEntity().getUniqueId());
        checkUnregistration();
    }

    private void checkUnregistration() {
        if(arrows.size() == 0) {
            Sponge.getEventManager().unregisterListeners(this);
        }
    }

    @Override
    public void commonLaunch(SpellCreationData data, Player caster) {

        Object plugin = Sponge.getPluginManager().getPlugin("yggdrasil").get();

        if(data.hasProperty("explosive")) {
            Sponge.getEventManager().registerListeners(plugin,this);
        }
        PropertyMap map = data.getPropertyMap();
        this.arrowsCount = map.getPropertyOrElse("arrowsCount", Integer.class, 1);
        Task.builder()
                .interval(300, TimeUnit.MILLISECONDS)
                .execute(task -> summonArrow(task, map, caster))
                .submit(plugin);

    }

    private void summonArrow(Task task, PropertyMap map, Player caster) {

        Vector3d dir = YggdrasilMath.headRotationToDirection(caster.getHeadRotation());
        Vector3d arrowPosition = caster.getPosition()
                .add(dir.mul(2))
                .add(0, 1, 0);
        World world = caster.getWorld();
        Entity arrow = world.createEntity(EntityTypes.TIPPED_ARROW, arrowPosition);
        arrow.offer(Keys.HAS_GRAVITY, false);

        double velocityFactor = map.getPropertyOrElse("velocity_factor", Double.class, 1.3d);
        Vector3d arrowDirection = dir.mul(velocityFactor);

        arrow.offer(Keys.VELOCITY, arrowDirection);
        arrow.offer(Keys.ACCELERATION, arrowDirection.mul(0.0001));
        arrow.offer(Keys.FIRE_TICKS, 100000);
        arrow.offer(Keys.ATTACK_DAMAGE, map.getProperty("damage", Double.class));
        world.spawnEntity(arrow);
        this.arrows.add(arrow.getUniqueId());

        arrowsSummoned++;
        if(arrowsSummoned >= arrowsCount) {
            task.cancel();
        }
    }
}

package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class PhoenixArrowGuidanceTier implements SpellTier<PhoenixArrowLauncher> {

    private PhoenixArrowLauncher launcher;
    private World world;
    private UUID playerTarget;
    private int count = 0;

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<PhoenixArrowLauncher> data) {
        data.addPostAction(this::followEnemy);
        data.setProperty("arrowsCount", 2);
    }

    private void followEnemy(Player player, PhoenixArrowLauncher launcher) {
        this.launcher = launcher;
        this.world = player.getWorld();
        this.playerTarget = findClosestPlayer(player);

        if(playerTarget == null) {
            return;
        }

        Object plugin = YggdrasilMain.getPlugin();
        Task.builder()
                .delay(500, TimeUnit.MILLISECONDS)
                .interval(200, TimeUnit.MILLISECONDS)
                .execute(this::run)
                .submit(plugin);
    }

    private UUID findClosestPlayer(Player player) {

        Collection<Entity> worldPlayers = player.getWorld().getEntities(e -> isOtherPlayer(e, player));
        Map<Entity, Double> distanceMap = new HashMap<>();

        if(worldPlayers.size() == 0) return null;

        worldPlayers.forEach(entity -> distanceMap.put(entity, distance(entity, player)));
        List<Entry<Entity, Double>> orderedDistances = new ArrayList<>(distanceMap.entrySet());
        if(orderedDistances.size() == 0) {
            return null;
        }
        orderedDistances.sort(Entry.comparingByValue());
        return orderedDistances
                .get(0)
                .getKey()
                .getUniqueId();
    }

    private boolean isOtherPlayer(Entity test, Player player) {
        return test.getType() == EntityTypes.PLAYER && test != player;
    }
    private double distance(Entity a, Entity b) {
        Vector3d posA = a.getLocation().getPosition();
        Vector3d posB = b.getLocation().getPosition();
        return posA.distance(posB);
    }


    private void run(Task task) {
        List<Entity> arrows = launcher.getAsEntities(world);
        count++;
        if (count >= 500 || arrows.isEmpty()) {
            task.cancel();
        }
        Optional<Entity> optPlayer = world.getEntity(playerTarget);
        if (!optPlayer.isPresent()) {
            task.cancel();
            return;
        }

        Entity target = optPlayer.get();
        Vector3d targetLocation = target.getLocation().getPosition();
        for (Entity arrow : arrows) {
            redirect(targetLocation, arrow);
        }
    }

    private void redirect(Vector3d targetLocation, Entity arrow) {

        Vector3d arrowLocation = arrow.getLocation().getPosition();
        Vector3d currentVelocity = arrow.get(Keys.VELOCITY).orElse(Vector3d.ZERO);

        Vector3d direction = targetLocation
                .sub(arrowLocation)
                .normalize()
                .add(currentVelocity.normalize().mul(1.7))
                .normalize()
                .mul(0.6);

        arrow.offer(Keys.VELOCITY, direction);
        arrow.offer(Keys.ACCELERATION, direction.mul(0.0001));
    }
}

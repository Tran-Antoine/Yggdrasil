package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PhoenixArrowGuidanceTier implements SpellTier<PhoenixArrowLauncher> {

    private PhoenixArrowLauncher launcher;
    private World world;
    private UUID playerTarget;
    private int count = 0;

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<PhoenixArrowLauncher> data) {
        data.addAction(this::followEnemy);
    }

    private void followEnemy(Player player, PhoenixArrowLauncher launcher) {
        this.launcher = launcher;
        this.world = player.getWorld();
        this.playerTarget = findClosestPlayer(player);
        Object plugin = Sponge.getPluginManager().getPlugin("yggdrasil").get();
        Task.builder()
                .interval(500, TimeUnit.MILLISECONDS)
                .execute(this::run)
                .submit(plugin);
    }

    private UUID findClosestPlayer(Player player) {

        Collection<Player> worldPlayers = player.getWorld().getPlayers();
        Map<Player, Double> distanceMap = new HashMap<>();

        worldPlayers.forEach(entity -> distanceMap.put(entity, distance(entity, player)));
        List<Entry<Player, Double>> orderedDistances = new ArrayList<>(distanceMap.entrySet());
        orderedDistances.sort(Entry.comparingByValue());
        return orderedDistances
                .get(0)
                .getKey()
                .getUniqueId();
    }

    private double distance(Entity a, Entity b) {
        Vector3d posA = a.getLocation().getPosition();
        Vector3d posB = b.getLocation().getPosition();
        return posA.distance(posB);
    }


    private void run(Task task) {
        List<Entity> arrows = launcher.arrows
                .stream()
                .map(world::getEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        count++;
        if(count >= 20 || arrows.isEmpty()) {
            task.cancel();
        }
        Optional<Player> optPlayer = Sponge.getServer().getPlayer(playerTarget);
        if(!optPlayer.isPresent()) {
            return;
        }

        Player target = optPlayer.get();
        for(Entity arrow : arrows) {
            redirect(target, arrow);
        }
    }
}

package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class WaterPrisonCurrentTier implements SpellTier<WaterPrisonLauncher> {

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<WaterPrisonLauncher> data) {
        data.addPostAction(this::makeCurrent);
        data.setProperty("slows_down", false);
    }

    private void makeCurrent(Player player, WaterPrisonLauncher launcher) {
        Task.builder()
                .interval(600, TimeUnit.MILLISECONDS)
                .execute((task) -> moveEntities(launcher, player.getWorld(), task))
                .submit(YggdrasilMain.getPlugin());
    }

    private void moveEntities(WaterPrisonLauncher launcher, World world, Task task) {

        if(launcher.isSpellDead()) {
            task.cancel();
            return;
        }

        List<Entity> entities = launcher.getTrappedEntities()
                .stream()
                .map(world::getEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        Vector3d center = launcher.getCenter().toDouble();
        Random random = new Random();

        for(Entity entity : entities) {
            Vector3d pos = entity.getLocation().getPosition();
            Vector3d direction = center.sub(pos);
            direction = direction
                    .normalize()
                    .add(random.nextDouble(), random.nextDouble(), random.nextDouble())
                    .mul(1.5);
            entity.setVelocity(direction);
        }
    }
}

package net.akami.yggdrasil.item.list;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.spell.Spell;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class EarthElevatorSpell implements Spell {

    private int currentY = 0;

    @Override
    public void cast(Player caster) {
        Vector3d pos = caster.getPosition();
        World world = caster.getWorld();
        Object plugin = Sponge.getPluginManager().getPlugin("yggdrasil").get();
        Task
                .builder()
                .interval(80, TimeUnit.MILLISECONDS)
                .execute((currentTask) -> run(currentTask, pos, world, this::fill))
                .submit(plugin);
        caster.setVelocity(new Vector3d(0, 1.5, 0));

        Task
                .builder()
                .delay(50, TimeUnit.SECONDS)
                .interval(3500, TimeUnit.MILLISECONDS)
                .execute((currentTask) -> run(currentTask, pos, world, this::reset))
                .submit(plugin);

    }

    private void run(Task task, Vector3d loc, World world, Consumer<Location<World>> action) {

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location<World> pos = world.getLocation(loc.add(x, currentY, z));
                action.accept(pos);
            }
        }
        currentY += 1;
        if(currentY >= 10) {
            task.cancel();
            currentY = 0;
        }
    }

    private void fill(Location<World> pos) {
        if(pos.getBlockType() != BlockTypes.AIR) {
            return;
        }
        pos.setBlockType(BlockTypes.DIRT);
    }

    private void reset(Location<World> pos) {
        if(pos.getBlockType() != BlockTypes.DIRT) {
            return;
        }
        pos.setBlockType(BlockTypes.AIR);
    }
}

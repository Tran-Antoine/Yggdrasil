package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.concurrent.TimeUnit;

public class GravitySpell implements Spell {

    private int currentY = -10;
    private int blockSpawnedCount = 0;

    @Override
    public void cast(Player caster) {
        Vector3d loc = caster.getPosition();
        World world = caster.getWorld();
        Object plugin = Sponge.getPluginManager().getPlugin("yggdrasil").get();
        Task
                .builder()
                .interval(50, TimeUnit.MILLISECONDS)
                .execute((task) -> run(task, loc, world))
                .submit(plugin);
        Task
                .builder()
                .delay(20, TimeUnit.SECONDS)
                .execute(() -> killImmobileFallingBlocks(world))
                .submit(plugin);
    }

    private void run(Task task, Vector3d loc, World world) {

        int locX = loc.getFloorX();
        int locY = loc.getFloorY() + currentY;
        int locZ = loc.getFloorZ();

        for(int x = -20 + locX; x <= 20 + locX; x++) {

            for(int z = -20 + locZ; z <= 20 + locZ; z++) {

                if(blockSpawnedCount > 1200) {
                    task.cancel();
                    return;
                }

                BlockState state = world.getBlock(x, locY, z);
                BlockType type = state.getType();

                if(type == BlockTypes.AIR || type == BlockTypes.WATER || type == BlockTypes.FLOWING_WATER
                        || type == BlockTypes.LAVA || type == BlockTypes.FLOWING_LAVA) {
                    continue;
                }

                BlockState below = world.getBlock(x, locY-1, locZ);
                if(below.getType() != BlockTypes.AIR) {
                    continue;
                }

                Entity block = world.createEntity(EntityTypes.FALLING_BLOCK, new Vector3d(x, locY, z));
                block.offer(Keys.FALLING_BLOCK_STATE, state);
                world.spawnEntity(block);
                blockSpawnedCount++;
            }
        }

        currentY++;

        if(currentY >= 100 || locY > 250) {
            task.cancel();
        }
    }

    private void killImmobileFallingBlocks(World world) {
        world.getEntities(this::isImmobileFallingBlock)
                .forEach(Entity::remove);
    }

    private boolean isImmobileFallingBlock(Entity entity) {
        if(entity.getType() != EntityTypes.FALLING_BLOCK) {
            return false;
        }
        return entity.getVelocity().length() < 0.01;
    }
}

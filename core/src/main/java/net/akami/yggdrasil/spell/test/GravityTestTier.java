package net.akami.yggdrasil.spell.test;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
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

public class GravityTestTier implements SpellTier {

    private int currentY = -10;
    private int blockSpawnedCount = 0;

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData data) {
        Vector3d loc = caster.getPosition();
        World world = caster.getWorld();
        Object plugin = YggdrasilMain.getPlugin();
        Task
                .builder()
                .interval(50, TimeUnit.MILLISECONDS)
                .execute((task) -> run(task, loc, world))
                .submit(plugin);
        Task
                .builder()
                .delay(13, TimeUnit.SECONDS)
                .execute(() -> killImmobileFallingBlocks(world))
                .submit(plugin);
    }

    private void run(Task task, Vector3d loc, World world) {

        int playerLocX = loc.getFloorX();
        int y = loc.getFloorY() + currentY;
        int playerLocZ = loc.getFloorZ();

        for(int x = -20 + playerLocX; x <= 20 + playerLocX; x++) {

            for(int z = -20 + playerLocZ; z <= 20 + playerLocZ; z++) {

                if(blockSpawnedCount > 1500) {
                    System.out.println("More than 1500 blocks falling. Stopping the task");
                    task.cancel();
                    return;
                }

                BlockState state = world.getBlock(x, y, z);
                BlockType type = state.getType();

                if(type == BlockTypes.AIR || type == BlockTypes.WATER || type == BlockTypes.FLOWING_WATER
                        || type == BlockTypes.LAVA || type == BlockTypes.FLOWING_LAVA) {
                    continue;
                }
                BlockState below = world.getBlock(x, y-1, z);
                if(below.getType() != BlockTypes.AIR) {
                    continue;
                }
                Entity block = world.createEntity(EntityTypes.FALLING_BLOCK, new Vector3d(x, y, z));
                block.offer(Keys.FALLING_BLOCK_STATE, state);
                world.spawnEntity(block);
                blockSpawnedCount++;
            }
        }

        currentY++;

        if(currentY >= 100 || y > 250) {
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

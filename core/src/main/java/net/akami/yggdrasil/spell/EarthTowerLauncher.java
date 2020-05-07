package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.utils.ProbabilityLaw;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EarthTowerLauncher implements SpellLauncher<EarthTowerLauncher> {

    private static final double DEFAULT_DISTANCE = 6;
    private final Random random = new Random();
    private final Map<Vector2i, Vector2i> heights = new HashMap<>();
    private int currentDY = 0;

    @Override
    public LaunchResult commonLaunch(SpellCreationData<EarthTowerLauncher> data, Player caster) {

        PropertyMap map = data.getPropertyMap();

        CenterRetriever retriever = map.getProperty("center_retriever", CenterRetriever.class);
        int radius = map.getProperty("radius", Integer.class);
        int height = map.getProperty("height", Integer.class);
        World world = caster.getWorld();

        Optional<Vector3i> optCenter = retriever.getCenter(caster, map);
        if (!optCenter.isPresent()) {
            return LaunchResult.FAIL;
        }

        Vector3i center = optCenter.get();
        scheduleFillBlocks(center, radius, height, world);
        launchPlayerUpwards(caster, radius, center);
        return LaunchResult.SUCCESS;
    }

    private void scheduleFillBlocks(Vector3i center, int radius, int height, World world) {
        Task.builder()
                .interval(100, TimeUnit.MILLISECONDS)
                .execute((task) -> createFloor(task, center, radius, height, world))
                .submit(YggdrasilMain.getPlugin());
    }

    private void createFloor(Task task, Vector3i center, int radius, int tierHeight, World world) {

        int maxPossibleHeight = tierHeight + 5;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                Vector2i columnData = getMaxHeightForColumn(center, dx, dz, random, tierHeight, world);
                // Those getX and getY don't represent spatial coordinates, only mathematical compounds
                setBlock(dx, currentDY, columnData.getY(), dz, columnData.getX(), world, center);
            }
        }

        currentDY++;

        if(currentDY > maxPossibleHeight) {
            task.cancel();
        }
    }

    private Vector2i getMaxHeightForColumn(Vector3i center, int dx, int dz, Random random, int tierHeight, World world) {
        Vector2i column = new Vector2i(dx, dz);
        if (!heights.containsKey(column)) {
            int randomHeightFactor = random.nextInt(8) - 3 - (dx * dx + dz * dz);
            int finalHeight = tierHeight + randomHeightFactor;
            int deltaGroundingY = findDeltaGroundingY(center, dx, dz, world);

            heights.put(column, new Vector2i(finalHeight, deltaGroundingY));
        }

        return heights.get(column);
    }

    private void setBlock(int dx, int dy, int dyGrounding, int dz, int finalHeight, World world, Vector3i center) {

        if (dy >= finalHeight) {
            return;
        }

        Vector3i realPos = center.add(dx, dyGrounding + dy, dz);
        if (world.getBlockType(realPos) != BlockTypes.AIR) {
            return;
        }
        BlockType type = findBlockType(dy, finalHeight);
        world.setBlockType(realPos, type);
    }

    private int findDeltaGroundingY(Vector3i center, int dx, int dz, World world) {
        Vector3i column = center.add(dx, 0, dz);
        for (int dy = 0; dy > 1 - center.getY(); dy--) {
            if (world.getBlockType(column.add(0, dy, 0)) != BlockTypes.AIR) {
                return dy + 1;
            }
        }
        return 1 - center.getY();
    }

    private BlockType findBlockType(int dy, int maxHeight) {

        float heightRatio = (float) dy / (float) maxHeight;
        float dirtRatio = findDirtRatio(heightRatio);
        ProbabilityLaw<BlockType> law = new ProbabilityLaw<>();
        law.add(BlockTypes.GRASS, (float) Math.pow(heightRatio, 3));
        law.add(BlockTypes.STONE, 1 - heightRatio);
        law.add(BlockTypes.DIRT, dirtRatio);

        return law.draw().get();
    }

    private float findDirtRatio(float heightRatio) {
        if (heightRatio < 1) {
            return 1.5f * heightRatio;
        }
        return 0;
    }

    private void launchPlayerUpwards(Player caster, int radius, Vector3i center) {
        if (caster.getPosition().distance(center.toDouble()) > radius) {
            return;
        }
        caster.setVelocity(caster.getVelocity().add(0, 1.5, 0));
    }

    public interface CenterRetriever {

        CenterRetriever DEFAULT = (caster, map) -> {
            Vector3d arrowLoc = map.getProperty("location", Vector3d.class);
            if(arrowLoc != null) {
                return Optional.of(arrowLoc.toInt());
            }
            Vector3d dir = YggdrasilMath.headRotationToDirection(caster.getHeadRotation());
            Vector3d flatDir = new Vector3d(dir.getX(), 0, dir.getZ());

            if (flatDir.length() < 0.1) {
                return Optional.empty();
            }

            return Optional.of(caster.getPosition()
                    .add(flatDir
                            .normalize()
                            .mul(DEFAULT_DISTANCE))
                    .toInt());
        };


        Optional<Vector3i> getCenter(Player caster, PropertyMap map);

        default CenterRetriever or(CenterRetriever or) {
            return (caster, map) -> {
                Optional<Vector3i> first = this.getCenter(caster, map);
                if (first.isPresent()) return first;
                return or.getCenter(caster, map);
            };
        }
    }
}

package net.akami.yggdrasil.spell;

import com.flowpowered.math.GenericMath;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.utils.ProbabilityLaw;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Random;

public class EarthTowerLauncher implements SpellLauncher<EarthTowerLauncher> {

    private static final double DEFAULT_DISTANCE = 6;

    @Override
    public LaunchResult commonLaunch(SpellCreationData<EarthTowerLauncher> data, Player caster) {

        Vector3d dir = YggdrasilMath.headRotationToDirection(caster.getHeadRotation());
        Vector3d flatDir = new Vector3d(dir.getX(), 0, dir.getZ());

        if(flatDir.length() < GenericMath.DBL_EPSILON) {
            return LaunchResult.FAIL;
        }

        Vector3i center = caster.getPosition()
                .add(flatDir
                        .normalize()
                        .mul(DEFAULT_DISTANCE))
                .toInt();
        World world = caster.getWorld();
        PropertyMap map = data.getPropertyMap();

        int radius = map.getProperty("radius", Integer.class);
        int height = map.getProperty("height", Integer.class);

        fillBlocks(center, radius, height, world);
        return LaunchResult.SUCCESS;
    }

    private void fillBlocks(Vector3i center, int radius, int height, World world) {
        Random random = new Random();
        for(int dx = -radius; dx <= radius; dx++) {
            for(int dz = -radius; dz <= radius; dz++) {
                int randomHeightFactor = random.nextInt(6) - 3 - (dx*dx + dz*dz);
                int deltaGroundingY = findDeltaGroundingY(center, dx, dz, world);
                int finalHeight = height + randomHeightFactor;

                for(int dy = 0; dy <= finalHeight; dy++) {
                    Vector3i realPos = center.add(dx, deltaGroundingY + dy, dz);
                    if(world.getBlockType(realPos) != BlockTypes.AIR) {
                        continue;
                    }
                    BlockType type = findBlockType(dy, finalHeight);
                    world.setBlockType(realPos, type);
                }
            }
        }
    }

    private int findDeltaGroundingY(Vector3i center, int dx, int dz, World world) {
        Vector3i column = center.add(dx, 0, dz);
        for(int dy = 0; dy > 1 - center.getY(); dy--) {
            if(world.getBlockType(column.add(0, dy, 0)) != BlockTypes.AIR) {
                return dy;
            }
        }
        return 1 - center.getY();
    }

    private BlockType findBlockType(int dy, int maxHeight) {

        float heightRatio = (float) dy / (float) maxHeight;
        float dirtRatio = findDirtRatio(heightRatio);
        ProbabilityLaw<BlockType> law = new ProbabilityLaw<>();
        law.add(BlockTypes.GRASS, (float) Math.pow(heightRatio,3));
        law.add(BlockTypes.STONE, 1 - heightRatio);
        law.add(BlockTypes.DIRT, dirtRatio);

        return law.draw().get();
    }

    private float findDirtRatio(float heightRatio) {
        if(heightRatio < 1) {
            return 1.5f * heightRatio;
        }
        return 0;
    }
}

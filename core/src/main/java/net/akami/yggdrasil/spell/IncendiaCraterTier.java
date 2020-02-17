package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Random;

public class IncendiaCraterTier implements SpellTier<IncendiaSpellLauncher> {

    private SpellCreationData<IncendiaSpellLauncher> data;

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<IncendiaSpellLauncher> data) {
        this.data = data;
        data.addPreAction((futureCaster, launcher) -> makeCrater(futureCaster));
        //data.setProperty("explosion_radius", 0); // For testing only
    }

    private void makeCrater(Player caster) {
        PropertyMap map = data.getPropertyMap();
        Vector3d arrowLocation = map.getProperty("location", Vector3d.class);
        int radius = map.getProperty("radius", Integer.class);
        World world = caster.getWorld();
        Random random = new Random();

        for(int dx = -radius; dx <= radius; dx++) {
            for(int dz = -radius; dz <= radius; dz++) {
                createHole(random, arrowLocation, world, dx, dz, radius);
            }
        }
    }

    private void createHole(Random random, Vector3d arrowLocation, World world, int dx, int dz, int radius) {
        double distance = Math.sqrt(dx*dx + dz*dz);
        if(distance == 0) distance = 1;
        int depth = (int) (2 * (random.nextDouble()/2 + 0.6) * (radius/(1.5 * distance)));
        int count = 0;
        int currentDeltaY = radius;
        while(count < depth && currentDeltaY >= -radius) {
            currentDeltaY--;
            Vector3i pos = new Vector3i(
                    arrowLocation.getFloorX() + dx,
                    arrowLocation.getFloorY() + currentDeltaY,
                    arrowLocation.getFloorZ() + dz);
            if(world.getBlockType(pos) != BlockTypes.AIR) {
                count++;
                world.setBlockType(pos, BlockTypes.AIR);
            }
        }
    }
}

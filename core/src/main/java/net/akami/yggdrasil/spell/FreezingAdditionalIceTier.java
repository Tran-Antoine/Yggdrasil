package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

public class FreezingAdditionalIceTier implements SpellTier<FreezingSpellLauncher> {

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<FreezingSpellLauncher> data) {
        data.addPostAction(this::spawnExtraIce);
    }

    private void spawnExtraIce(Player player, FreezingSpellLauncher launcher) {
        int radius = launcher.getRadius();
        Collection<Vector3i> nearEntities = player.getNearbyEntities(radius)
                .stream()
                .map((e) -> e.getLocation().getPosition().toInt())
                .collect(Collectors.toList());

        World world = player.getWorld();

        for(Vector3i pos : nearEntities) {
            spawnExtraIce(pos, world);
        }
    }

    private void spawnExtraIce(Vector3i pos, World world) {
        Random random = new Random();
        world.setBlockType(pos, BlockTypes.ICE);
        for(int dx = -2; dx <= 1; dx++) {
            for(int dy = -2; dy <= 2; dy++) {
                for(int dz = -1; dz <= 2; dz++) {
                    if(random.nextFloat() > 0.8) {
                        world.setBlockType(pos.add(dx, dy, dz), BlockTypes.ICE);
                    }
                }
            }
        }
    }
}

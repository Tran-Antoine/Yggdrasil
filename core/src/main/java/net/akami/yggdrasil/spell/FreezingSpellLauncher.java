package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.spongepowered.api.block.BlockTypes.*;

public class FreezingSpellLauncher implements SpellLauncher<FreezingSpellLauncher> {

    private List<Vector3i> blocksPlaced;
    private int radius;

    public FreezingSpellLauncher() {
        this.blocksPlaced = new ArrayList<>();
    }

    @Override
    public LaunchResult commonLaunch(SpellCreationData<FreezingSpellLauncher> data, Player caster) {

        PropertyMap map = data.getPropertyMap();
        int time = map.getProperty("time", Integer.class);
        this.radius = map.getProperty("radius", Integer.class);
        Vector3i center = caster.getPosition().toInt();
        World world = caster.getWorld();

        createArea(center, radius, world);

        if(map.getPropertyOrElse("further_cancel", true)) {
            scheduleMeltIce(time, world);
        }

        return LaunchResult.SUCCESS;
    }

    private void scheduleMeltIce(int time, World world) {
        Task.builder()
                .delay(time, TimeUnit.SECONDS)
                .execute(() -> {
                    for(Vector3i pos : blocksPlaced) {
                        world.setBlockType(pos, WATER);
                    }
                })
                .submit(YggdrasilMain.getPlugin());
    }

    private void createArea(Vector3i center, int radius, World world) {
        for(long dx = -radius; dx <= radius; dx++) {
            for(long dy = -radius; dy <= radius; dy++) {
                for(long dz = -radius; dz <= radius; dz++) {
                    Vector3i pos = center.add(dx, dy, dz);
                    BlockType type = world.getBlockType(pos);
                    if(type == FLOWING_WATER || type == WATER) {
                        world.setBlockType(pos, ICE);
                        blocksPlaced.add(pos);
                    }
                }
            }
        }
    }

    public int getRadius() {
        return radius;
    }
}

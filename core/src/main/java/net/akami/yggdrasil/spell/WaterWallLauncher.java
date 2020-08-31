package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.data.manipulator.mutable.entity.VelocityData;
import org.spongepowered.api.data.property.block.MatterProperty;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class WaterWallLauncher implements SpellLauncher {

    private Set<UUID> trappedentities;
    private boolean slowdown;
    private SpellCreationData<WaterPrisonLauncher> data;
    private int length;
    private int width;
    private int height;
    private int duration;
    private Vector3i center;
    private Vector3i point;
    private int offset;
    private boolean widthIsPair;
    private Direction direction;

    private Player player;


    public WaterWallLauncher() {
        this.trappedentities = new HashSet<>();
    }

    @Override
    public LaunchResult commonLaunch(SpellCreationData data, Player caster) {

        SpellCreationData.PropertyMap map = data.getPropertyMap();
        this.data = data;
        this.length = map.getProperty("length", Integer.class);
        this.width = map.getProperty("width", Integer.class);
        this.height = map.getProperty("height", Integer.class);

        this.duration = map.getProperty("duration", Integer.class);
        this.center = map.getProperty("location", Vector3d.class).toInt();

        this.player = caster;

        World world = caster.getWorld();
        Object plugin = YggdrasilMain.getPlugin();

        createWall(length, width, height, world, caster);
        register(plugin);

        Task.builder()
                .delay(20, TimeUnit.SECONDS)
                .execute(() -> removeWater(world))
                .submit(plugin);

        return LaunchResult.SUCCESS;
    }

    private void createWall(int length, int width, int height, World world, Player player) {
        direction = getPlayerFacingDirection(player);
        widthIsPair = width % 2 == 0;
        createCuboid(length, width, height, world, BlockTypes.WATER);
    }

    private void createCuboid(int length, int width, int height, World world, BlockType blocktype) {

        offset = length % 2 == 0 ? length / 2 : (length - 1) / 2;

        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            // Vary on X

            Vector3i point = center.add(-offset, 0, 0);
            this.point = center.add(-offset, 0, 0);

            for (int dz = 1; dz <= width; dz++) {
                for (int dy = 1; dy <= height; dy++) {
                    for (int dx = 1; dx <= length; dx++) {
                        Vector3i currentPos = point.add(dx - 1, dy - 1, dz - 1);
                        world.setBlockType(currentPos, blocktype);
                    }
                }
            }

        }

        if (direction == Direction.EAST || direction == Direction.WEST) {
            // Vary on Z

            Vector3i point = center.add(0, 0, -offset);
            this.point = center.add(0, 0, -offset);

            for (int dx = 1; dx <= width; dx++) {
                for (int dy = 1; dy <= height; dy++) {
                    for (int dz = 1; dz <= length; dz++) {
                        Vector3i currentPos = point.add(dx - 1, dy - 1, dz - 1);
                        world.setBlockType(currentPos, blocktype);
                    }
                }
            }

        }

    }

    private void removeWater(World world) {
        createCuboid(length, width, height, world, BlockTypes.AIR);
        Sponge.getEventManager().unregisterListeners(this);
    }

    private Direction getPlayerFacingDirection(Player player) {

        double rotation = player.getRotation().getY();

        if ((rotation < -135) && (rotation >= -225)) {
            return Direction.NORTH;
        } else if ((rotation < -45) && (rotation >= -135)) {
            return Direction.EAST;
        } else if ((rotation < -225) && (rotation >= -315)) {
            return Direction.WEST;
        } else if ((rotation < -45) || (rotation >= -315)) {
            return Direction.SOUTH;
        } else {
            return Direction.NONE;
        }

    }

    @Listener
    public void onMove(MoveEntityEvent event) {
        Entity entity = event.getTargetEntity();

        if (vectorIsIn(entity.getLocation().getPosition())) {
            if (!entity.getUniqueId().equals(player.getUniqueId())) {
                depriveEntity(entity);
            }
        } else {
            clearEffects(entity);
        }
    }

    private void clearEffects(Entity entity) {
        
        UUID entityID = entity.getUniqueId();

        if (!trappedentities.contains(entityID)) {
            return;
        }
        data.restoreSpellAccess((magicUser) -> magicUser.getUUID().equals(entityID));
        trappedentities.remove(entityID);

    }

    public void depriveEntity(Entity entity) {

        entity.offer(Keys.WALKING_SPEED, 5.0);

        if (trappedentities.contains(entity.getUniqueId())) {
            return;
        }

        trappedentities.add(entity.getUniqueId());
        entity.offer(Keys.REMAINING_AIR, 40);
        data.excludeTargetSpells();
    }

    @Listener
    public void stopLiquidFlow(ChangeBlockEvent.Place event) {

        BlockSnapshot snapshot = event.getTransactions().get(0).getFinal();

        // Remove the event above or below the wall
        if (snapshot.getPosition().getY() < this.center.getY() - 1.5 || snapshot.getPosition().getY() > this.center.getY() + this.height + 1.5) {
            return;
        }

        if (this.direction == Direction.NORTH || this.direction == Direction.SOUTH) {

            // Vary on X
            if (snapshot.getPosition().getX() < this.point.getX() - 1.5 || snapshot.getPosition().getX() > this.point.getX() + this.length + 1.5) {
                return;
            }

            if (snapshot.getPosition().getZ() < this.point.getZ() - 1.5 || snapshot.getPosition().getZ() > this.point.getZ() + this.width + 1.5) {
                return;
            }

        } else {
            // Vary on Z
            if (snapshot.getPosition().getZ() < this.point.getZ() - 1.5 || snapshot.getPosition().getZ() > this.point.getZ() + this.length + 1.5) {
                return;
            }

            if (snapshot.getPosition().getX() < this.point.getX() - 1.5 || snapshot.getPosition().getX() > this.point.getX() + this.width + 1.5) {
                return;
            }

        }

        Optional<MatterProperty> matter = snapshot.getState().getProperty(MatterProperty.class);

        if (matter.isPresent() && matter.get().getValue() == MatterProperty.Matter.LIQUID) {
            event.setCancelled(true);
        }
    }

    private boolean vectorIsIn(Vector3d vector) {

        if (vector.getY() < this.center.getY() || vector.getY() > this.center.getY() + this.height) {
            return false;
        }

        if (this.direction == Direction.NORTH || this.direction == Direction.SOUTH) {
            // Vary on X
            if (vector.getX() < this.point.getX() || vector.getX() > this.point.getX() + this.length) {
                return false;
            }
            if (vector.getZ() < this.point.getZ() || vector.getZ() > this.point.getZ() + this.width) {
                return false;
            }
        } else {
            // Vary on Z
            if (vector.getZ() < this.point.getZ() || vector.getZ() > this.point.getZ() + this.length) {
                return false;
            }
            if (vector.getX() < this.point.getX() || vector.getX() > this.point.getX() + this.width) {
                return false;
            }
        }

        return true;
    }

    private void register(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }
}

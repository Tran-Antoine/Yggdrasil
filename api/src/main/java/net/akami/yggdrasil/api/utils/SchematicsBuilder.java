package net.akami.yggdrasil.api.utils;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.schematic.Schematic;

import java.util.function.Consumer;
import java.util.function.Function;

public class SchematicsBuilder implements Consumer<Task> {

    private Schematic schematic;
    private Location<World> startLocation;
    private int speed;
    private int currentX, currentY, currentZ;
    private Function<BlockState, SoundType> soundFunction;

    public SchematicsBuilder(Schematic blocks, Location<World> startLocation, int speed, Function<BlockState, SoundType> soundFunction) {
        this.schematic = blocks;
        this.startLocation = startLocation;
        this.speed = speed;
        this.soundFunction = soundFunction;
        reset();
    }

    public SchematicsBuilder(Schematic blocks, Location<World> startLocation, int speed) {
        this(blocks, startLocation, speed, SOUND_PLACE);
    }

    @Override
    public void accept(Task task) {
        for(int i = 0; i < speed; i++) {

            if(currentZ >= schematic.getBlockMax().getZ()) {
                currentZ = schematic.getBlockMin().getZ();
                currentX++;
                if(currentX >= schematic.getBlockMax().getX()) {
                    currentX = schematic.getBlockMin().getX();
                    currentY++;
                    if(currentY >= schematic.getBlockMax().getY()) {
                        task.cancel();
                        return;
                    }
                }
            }
            BlockState blockState = schematic.getBlock(currentX, currentY, currentZ);
            currentZ++;
            if(blockState.getType() == BlockTypes.AIR) continue;
            Location<World> newLocation = startLocation.copy()
                    .add(currentX, currentY, currentZ)
                    .sub(schematic.getBlockMin())
                    .sub(schematic.getBlockSize().getX() / 2.0, 0, schematic.getBlockSize().getZ() / 2.0);
            newLocation.setBlock(blockState);
            onBlockPlace(newLocation, blockState);
            startLocation.getExtent().playSound(soundFunction.apply(blockState), startLocation.getPosition(), 1);
        }
    }

    public void onBlockPlace(Location<World> location, BlockState blockState) {

    }

    public Schematic getSchematic() {
        return schematic;
    }

    public int getSpeed() {
        return speed;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public int getCurrentZ() {
        return currentZ;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public void setCurrentZ(int currentZ) {
        this.currentZ = currentZ;
    }

    public void reset() {
        this.currentX = schematic.getBlockMin().getX();
        this.currentY = schematic.getBlockMin().getY();
        this.currentZ = schematic.getBlockMin().getZ();
    }

    public static final Function<BlockState, SoundType> SOUND_PLACE = blockState -> blockState.getType().getSoundGroup().getPlaceSound();
    public static final Function<BlockState, SoundType> SOUND_BREAK = blockState -> blockState.getType().getSoundGroup().getBreakSound();
}

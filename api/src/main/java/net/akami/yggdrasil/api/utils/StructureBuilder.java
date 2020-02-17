package net.akami.yggdrasil.api.utils;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class StructureBuilder implements Consumer<Task> {

    private List<List<List<BlockState>>> blocks;
    private Location<World> startLocation;
    private int speed;
    private int currentX, currentY, currentZ = 0;
    private Function<BlockState, SoundType> soundFunction;

    public StructureBuilder(List<List<List<BlockState>>> blocks, Location<World> startLocation, int speed, Function<BlockState, SoundType> soundFunction) {
        this.blocks = blocks;
        this.startLocation = startLocation;
        this.speed = speed;
        this.soundFunction = soundFunction;
    }

    public StructureBuilder(List<List<List<BlockState>>> blocks, Location<World> startLocation, int speed) {
        this(blocks, startLocation, speed, SOUND_PLACE);
    }

    @Override
    public void accept(Task task) {
        for(int i = 0; i < speed; i++) {

            if(currentZ == blocks.get(currentX).get(currentY).size()) {
                currentZ = 0;
                currentY++;
                if(currentY == blocks.get(currentX).size()) {
                    currentY = 0;
                    currentX++;
                    if(currentX == blocks.size()) {
                        task.cancel();
                        return;
                    }
                }
            }
            BlockState blockState = blocks.get(currentX).get(currentY).get(currentZ);
            currentZ++;
            if(blockState.getType() == BlockTypes.AIR) continue;
            startLocation.copy()
                    .add(currentX, currentY, currentZ)
                    .setBlock(blockState);
            startLocation.getExtent().playSound(blockState.getType().getSoundGroup().getPlaceSound(), startLocation.getPosition(), 1);
        }
    }

    public List<List<List<BlockState>>> getBlocks() {
        return blocks;
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
        currentX = currentY = currentZ = 0;
    }

    public static final Function<BlockState, SoundType> SOUND_PLACE = blockState -> blockState.getType().getSoundGroup().getPlaceSound();
    public static final Function<BlockState, SoundType> SOUND_BREAK = blockState -> blockState.getType().getSoundGroup().getBreakSound();
}

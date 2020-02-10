package net.akami.yggdrasil.api.game.task;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TestVelocityTask implements Consumer<Task> {

    private Player player;
    private List<Float> fallingDistances;
    private List<Double> velocities;

    public TestVelocityTask(Player player) {
        this.player = player;
        this.fallingDistances = new ArrayList<>();
        this.velocities = new ArrayList<>();
    }

    @Override
    public void accept(Task task) {
        Optional<Float> time = player.get(Keys.FALL_DISTANCE);
        if(!time.isPresent() || time.get() == 0) return;
        double velocity = -player.getVelocity().getY();
        fallingDistances.add(time.get());
        velocities.add(velocity);
        System.out.println("Values : "+ fallingDistances.size());
        if(fallingDistances.size() >= 100) {
            task.cancel();
            try {
                File file = new File("values.txt");
                if(!file.exists()) file.createNewFile();
                System.out.println(file.getAbsolutePath());
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.append("X = [");
                writer.append(fallingDistances.stream().map(Object::toString).collect(Collectors.joining(", ")));
                writer.append("]");
                writer.newLine();
                writer.append("Y = [");
                writer.append(velocities.stream().map(Object::toString).collect(Collectors.joining(", ")));
                writer.append("]");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

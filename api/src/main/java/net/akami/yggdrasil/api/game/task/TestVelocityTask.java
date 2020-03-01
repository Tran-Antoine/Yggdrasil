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

    private final Player player;
    private final List<Float> fallingDistances;
    private final List<Double> velocities;

    public TestVelocityTask(Player player) {
        this.player = player;
        this.fallingDistances = new ArrayList<>();
        this.velocities = new ArrayList<>();
    }

    @Override
    public void accept(Task task) {

        Float time = player.get(Keys.FALL_DISTANCE).orElse(0f);
        if(time == 0) {
            return;
        }

        double velocity = - player.getVelocity().getY();
        fallingDistances.add(time);
        velocities.add(velocity);
        System.out.println("Values : " + fallingDistances.size());

        if(fallingDistances.size() >= 100) {

            task.cancel();
            writeValues();

        }

    }

    private void writeValues() {

        try {

            File file = new File("values.txt");

            if(!file.exists()) {
                file.createNewFile();
            }

            System.out.println(file.getAbsolutePath());

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            writer.append("X = [")
                    .append(fallingDistances.stream().map(Object::toString).collect(Collectors.joining(", ")))
                    .append("]");

            writer.newLine();

            writer.append("Y = [")
                    .append(velocities.stream().map(Object::toString).collect(Collectors.joining(", ")))
                    .append("]");

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

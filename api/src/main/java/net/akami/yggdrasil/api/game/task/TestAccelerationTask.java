package net.akami.yggdrasil.api.game.task;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.function.Consumer;

public class TestAccelerationTask implements Consumer<Task> {

    private final Player player;

    private Vector3d lastVelocity;
    private int values = 0;

    public TestAccelerationTask(Player player) {
        this.player = player;
    }

    @Override
    public void accept(Task task) {

        updateVelocity();

        if (values >= 50) {
            task.cancel();
        }

    }

    private void updateVelocity() {
        if (lastVelocity == null) {

            lastVelocity = player.getVelocity();
            return;
        }

        ++values;
        Vector3d currentVelocity = player.getVelocity();
        Vector3d dV = currentVelocity.sub(lastVelocity);
        lastVelocity = currentVelocity;
        System.out.println("Calculated instant acceleration : " + dV.length() / 0.1);

    }

}

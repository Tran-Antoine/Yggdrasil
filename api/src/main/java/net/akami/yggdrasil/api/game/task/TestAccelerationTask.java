package net.akami.yggdrasil.api.game.task;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.function.Consumer;

public class TestAccelerationTask implements Consumer<Task> {

    private Player player;
    private Vector3d lastVelocity;
    private int values = 0;

    public TestAccelerationTask(Player player) {
        this.player = player;
    }

    @Override
    public void accept(Task task) {
        if(lastVelocity == null) {
            lastVelocity = player.getVelocity();
        } else {
            values++;
            Vector3d currentVelocity = player.getVelocity();
            Vector3d dV = currentVelocity.sub(lastVelocity);
            lastVelocity = currentVelocity;
            System.out.println("Calculated instant acceleration : " + dV.length() / 0.1);
        }
        if(values >= 50) {
            task.cancel();
        }
    }
}

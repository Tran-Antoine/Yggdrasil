package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

public class UpwardPropulsionStandardTier implements SpellTier {

    private final Vector3d upwardVelocity;
    private static final double DELTA_HEIGHT = 20;

    public UpwardPropulsionStandardTier(double yVelocity) {
        this.upwardVelocity = new Vector3d(0, yVelocity, 0);
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData data) {
        Vector3d playerV = caster.getVelocity();
        if(!caster.isOnGround()) {
            System.out.println("Player must be on the ground to definePreLaunchProperties this spell");
            return;
        }

        double finalHeight = caster.getPosition().getY() + DELTA_HEIGHT;
        caster.setVelocity(playerV.add(upwardVelocity));
        Task.builder()
                .delay(500, TimeUnit.MILLISECONDS)
                .interval(250, TimeUnit.MILLISECONDS)
                .execute(task -> run(task, caster, finalHeight))
                .submit(Sponge.getPluginManager().getPlugin("yggdrasil").get());
    }

    private void run(Task task, Player player, double finalHeight) {
        if(player.getPosition().getY() >= finalHeight) {
            task.cancel();
        }
        if(player.getVelocity().getY() < 0.1) {
            task.cancel();
        }
    }
}

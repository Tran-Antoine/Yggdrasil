package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;

public class UpwardPropulsionStandardTier implements SpellTier {

    private Vector3d upwardVelocity;
    private double boost;

    public UpwardPropulsionStandardTier(float yVelocity, double boost) {
        this.upwardVelocity = new Vector3d(0, yVelocity, 0);
        this.boost = boost;
    }

    @Override
    public void cast(Player caster) {
        Vector3d playerV = caster.getVelocity();
        double regularA = (...);
        double ySpeed = upwardVelocity.getY();
        double modifiedA = regularA * Math.pow(ySpeed + boost, 2) / Math.pow(ySpeed, 2);
        caster.setVelocity(playerV.add(upwardVelocity).add(new Vector3d(0, boost, 0)));
        caster.offer(Keys.ACCELERATION, new Vector3d(0, modifiedA, 0));
    }
}

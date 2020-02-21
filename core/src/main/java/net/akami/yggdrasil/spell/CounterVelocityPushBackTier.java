package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;

public class CounterVelocityPushBackTier implements SpellTier<CounterVelocityLauncher> {

    private ParticleEffect effect;

    public CounterVelocityPushBackTier() {
        this.effect = ParticleEffect.builder()
                .type(ParticleTypes.CLOUD)
                .quantity(50)
                .offset(new Vector3d(1.5,1.5,1.5))
                .build();
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<CounterVelocityLauncher> data) {
        data.addPostAction(this::pushEntities);
    }

    private void pushEntities(Player player, CounterVelocityLauncher launcher) {

        spawnParticles(player);
        double counteredVel = launcher.getCounteredVelocity().length();
        Vector3d playerPos = player.getPosition();

        for(Entity nearEntity : player.getNearbyEntities(15)) {

            Vector3d direction = nearEntity.getLocation().getPosition().sub(playerPos);
            double distance = direction.length();
            if(distance == 0) {
                continue;
            }
            direction = direction
                    .normalize()
                    .div(distance / 2)
                    .mul(6 * counteredVel)
                    .add(0, 0.4, 0);
            nearEntity.setVelocity(nearEntity.getVelocity().add(direction));
        }
    }

    private void spawnParticles(Player player) {
        player.spawnParticles(effect, player.getPosition());
    }
}

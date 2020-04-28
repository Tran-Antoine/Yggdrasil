package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PhoenixArrowBaseTier implements SpellTier<PhoenixArrowLauncher> {

    private World world;
    private ParticleEffect effect;

    public PhoenixArrowBaseTier() {
        this.effect = ParticleEffect.builder()
                .type(ParticleTypes.FLAME)
                .quantity(1)
                .build();
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<PhoenixArrowLauncher> data) {
        data.setProperty("damage", 2.5);
        data.addPostAction(this::addParticles);
    }

    private void addParticles(Player player, PhoenixArrowLauncher launcher) {
        this.world = player.getWorld();
        Task.builder()
                .delay(100, TimeUnit.MILLISECONDS)
                .interval(20, TimeUnit.MILLISECONDS)
                .execute(task -> spawnParticles(task, launcher))
                .submit(YggdrasilMain.getPlugin());
    }

    private void spawnParticles(Task task, PhoenixArrowLauncher launcher) {
        List<Entity> entities = launcher.getAsEntities(world)
                .stream()
                .filter((e) -> e.getVelocity().length() > 0.1)
                .collect(Collectors.toList());
        if(entities.size() == 0) {
            task.cancel();
            return;
        }

        for(Entity arrow : entities) {
            world.spawnParticles(effect, arrow.getLocation().getPosition());
        }
    }
}

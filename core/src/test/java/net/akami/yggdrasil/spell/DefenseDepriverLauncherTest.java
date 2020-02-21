package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.MagicUser;
import net.akami.yggdrasil.api.spell.SpellCaster.SpellType;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;

public class DefenseDepriverLauncherTest implements SpellLauncher<DefenseDepriverLauncherTest> {

    @Override
    public LaunchResult commonLaunch(SpellCreationData<DefenseDepriverLauncherTest> data, Player caster) {
        BiPredicate<DefenseDepriverLauncherTest, MagicUser> condition = (launcher, other) -> areEntitiesNear(caster, other);
        data.setProperty("excluded_type", SpellType.DEFENSIVE);
        data.setProperty("exclusion_condition", condition);
        scheduleRestoringTask(data);
        return LaunchResult.SUCCESS;
    }

    private void scheduleRestoringTask(SpellCreationData<DefenseDepriverLauncherTest> data) {
        Task.builder()
                .delay(10, TimeUnit.SECONDS)
                .execute(data::restoreSpellAccess)
                .submit(Sponge.getPluginManager().getPlugin("yggdrasil").get());
    }

    private boolean areEntitiesNear(Player a, MagicUser target) {
        Optional<Player> optPlayer = Sponge.getServer().getPlayer(target.getUUID());
        if(!optPlayer.isPresent()) {
            return false;
        }
        Player b = optPlayer.get();
        return a.getPosition().distance(b.getLocation().getPosition()) <= 10;
    }
}

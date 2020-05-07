package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.YggdrasilMain;
import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.mana.ManaDrainTask;
import net.akami.yggdrasil.api.spell.MagicUser;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellCreationData.PropertyMap;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.utils.ItemUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

public class LevitationSpellLauncher implements SpellLauncher<LevitationSpellLauncher> {

    private InteractiveItemHandler handler;
    private LevitationCancelItem item;
    private MagicUser magicUser;
    private Task endingSpell;
    private boolean shouldStop = false;

    @Override
    public LaunchResult commonLaunch(SpellCreationData<LevitationSpellLauncher> data, Player caster) {

        if (Math.abs(caster.getVelocity().getY()) > 0.4) {
            return LaunchResult.FAIL;
        }

        PropertyMap map = data.getPropertyMap();
        ManaDrainTask drainTask = map.getProperty("drain_task", ManaDrainTask.class);
        drainTask.setCancelPredicate(this::shouldStop);

        double speed = map.getProperty("speed", Double.class);
        this.handler = data.getHandler();
        this.magicUser = map.getProperty("caster", MagicUser.class);

        Vector3d initV = caster.getVelocity();

        caster.setVelocity(new Vector3d(initV.getX(), speed, initV.getZ()));
        caster.offer(Keys.HAS_GRAVITY, false);
        endingSpell = Task.builder()
                .delay(10, TimeUnit.SECONDS)
                .execute(() -> cancel(caster))
                .submit(YggdrasilMain.getPlugin());

        addItem(caster);

        return LaunchResult.SUCCESS;
    }

    @Override
    public void onRanOutOfMana() {
        cancel(Sponge.getServer().getPlayer(magicUser.getUUID()).get());
    }

    private void addItem(Player caster) {
        this.item = new LevitationCancelItem(this::cancel);
        ItemUtils.fitItem(caster, handler, item);
    }

    private void cancel(Player player) {
        shouldStop = true;
        endingSpell.cancel();
        player.offer(Keys.HAS_GRAVITY, true);
        handler.completeRemove(item, player);
    }

    private boolean shouldStop() {
        return shouldStop;
    }
}

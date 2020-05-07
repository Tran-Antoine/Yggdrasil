package net.akami.yggdrasil.api.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.item.LaunchableSpellItem;
import net.akami.yggdrasil.api.mana.ManaDrainTask;
import net.akami.yggdrasil.api.utils.ItemUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public interface Spell<T extends SpellLauncher<T>> {

    List<SpellTier<T>> getTiers();
    T getLauncher();

    default SpellTier<T> getTier(int tier) {
        return getTiers().get(tier);
    }

    default void cast(MagicUser user, Vector3d location, int tier, ManaDrainTask manaDrain) {

        Player wizard = Sponge.getServer().getPlayer(user.getUUID()).get();

        SpellCreationData<T> data = new SpellCreationData<>();
        data.setProperty("location", location);
        data.setProperty("caster", user);
        data.setProperty("drain_task", manaDrain);

        for(int i = 0; i < tier; i++) {
            SpellTier<T> spellTier = getTier(i);
            spellTier.definePreLaunchProperties(wizard, data);
        }

        if(data.isStorable()) {
            LaunchableSpellItem item = new LaunchableSpellItem(data.getItem(), data, this::getLauncher);
            InteractiveItemHandler handler = data.getHandler();
            ItemUtils.fitItem(wizard, handler, item);
        } else {
            T launcher = getLauncher();
            manaDrain.setOnCancelled(launcher::onRanOutOfMana);
            launcher.launch(data, wizard);
        }
    }
}

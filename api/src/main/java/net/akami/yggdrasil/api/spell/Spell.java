package net.akami.yggdrasil.api.spell;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.item.LaunchableSpellItem;
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

    default void cast(MagicUser user, Vector3d location, int tier) {

        Player wizard = Sponge.getServer().getPlayer(user.getUUID()).get();

        SpellCreationData<T> data = new SpellCreationData<>();
        data.setProperty("location", location);
        data.setProperty("caster", user);

        for(int i = 0; i < tier; i++) {
            SpellTier<T> spellTier = getTier(i);
            spellTier.definePreLaunchProperties(wizard, data);
        }

        T launcher = this.getLauncher();

        if(data.isStorable()) {
            LaunchableSpellItem item = new LaunchableSpellItem(data.getItem(), data, () -> launcher);
            InteractiveItemHandler handler = data.getHandler();
            ItemUtils.fitItem(wizard, handler, item);
        } else {
            launcher.launch(data, wizard);
        }
    }
}

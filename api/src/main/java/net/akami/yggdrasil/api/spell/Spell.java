package net.akami.yggdrasil.api.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.item.LaunchableSpellItem;
import net.akami.yggdrasil.api.utils.InteractiveItemUtils;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public interface Spell {

    List<SpellTier> getTiers();
    SpellLauncher getLauncher();

    default SpellTier getTier(int tier) {
        return getTiers().get(tier-1);
    }

    default void cast(Player player, int tier) {
        SpellCreationData data = new SpellCreationData();
        for(int i = 0; i < tier; i++) {
            SpellTier spellTier = getTier(i);
            spellTier.definePreLaunchProperties(player, data);
        }

        SpellLauncher launcher = this.getLauncher();

        if(data.isStorable()) {
            LaunchableSpellItem item = new LaunchableSpellItem(data.getItem(), data, launcher);
            InteractiveItemHandler handler = data.getHandler();
            InteractiveItemUtils.fitItem(player, handler, item);
        } else {
            launcher.launch(data, player);
        }

    }
}

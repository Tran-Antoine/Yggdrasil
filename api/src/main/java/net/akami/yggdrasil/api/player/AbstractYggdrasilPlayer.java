package net.akami.yggdrasil.api.player;

import net.akami.yggdrasil.api.item.InteractiveItemUser;
import net.akami.yggdrasil.api.life.LivingUser;
import net.akami.yggdrasil.api.spell.MagicUser;

public interface AbstractYggdrasilPlayer extends InteractiveItemUser, LivingUser, MagicUser {

    void addDefaultItems();

    void addDefaultSpells();

}

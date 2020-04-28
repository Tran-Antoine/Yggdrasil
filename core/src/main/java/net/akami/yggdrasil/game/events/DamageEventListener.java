package net.akami.yggdrasil.game.events;

import net.akami.yggdrasil.api.life.LivingUser;
import net.akami.yggdrasil.api.input.UUIDHolder;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;

import java.util.List;
import java.util.UUID;

public class DamageEventListener {

    private List<? extends LivingUser> users;

    public DamageEventListener(List<? extends LivingUser> users) {
        this.users = users;
    }

    @Listener
    public void onDamageTaken(DamageEntityEvent event) {
        Entity entity = event.getTargetEntity();
        if(entity.getType() != EntityTypes.PLAYER) {
            return;
        }
        UUID targetID = entity.getUniqueId();
        LivingUser user = UUIDHolder.getByUUID(users, targetID);
        if(user == null) {
            return;
        }
        double finalDamage = event.getFinalDamage();
        user.getLife().damage(Math.round(finalDamage));
    }
}

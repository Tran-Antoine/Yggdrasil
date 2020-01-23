package net.akami.yggdrasil.player;

import java.util.List;
import java.util.UUID;

public interface InteractiveItemUser extends InteractiveItemHandler, UUIDHolder {

    static InteractiveItemUser getByUUID(List<? extends InteractiveItemUser> users, UUID id) {
        for(InteractiveItemUser user : users) {
            if(user.getUUID().equals(id))
                return user;
        }
        return null;
    }
}

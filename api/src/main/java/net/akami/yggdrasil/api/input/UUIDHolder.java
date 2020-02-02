package net.akami.yggdrasil.api.input;

import java.util.List;
import java.util.UUID;

public interface UUIDHolder {

    static <T extends UUIDHolder> T getByUUID(List<T> users, UUID id) {
        for(T user : users) {
            if(user.getUUID().equals(id))
                return user;
        }
        return null;
    }

    UUID getUUID();
}

package net.akami.yggdrasil.api.data;

import org.spongepowered.api.data.DataRegistration;

public class YggdrasilData {

    public static final DataRegistration<BoolData, BoolData.Immutable> PERSISTENT = DataRegistration.builder()
            .name("Is Persistent")
            .id("persistent")
            .dataClass(BoolData.class)
            .dataImplementation(PersistentData.class)
            .immutableClass(BoolData.Immutable.class)
            .immutableImplementation(PersistentData.Immutable.class)
            .builder(new PersistentData.Builder())
            .build();
}

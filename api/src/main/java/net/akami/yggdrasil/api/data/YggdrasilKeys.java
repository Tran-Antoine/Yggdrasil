package net.akami.yggdrasil.api.data;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

public class YggdrasilKeys {

    public static final Key<Value<Boolean>> PERSISTENT = Key.builder()
            .type(TypeTokens.BOOLEAN_VALUE_TOKEN)
            .id("persistent")
            .name("Is Persistent")
            .query(DataQuery.of('.', "persistent.enabled"))
            .build();
}

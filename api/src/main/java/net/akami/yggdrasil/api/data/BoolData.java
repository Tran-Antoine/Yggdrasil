package net.akami.yggdrasil.api.data;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

public interface BoolData extends DataManipulator<BoolData, BoolData.Immutable> {

    Value<Boolean> enabled();

    interface Immutable extends ImmutableDataManipulator<Immutable, BoolData> {
        ImmutableValue<Boolean> enabled();
    }

    interface Builder extends DataManipulatorBuilder<BoolData, BoolData.Immutable> {

    }

}

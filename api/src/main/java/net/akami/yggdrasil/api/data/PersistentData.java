package net.akami.yggdrasil.api.data;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableBooleanData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractBooleanData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataContentUpdater;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

// Example of specific singular data
// Example of API-implementation split
// Example of data updating
public class PersistentData extends AbstractBooleanData<BoolData, BoolData.Immutable> implements BoolData {
    PersistentData(boolean enabled) {
        super(enabled, YggdrasilKeys.PERSISTENT, false);
    }

    @Override
    public Value<Boolean> enabled() {
        return getValueGetter();
    }

    @Override
    public Optional<BoolData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<PersistentData> data_ = dataHolder.get(PersistentData.class);
        if(data_.isPresent()) {
            PersistentData data = data_.get();
            PersistentData finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<BoolData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<BoolData> from(DataView view) {
        if(view.contains(YggdrasilKeys.PERSISTENT.getQuery())) {
            setValue(view.getBoolean(YggdrasilKeys.PERSISTENT.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public PersistentData copy() {
        return new PersistentData(getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(getValue());
    }

    @Override
    public int getContentVersion() {
        return 2;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(YggdrasilKeys.PERSISTENT.getQuery(), getValue());
    }

    public static class Immutable extends AbstractImmutableBooleanData<BoolData.Immutable, BoolData> implements BoolData.Immutable {
        Immutable(boolean enabled) {
            super(enabled, YggdrasilKeys.PERSISTENT, false);
        }

        @Override
        public PersistentData asMutable() {
            return new PersistentData(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(YggdrasilKeys.PERSISTENT.getQuery(), getValue());
        }

        @Override
        public ImmutableValue<Boolean> enabled() {
            return getValueGetter();
        }
    }

    public static class Builder extends AbstractDataBuilder<BoolData> implements BoolData.Builder {
        Builder() {
            super(BoolData.class, 2);
        }

        @Override
        public PersistentData create() {
            return new PersistentData(false);
        }

        @Override
        public Optional<BoolData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<BoolData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }
    }

    public static class BoolEnabled1To2Updater implements DataContentUpdater {

        @Override
        public int getInputVersion() {
            return 1;
        }

        @Override
        public int getOutputVersion() {
            return 2;
        }

        @Override
        public DataView update(DataView content) {
            return content.set(DataQuery.of('.', "persistent.enabled"), content.get(DataQuery.of('.', "persistent.isEnabled"))).remove(DataQuery.of('.', "persistent.isEnabled"));
        }
    }
}

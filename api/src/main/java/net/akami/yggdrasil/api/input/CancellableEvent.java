package net.akami.yggdrasil.api.input;

import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.util.annotation.eventgen.AbsoluteSortPosition;
import org.spongepowered.api.util.annotation.eventgen.PropertySettings;

public class CancellableEvent<T extends Cancellable & Event> implements Event, Cancellable {

    private T event;

    private CancellableEvent(T event) {
        this.event = event;
    }

    public static <T extends Cancellable & Event> CancellableEvent<T> of(T event) {
        return new CancellableEvent<>(event);
    }

    public T getEvent() {
        return event;
    }

    @Override
    @PropertySettings(requiredParameter = false)
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCancelled(cancel);
    }

    @Override
    @AbsoluteSortPosition(0)
    public Cause getCause() {
        return event.getCause();
    }

    @Override
    @PropertySettings(requiredParameter = false, generateMethods = false)
    public Object getSource() {
        return event.getSource();
    }

    @Override
    @PropertySettings(requiredParameter = false, generateMethods = false)
    public EventContext getContext() {
        return event.getContext();
    }
}

package net.lortservers.iris.events;

public interface IrisCancellableEvent {
    boolean isCancelled();

    void setCancelled(boolean cancelled);
}

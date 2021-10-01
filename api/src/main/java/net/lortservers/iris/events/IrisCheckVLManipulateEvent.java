package net.lortservers.iris.events;

import net.lortservers.iris.checks.Check;

public interface IrisCheckVLManipulateEvent extends IrisPlayerEvent {
    Check getCheck();
    int getOldVL();
    int getNewVL();
    boolean isScheduled();
    ManipulateType getAction();

    enum ManipulateType {
        INCREASE, DECREASE, RESET
    }
}

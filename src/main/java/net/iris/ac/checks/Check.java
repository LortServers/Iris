package net.iris.ac.checks;

import net.iris.ac.utils.CheckAlphabet;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;

public abstract class Check {
    private int vl = 0;

    public Check(int decreaseBy, long decreaseTime) {
        this(decreaseBy, decreaseTime, TaskerTime.TICKS);
    }

    public Check(int decreaseBy, long decreaseTime, TaskerTime decreaseTimeType) {
        Tasker.build(() -> Check.this.decreaseVL(decreaseBy)).repeat(decreaseTime, decreaseTimeType).start();
    }

    public abstract CheckAlphabet getType();
    public abstract String getName();

    public void increaseVL(int vl) {
        this.vl = this.vl + vl;
    }

    public void decreaseVL(int vl) {
        if (this.vl >= vl) {
            this.vl = this.vl - vl;
        } else {
            this.vl = 0;
        }
    }
}

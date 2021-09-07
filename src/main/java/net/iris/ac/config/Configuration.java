package net.iris.ac.config;

import java.util.Objects;

public class Configuration {
    private int checkDecreaseFrequency = 60;
    private int checkDecreaseAmount = 10;
    private int checkCooldownPeriod = 1;

    protected Configuration() {}

    @Override
    public String toString() {
        return "Configuration{" +
                "checkDecreaseFrequency=" + checkDecreaseFrequency +
                ", checkDecreaseAmount=" + checkDecreaseAmount +
                ", checkCooldownPeriod=" + checkCooldownPeriod +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return checkDecreaseFrequency == that.checkDecreaseFrequency && checkDecreaseAmount == that.checkDecreaseAmount && checkCooldownPeriod == that.checkCooldownPeriod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkDecreaseFrequency, checkDecreaseAmount);
    }

    public int getCheckDecreaseFrequency() {
        return checkDecreaseFrequency;
    }

    public void setCheckDecreaseFrequency(int checkDecreaseFrequency) {
        this.checkDecreaseFrequency = checkDecreaseFrequency;
    }

    public int getCheckDecreaseAmount() {
        return checkDecreaseAmount;
    }

    public void setCheckDecreaseAmount(int checkDecreaseAmount) {
        this.checkDecreaseAmount = checkDecreaseAmount;
    }

    public int getCheckCooldownPeriod() {
        return checkCooldownPeriod;
    }

    public void setCheckCooldownPeriod(int checkCooldownPeriod) {
        this.checkCooldownPeriod = checkCooldownPeriod;
    }
}

package net.iris.ac.utils;

public class CooldownMapping {
    private final int cooldown;
    private long lastUsage = System.currentTimeMillis();

    /**
     * @param cooldown cooldown in seconds
     */
    public CooldownMapping(int cooldown) {
        this.cooldown = cooldown / 1000;
    }

    public boolean isOnCooldown() {
        return (System.currentTimeMillis() - lastUsage) < cooldown;
    }

    public void putCooldown() {
        lastUsage = System.currentTimeMillis();
    }
}

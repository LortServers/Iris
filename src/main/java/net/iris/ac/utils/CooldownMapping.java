package net.iris.ac.utils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CooldownMapping {
    private final int cooldown;
    private long lastUsage = System.currentTimeMillis();

    public boolean isOnCooldown() {
        return (System.currentTimeMillis() - lastUsage) < cooldown;
    }

    public void putCooldown() {
        lastUsage = System.currentTimeMillis();
    }
}

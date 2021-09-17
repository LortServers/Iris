package net.lortservers.iris.utils;

import lombok.RequiredArgsConstructor;

/**
 * <p>Class responsible for holding a cooldown value.</p>
 */
@RequiredArgsConstructor
public class CooldownMapping {
    /**
     * <p>Cooldown time.</p>
     */
    private final int cooldown;
    /**
     * <p>Last usage time.</p>
     */
    private long lastUsage = System.currentTimeMillis();

    /**
     * <p>Gets if the cooldown is active.</p>
     *
     * @return is the cooldown active?
     */
    public boolean isOnCooldown() {
        return (System.currentTimeMillis() - lastUsage) < cooldown;
    }

    /**
     * <p>Activates the cooldown.</p>
     */
    public void putCooldown() {
        lastUsage = System.currentTimeMillis();
    }
}

package net.lortservers.iris.config;

import lombok.*;

import java.io.Serializable;

/**
 * <p>A class holding the plugin configuration.</p>
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Configuration implements Serializable {
    private int checkDecreaseFrequency = 60;
    private int checkDecreaseAmount = 10;
    private int checkCooldownPeriod = 100;
    private double aimbotHFirstDistance = 3.75;
    private double aimbotHLastDistance = 3.5;
    private int aimbotHCount = 21;
    private int aimbotHVLThreshold = 2;
    private double aimbotIDistance = 3.5;
    private int aimbotIVLThreshold = 2;
    private double aimbotADistance = 0.5;
    private int aimbotAMaxCountDifference = 1;
    private int aimbotAVLThreshold = 4;
    private int aimbotBVLThreshold = 5;
    private int aimbotEMinSimilarYaw = 11;
    private int aimbotEMinSimilarPitch = 5;
    private int aimbotFVLThreshold = 3;
    private double aimbotGDistance = 0.5;
    private int aimbotGVLThreshold = 2;
    private int interactFrequencyAMaxCPS = 22;
    private int interactFrequencyAVLThreshold = 2;
    private boolean discordWebhook = false;
    private String webhookUrl = "";
}

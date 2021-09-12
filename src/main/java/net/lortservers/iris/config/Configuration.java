package net.lortservers.iris.config;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Configuration {
    private int checkDecreaseFrequency = 60;
    private int checkDecreaseAmount = 10;
    private int checkCooldownPeriod = 100;
    private double aimbotHFirstDistance = 3.75;
    private double aimbotHLastDistance = 3.5;
    private int aimbotHCount = 21;
    private int aimbotHVLThreshold = 2;
    private double aimbotIDistance = 3.5;
    private int aimbotIVLThreshold = 2;
}

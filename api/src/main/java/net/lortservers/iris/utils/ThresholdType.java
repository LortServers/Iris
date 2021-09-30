package net.lortservers.iris.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ThresholdType {
    MESSAGE("message");

    private final String key;
}

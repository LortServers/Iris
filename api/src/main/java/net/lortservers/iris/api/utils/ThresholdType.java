package net.lortservers.iris.api.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ThresholdType {
    MESSAGE("message");

    private final String key;
}

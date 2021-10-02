package net.lortservers.iris.utils;

import java.util.*;

public final class MiscUtils {
    public static <K, V> Map<K, V> collectionMerge(Map<K, V> map1, Map<K, V> map2) {
        final Map<K, V> clone = new HashMap<>(map1);
        clone.putAll(map2);
        return clone;
    }
}

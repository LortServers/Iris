package net.lortservers.iris.utils;

import java.util.List;

public interface MaterialGroup<T> {
    List<T> materials();
    boolean contains(T mat);
    MaterialGroup<T> filter(String... parts);
}

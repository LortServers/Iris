package net.lortservers.iris.api.utils;

import java.util.List;

public interface MaterialGroup<T> {
    List<T> materials();
    boolean contains(T mat);
    MaterialGroup<T> filter(String... parts);
    MaterialGroup<T> copy();
    Class<T> getMaterialType();
}

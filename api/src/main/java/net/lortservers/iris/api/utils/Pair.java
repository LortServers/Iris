package net.lortservers.iris.api.utils;

public interface Pair<A, B> {
    A first();
    B second();
    Pair<A, B> modifyFirst(A first);
    Pair<A, B> modifySecond(B second);
    Pair<A, B> modifyFull(A first, B second);
    Pair<A, B> copy();
}

package net.lortservers.iris.api.utils;

public interface IntegerPair extends Pair<Integer, Integer> {
    IntegerPair incrementFirst(int val);
    IntegerPair incrementSecond(int val);
    IntegerPair incrementBoth(int val);
    IntegerPair incrementFull(int val1, int val2);
    IntegerPair decrementFirst(int val);
    IntegerPair decrementSecond(int val);
    IntegerPair decrementBoth(int val);
    IntegerPair decrementFull(int val1, int val2);
}

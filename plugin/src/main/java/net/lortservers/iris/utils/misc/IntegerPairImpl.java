package net.lortservers.iris.utils.misc;

import net.lortservers.iris.utils.IntegerPair;

public class IntegerPairImpl implements IntegerPair {
    private Integer first;
    private Integer second;

    private IntegerPairImpl(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public static IntegerPair of(int first, int second) {
        return new IntegerPairImpl(first, second);
    }

    @Override
    public Integer first() {
        return first;
    }

    @Override
    public Integer second() {
        return second;
    }

    @Override
    public IntegerPair modifyFirst(Integer first) {
        this.first = first;
        return this;
    }

    @Override
    public IntegerPair modifySecond(Integer second) {
        this.second = second;
        return this;
    }

    @Override
    public IntegerPair modifyFull(Integer first, Integer second) {
        this.first = first;
        this.second = second;
        return this;
    }

    @Override
    public IntegerPair copy() {
        return new IntegerPairImpl(first, second);
    }

    @Override
    public IntegerPair incrementFirst(int val) {
        first += val;
        return this;
    }

    @Override
    public IntegerPair incrementSecond(int val) {
        second += val;
        return this;
    }

    @Override
    public IntegerPair incrementBoth(int val) {
        first += val;
        second += val;
        return this;
    }

    @Override
    public IntegerPair incrementFull(int val1, int val2) {
        first += val1;
        second += val2;
        return this;
    }

    @Override
    public IntegerPair decrementFirst(int val) {
        first -= val;
        return this;
    }

    @Override
    public IntegerPair decrementSecond(int val) {
        second -= val;
        return this;
    }

    @Override
    public IntegerPair decrementBoth(int val) {
        first -= val;
        second -= val;
        return this;
    }

    @Override
    public IntegerPair decrementFull(int val1, int val2) {
        first -= val1;
        second -= val2;
        return this;
    }
}

package net.lortservers.iris.utils.misc;

import net.lortservers.iris.api.utils.Pair;

public class PairImpl<A, B> implements Pair<A, B> {
    private A first;
    private B second;

    private PairImpl(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public static <C, D> Pair<C, D> of(C first, D second) {
        return new PairImpl<>(first, second);
    }

    @Override
    public A first() {
        return first;
    }

    @Override
    public B second() {
        return second;
    }

    @Override
    public Pair<A, B> modifyFirst(A first) {
        this.first = first;
        return this;
    }

    @Override
    public Pair<A, B> modifySecond(B second) {
        this.second = second;
        return this;
    }

    @Override
    public Pair<A, B> modifyFull(A first, B second) {
        this.first = first;
        this.second = second;
        return this;
    }

    @Override
    public Pair<A, B> copy() {
        return new PairImpl<>(first, second);
    }
}

package com.mudalov.safe.util;

/**
 * User: mudalov
 * Date: 24/01/15
 * Time: 22:59
 */
public class Pair<F,S> {

    private final F first;

    private final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}

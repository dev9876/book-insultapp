package org.openshift;

public final class MoreFunctions {

    @FunctionalInterface
    public interface TriFunction<S, T> {
        T apply(S a, S b, S c);
    }

}

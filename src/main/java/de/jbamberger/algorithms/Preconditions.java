package de.jbamberger.algorithms;

public class Preconditions {

    public static <T> T assertNonNull(T value) {
        if (value == null) {
            throw new AssertionError("Value cannot be null.");
        }
        return value;
    }

    public static <T> T assertNonNull(T value, String msg) {
        if (value == null) {
            throw new AssertionError(msg);
        }
        return value;
    }

    public static int assertPositive(int value) {
        if (value <= 0) {
            throw new AssertionError("Value must be positive.");
        }
        return value;
    }

    public static int assertNonNegative(int value) {
        if (value < 0) {
            throw new AssertionError("Value must be non-negative.");
        }
        return value;
    }
}

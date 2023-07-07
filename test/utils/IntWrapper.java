package utils;

import java.util.Objects;

/**
 * A wrapper for integers; meant for testing custom objects in generic type parameters.
 */
public class IntWrapper {
    public final int val;

    public IntWrapper(int val) {
        this.val = val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IntWrapper that = (IntWrapper) o;
        return val == that.val;
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }

    @Override
    public String toString() {
        return "IntWrapper{" +
            "id=" + val +
            '}';
    }
}

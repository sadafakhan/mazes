package graphs;

import java.util.Objects;

/**
 * A weighted directed edge that stores some extra custom data.
 *
 * @param <V> The vertex type.
 * @param <T> The type of the extra data for each edge.
 */
public final class EdgeWithData<V, T> extends BaseEdge<V, EdgeWithData<V, T>> {
    protected final T data;

    public EdgeWithData(V from, V to, double weight, T data) {
        super(from, to, weight);
        this.data = data;
    }

    public T data() {
        return data;
    }

    /**
     * Returns a new edge with the same data and weight, but in the opposite direction.
     */
    @Override
    public EdgeWithData<V, T> reversed() {
        return new EdgeWithData<>(this.to, this.from, this.weight, this.data);
    }

    @Override
    public String toString() {
        return "EdgeWithData{" +
            "data=" + data +
            ", from=" + from +
            ", to=" + to +
            ", weight=" + weight +
            "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        EdgeWithData<?, ?> that = (EdgeWithData<?, ?>) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), data);
    }
}

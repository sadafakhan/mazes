package graphs.shortestpaths;

import graphs.BaseEdge;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link ShortestPathFinder} return an object implementing this interface.
 * The exact implementation returned will depend on the result of the shortest path computation.
 */
public interface ShortestPath<V, E extends BaseEdge<V, E>> {
    /** Returns true iff a path was found from the start vertex to the end vertex. */
    boolean exists();

    /**
     * Returns the list of edges in the shortest path (in order from start to end).
     * @throws UnsupportedOperationException if no path exists from start to end
     */
    List<E> edges();

    /**
     * Returns the list of vertices in the shortest path (in order from start to end).
     * @throws UnsupportedOperationException if no path exists from start to end
     */
    List<V> vertices();

    /**
     * Returns the total weight of the edges in the shortest path.
     * @throws UnsupportedOperationException if no path exists from start to end
     */
    default double totalWeight() {
        return edges().stream().mapToDouble(E::weight).sum();
    }

    /** A result used when a shortest path is found from start to end. */
    class Success<V, E extends BaseEdge<V, E>> implements ShortestPath<V, E> {
        private final List<E> edges;

        /**
         * @param edges The list of edges in this shortest path (in order from start to end).
         * @throws IllegalArgumentException if edges is empty or null
         */
        public Success(List<E> edges) {
            if (edges == null || edges.isEmpty()) {
                throw new IllegalArgumentException("Input edges must not be null or empty.");
            }
            this.edges = edges;
        }

        @Override
        public boolean exists() {
            return true;
        }

        @Override
        public List<E> edges() {
            return this.edges;
        }

        @Override
        public List<V> vertices() {
            return Stream.concat(
                Stream.of(this.edges.get(0).from()),
                this.edges.stream().map(E::to)
            ).collect(Collectors.toList());
        }
    }

    /** A result used when a shortest path consists of a single vertex (same start and end). */
    class SingleVertex<V, E extends BaseEdge<V, E>> implements ShortestPath<V, E> {
        private final List<V> vertex;

        /**
         * @param vertex the only vertex in the path
         */
        public SingleVertex(V vertex) {
            this.vertex = List.of(vertex);
        }

        @Override
        public boolean exists() {
            return true;
        }

        @Override
        public List<E> edges() {
            return List.of();
        }

        @Override
        public List<V> vertices() {
            return this.vertex;
        }
    }

    /** A result used when no path exists from start to end. */
    class Failure<V, E extends BaseEdge<V, E>> implements ShortestPath<V, E> {
        public Failure() {}

        @Override
        public boolean exists() {
            return false;
        }

        @Override
        public List<E> edges() {
            throw new UnsupportedOperationException("No path found.");
        }

        @Override
        public List<V> vertices() {
            throw new UnsupportedOperationException("No path found.");
        }
    }
}

package graphs.minspantrees;

import graphs.BaseEdge;

import java.util.Collection;
import java.util.Set;

/**
 * {@link MinimumSpanningTreeFinder} return an object implementing this interface.
 * The exact implementation returned will depend on the result of the minimum spanning tree
 * computation.
 */
public interface MinimumSpanningTree<V, E extends BaseEdge<V, E>> {
    /** Returns true iff the graph has an MST, i.e., is connected. */
    boolean exists();

    /**
     * Returns the edges in the MST.
     * @throws UnsupportedOperationException if no MST exists
     */
    Collection<E> edges();

    /**
     * Returns the total weight of the edges in the MST.
     * @throws UnsupportedOperationException if no MST exists
     */
    default double totalWeight() {
        return edges().stream().mapToDouble(E::weight).sum();
    }

    /** A result used when a minimum spanning tree is found. */
    class Success<V, E extends BaseEdge<V, E>> implements MinimumSpanningTree<V, E> {
        private final Collection<E> edges;

        /** Creates a successful result representing a minimum spanning tree with no edges. */
        Success() {
            this.edges = Set.of();
        }

        /**
         * @param edges a Collection of all edges in the minimum spanning tree.
         */
        Success(Collection<E> edges) {
            this.edges = edges;
        }

        @Override
        public boolean exists() {
            return true;
        }

        @Override
        public Collection<E> edges() {
            return edges;
        }
    }

    /** A result used when no minimum spanning tree exists. */
    class Failure<V, E extends BaseEdge<V, E>> implements MinimumSpanningTree<V, E> {
        Failure() {}

        @Override
        public boolean exists() {
            return false;
        }

        @Override
        public Collection<E> edges() {
            throw new UnsupportedOperationException("Graph is not connected.");
        }
    }
}

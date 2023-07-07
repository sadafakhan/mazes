package graphs;

import java.util.Collection;

/**
 * The most basic possible graph interface; represents a directed graph.
 *
 * @param <V> The vertex type.
 * @param <E> The edge type. Must be a subtype of {@link Edge}.
 */
public interface Graph<V, E extends BaseEdge<V, E>> {
    /**
     * Returns an unmodifiable collection of the outgoing edges from the given vertex.
     *
     * If given vertex is not in the graph, returns an empty collection.
     */
    Collection<E> outgoingEdgesFrom(V vertex);
}

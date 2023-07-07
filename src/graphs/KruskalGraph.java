package graphs;

import java.util.Collection;

/**
 * An undirected graph that supports Kruskal's algorithm.
 *
 * Note: although the graph is undirected, its edges are directed.
 * In particular, this means that an edge AB from {@code outgoingEdgesFrom(A)}
 * will not be equal to the edge BA from {@code outgoingEdgesFrom(B)}.
 *
 * @param <V> The vertex type.
 * @param <E> The edge type. Must be a subtype of {@link Edge}.
 */
public interface KruskalGraph<V, E extends BaseEdge<V, E>> extends Graph<V, E> {
    /** Returns an unmodifiable collection of all vertices in the graph. */
    Collection<V> allVertices();
    /** Returns an unmodifiable collection of all edges in the graph. */
    Collection<E> allEdges();
}

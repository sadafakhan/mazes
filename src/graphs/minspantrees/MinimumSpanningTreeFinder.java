package graphs.minspantrees;

import graphs.BaseEdge;
import graphs.Edge;
import graphs.Graph;

/**
 * Interface for finding a minimum spanning tree of a graph.
 *
 * @param <G> The graph type. Must be a subtype of {@link Graph}.
 * @param <V> The vertex type.
 * @param <E> The edge type. Must be a subtype of {@link Edge}.
 */
public interface MinimumSpanningTreeFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>> {
    /**
     * Computes a minimum spanning tree for given graph, and returns a MinimumSpanningTree object
     * that can be queried for its edges or total weight.
     *
     * Alternatively, if no MST exists in the graph, the returned MinimumSpanningTree will have
     * {@link MinimumSpanningTree#exists()} false.
     */
    MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph);
}

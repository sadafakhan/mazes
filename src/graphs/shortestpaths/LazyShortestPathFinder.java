package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Very basic syntactically-correct but semantically-incorrect shortest paths finder.
 *
 * Checks the start vertex for an edge directly to the end vertex, and gives up if not found.
 */

public class LazyShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        HashMap<V, E> spt = new HashMap<>();
        if (Objects.equals(start, end)) {
            return spt;
        }

        for (E edge : graph.outgoingEdgesFrom(start)) {
            spt.put(edge.to(), edge);
        }

        return spt;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        E edge = spt.get(end);
        if (edge == null) {
            return new ShortestPath.Failure<>();
        }

        return new ShortestPath.Success<>(List.of(edge));
    }
}

package graphs;

import edu.washington.cse373.BaseTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BaseGraphTests extends BaseTest {

    /*
    Since we need to create so many objects in each test, using these helper methods significantly
    reduces visual clutter.
    */
    @SafeVarargs
    protected final <V, E extends BaseEdge<V, E>> AdjacencyListUndirectedGraph<V, E> graph(E... edges) {
        return graph(List.of(edges));
    }

    protected final <V, E extends BaseEdge<V, E>> AdjacencyListUndirectedGraph<V, E> graph(Collection<E> edges) {
        return new AdjacencyListUndirectedGraph<>(edges);
    }

    @SafeVarargs
    protected final <V, E extends BaseEdge<V, E>> AdjacencyListDirectedGraph<V, E> directedGraph(E... edges) {
        return directedGraph(List.of(edges));
    }

    protected final <V, E extends BaseEdge<V, E>> AdjacencyListDirectedGraph<V, E> directedGraph(Collection<E> edges) {
        return new AdjacencyListDirectedGraph<>(edges);
    }

    protected <V> Edge<V> edge(V from, V to) {
        return edge(from, to, 1);
    }

    protected <V> Edge<V> edge(V from, V to, double weight) {
        return new Edge<>(from, to, weight);
    }

    @SafeVarargs
    protected final <V, E extends BaseEdge<V, E>> Map<V, E> spt(E... edges) {
        return Arrays.stream(edges).collect(Collectors.toMap(E::to, Function.identity()));
    }

}

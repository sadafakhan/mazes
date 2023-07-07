package graphs.minspantrees;

import disjointsets.DisjointSets;
import graphs.AdjacencyListUndirectedGraph;
import graphs.BaseEdge;
import graphs.BaseGraphTests;
import graphs.Edge;
import graphs.KruskalGraph;
import graphs.ZeroEdgeGraph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KruskalMinimumSpanningTreeFinderTests extends BaseGraphTests {

    protected <G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    MinimumSpanningTreeFinderAssert.MinimumSpanningTreeAssert<V, E> assertThatMSTOf(G graph) {
        MinimumSpanningTreeFinder<G, V, E> mstFinder = new KruskalMinimumSpanningTreeFinder<>() {
            /** Override method to simulate behavior on grader. */
            @Override
            protected DisjointSets<V> createDisjointSets() {
                return super.createDisjointSets();
            }
        };

        return new MinimumSpanningTreeFinderAssert.MinimumSpanningTreeAssert<>(
            mstFinder.findMinimumSpanningTree(graph), graph);
    }

    @Test
    void find_onTreeGraph_returnsAllEdges() {
        AdjacencyListUndirectedGraph<String, Edge<String>> graph = graph(
            edge("a", "b", 2),
            edge("a", "c", 3),
            edge("c", "d", 1)
        );
        assertThatMSTOf(graph).hasEdges(graph.allEdges());
    }

    @Test
    void find_onGraphWithCycle_returnsAllEdges() {
        AdjacencyListUndirectedGraph<String, Edge<String>> graph = graph(
            edge("a", "b", 1),
            edge("b", "e", 6),
            edge("e", "c", 5),
            edge("c", "d", 4),
            edge("a", "c", 3),
            edge("a", "d", 2)
        );
        assertThatMSTOf(graph).hasEdges(getEdges(graph, 0, 2, 4, 5));
    }

    @Test
    void find_onDisconnectedGraph_returnsDoesNotExist() {
        AdjacencyListUndirectedGraph<String, Edge<String>> graph = graph(
            edge("a", "b", 2),

            edge("d", "c", 3),
            edge("d", "e", 1),
            edge("e", "c", 5)
        );
        assertThatMSTOf(graph).doesNotExist();
    }

    @Test
    void find_withSelfLoopEdge_returnsCorrectEdges() {
        AdjacencyListUndirectedGraph<String, Edge<String>> graph = graph(
            edge("a", "a", 0),
            edge("b", "c", 1),
            edge("a", "c", 2)
        );
        assertThatMSTOf(graph).hasEdges(getEdges(graph, 1, 2));
    }

    @Test
    void find_on0Edge0VertexGraph_returnsNoEdges() {
        ZeroEdgeGraph graph = new ZeroEdgeGraph(0);
        assertThatMSTOf(graph).hasEdges();
    }

    protected <V> List<Edge<V>> getEdges(AdjacencyListUndirectedGraph<V, Edge<V>> graph,
                                         int... edgeIndices) {
        return getEdges(graph, Arrays.stream(edgeIndices));
    }

    protected <V> List<Edge<V>> getEdges(AdjacencyListUndirectedGraph<V, Edge<V>> graph,
                                         IntStream edgeIndices) {
        List<Edge<V>> edges = graph.allEdges();
        return edgeIndices.mapToObj(edges::get).collect(Collectors.toList());
    }
}

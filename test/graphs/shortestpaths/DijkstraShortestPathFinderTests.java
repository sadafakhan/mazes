package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.BaseGraphTests;
import graphs.Edge;
import graphs.Graph;
import graphs.InfiniteGraph;
import graphs.InfiniteIntWrapperGraph;
import graphs.ZeroEdgeGraph;
import org.assertj.core.api.MapAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import priorityqueues.ExtrinsicMinPQ;
import utils.IntWrapper;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class DijkstraShortestPathFinderTests extends BaseGraphTests {

    protected <G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    DijkstraShortestPathFinder<G, V, E> createShortestPathFinder() {
        return new DijkstraShortestPathFinder<>() {
            /** Override method to simulate behavior on grader. */
            @Override
            protected <T> ExtrinsicMinPQ<T> createMinPQ() {
                return super.createMinPQ();
            }
        };
    }

    protected <G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    ShortestPathFinderAssert<G, V, E> assertThat(SPTShortestPathFinder<G, V, E> actual) {
        return new ShortestPathFinderAssert<>(actual);
    }

    protected <G extends Graph<V, E>, V, E extends BaseEdge<V, E>> MapAssert<V, E>
    assertThatSPTOf(G graph, V start, V end) {
        SPTShortestPathFinder<G, V, E> shortestPathFinder = createShortestPathFinder();
        return assertThat(shortestPathFinder).constructingShortestPathsTree(graph, start, end);
    }

    protected <G extends Graph<V, E>, V, E extends BaseEdge<V, E>> ShortestPathFinderAssert.ShortestPathAssert<V, E>
    assertThatShortestPathOf(G graph, Map<V, E> spt, V start, V end) {
        SPTShortestPathFinder<G, V, E> shortestPathFinder = createShortestPathFinder();
        return assertThat(shortestPathFinder).extractingShortestPathFromShortestPathsTree(graph, spt, start, end);
    }

    @Nested
    class LectureExampleDirectedGraph_PathExists extends PathExists<String, Edge<String>> {
        LectureExampleDirectedGraph_PathExists() {
            super(
                directedGraph(
                    edge("a", "b", 2),
                    edge("a", "c", 1),
                    edge("a", "d", 4),

                    edge("b", "c", 5),
                    edge("b", "e", 10),
                    edge("b", "f", 2),

                    edge("c", "a", 9),
                    edge("c", "e", 11),

                    edge("d", "c", 2),

                    edge("e", "d", 7),
                    edge("e", "g", 1),

                    edge("f", "h", 3),

                    edge("g", "e", 3),
                    edge("g", "f", 2),

                    edge("h", "g", 1)),
                spt(
                    edge("a", "b", 2),
                    edge("a", "c", 1),
                    edge("a", "d", 4),
                    edge("g", "e", 3),
                    edge("b", "f", 2),
                    edge("h", "g", 1),
                    edge("f", "h", 3)),
                new String[]{"a", "b", "f", "h", "g", "e"},
                11);
        }
    }

    @Nested
    class SingleEdgeGraph_PathExists extends PathExists<String, Edge<String>> {
        SingleEdgeGraph_PathExists() {
            super(
                graph(edge("s", "t", Math.PI)),
                spt(edge("s", "t",  Math.PI)),
                new String[]{"s", "t"},
                Math.PI);
        }
    }

    @Nested
    class SameStartAndEndVertex_PathExists extends PathExists<String, Edge<String>> {
        SameStartAndEndVertex_PathExists() {
            super(
                graph(edge("s", "t", 500)),
                spt(),
                new String[]{"s"},
                0);
        }
    }

    @Nested
    class InfiniteGraph_PathExists extends PathExists<Integer, Edge<Integer>> {
        InfiniteGraph_PathExists() {
            super(
                new InfiniteGraph(),
                spt(
                    edge(0, 1, 1),
                    edge(0, -1, 1),
                    edge(1, 2, 1),
                    edge(-1, -2, 1),
                    edge(2, 3, 1),
                    edge(-2, -3, 1),
                    edge(3, 4, 1),
                    edge(-3, -4, 1),
                    edge(4, 5, 1)),
                new Integer[]{0, 1, 2, 3, 4, 5},
                5);
        }
    }

    @Nested
    class ZeroEdgeGraph_PathDoesNotExist extends PathDoesNotExist<Integer, Edge<Integer>> {
        ZeroEdgeGraph_PathDoesNotExist() {
            super(new ZeroEdgeGraph(2), spt(), 0, 1);
        }
    }

    @Nested
    class MultiplePathsBetweenStartAndEnd_PathExists extends PathExists<String, Edge<String>> {
        MultiplePathsBetweenStartAndEnd_PathExists() {
            super(
                graph(
                    edge("s", "a", 2),
                    edge("s", "b", 3),
                    edge("s", "c", 6),

                    edge("a", "b", 2),
                    edge("a", "t", 100),

                    edge("b", "c", 4),
                    edge("b", "t", 10)),
                spt(
                    edge("s", "a", 2),
                    edge("s", "b", 3),
                    edge("s", "c", 6),
                    edge("b", "t", 10)),
                new String[]{"s", "b", "t"},
                13);
        }
    }

    @Nested
    class LeastCostPathHasMoreEdgesThanOtherPaths_PathExists extends PathExists<String, Edge<String>> {
        LeastCostPathHasMoreEdgesThanOtherPaths_PathExists() {
            super(
                graph(
                    edge("s", "a", 1),
                    edge("a", "b", 1),
                    edge("b", "c", 1),
                    edge("c", "t", 1),
                    edge("s", "d", 2),
                    edge("d", "t", 3),
                    edge("d", "e", 1)),
                spt(
                    edge("s", "a", 1),
                    edge("a", "b", 1),
                    edge("b", "c", 1),
                    edge("c", "t", 1),
                    edge("s", "d", 2),
                    edge("d", "e", 1)),
                new String[]{"s", "a", "b", "c", "t"},
                4);
        }
    }

    @Nested
    class GraphRequiringRelaxation_PathExists extends PathExists<String, Edge<String>> {
        GraphRequiringRelaxation_PathExists() {
            super(
                graph(
                    edge("s", "a", 2),
                    edge("s", "b", 4),
                    edge("s", "c", 6),
                    edge("a", "b", 1),
                    edge("b", "t", 1),
                    edge("c", "t", 1)),
                spt(
                    edge("s", "a", 2),
                    edge("a", "b", 1),
                    edge("b", "t", 1)),
                new String[]{"s", "a", "b", "t"},
                4);
        }
    }

    @Nested
    class GraphNotRequiringRelaxation_PathExists extends PathExists<String, Edge<String>> {
        GraphNotRequiringRelaxation_PathExists() {
            super(
                graph(
                    edge("s", "a", 2),
                    edge("s", "b", 4),
                    edge("s", "c", 6),
                    edge("a", "b", 10),
                    edge("b", "t", 1),
                    edge("c", "t", 1)),
                spt(
                    edge("s", "a", 2),
                    edge("s", "b", 4),
                    edge("b", "t", 1)),
                new String[]{"s", "b", "t"},
                5);
        }
    }
    /**
     * These tests are likely to fail for code that does not properly check object equality.
     */
    @Nested
    class GraphWithCustomVertexObjects_PathExists extends PathExists<IntWrapper, Edge<IntWrapper>> {
        GraphWithCustomVertexObjects_PathExists() {
            super(
                graph(
                    edge(new IntWrapper(0), new IntWrapper(1)),
                    edge(new IntWrapper(1), new IntWrapper(2))),
                spt(
                    edge(new IntWrapper(0), new IntWrapper(1)),
                    edge(new IntWrapper(1), new IntWrapper(2))),
                new IntWrapper[]{new IntWrapper(0), new IntWrapper(1), new IntWrapper(2)},
                2);
        }
    }

    /**
     * These tests are likely to time out for code that does not properly check object equality.
     */
    @Nested
    class GraphWithCustomVertexObjects_SameStartAndEndVertex_PathExists
        extends PathExists<IntWrapper, Edge<IntWrapper>> {
        GraphWithCustomVertexObjects_SameStartAndEndVertex_PathExists() {
            super(
                new InfiniteIntWrapperGraph(),
                spt(),
                new IntWrapper[]{new IntWrapper(0)},
                new IntWrapper(0),
                new IntWrapper(0),
                0);
        }
    }

    @Nested
    class GraphWithParallelEdgesSortedWeights_PathExists extends PathExists<String, Edge<String>> {
        GraphWithParallelEdgesSortedWeights_PathExists() {
            super(
                graph(
                    edge("s", "t", 1),
                    edge("s", "t", 2),
                    edge("s", "t", 3),
                    edge("s", "t", 4),
                    edge("s", "t", 5)),
                spt(edge("s", "t", 1)),
                new String[]{"s", "t"},
                1);
        }
    }

    @Nested
    class GraphWithParallelEdgesUnsortedWeights_PathExists extends PathExists<String, Edge<String>> {
        GraphWithParallelEdgesUnsortedWeights_PathExists() {
            super(
                graph(
                    edge("s", "t", 5),
                    edge("s", "t", 3),
                    edge("s", "t", 1),
                    edge("s", "t", 2),
                    edge("s", "t", 4)),
                spt(edge("s", "t", 1)),
                new String[]{"s", "t"},
                1);
        }
    }

    @Nested
    class GraphWithSelfLoop_PathExists extends PathExists<String, Edge<String>> {
        GraphWithSelfLoop_PathExists() {
            super(
                graph(
                    edge("s", "a", 2),
                    edge("a", "a", 0), // self loop with appealing weight
                    edge("a", "t", 2)),
                spt(
                    edge("s", "a", 2),
                    edge("a", "t", 2)),
                new String[]{"s", "a", "t"},
                4);
        }
    }

    @Nested
    class GraphWithDisconnectedVertices_PathDoesNotExist extends PathDoesNotExist<String, Edge<String>> {
        GraphWithDisconnectedVertices_PathDoesNotExist() {
            super(
                graph(
                    edge("s", "a", 1),
                    edge("b", "t", 1)),
                spt(edge("s", "a", 1)),
                "s",
                "t");
        }
    }

    @Nested
    class GraphWithUniformWeights_PathExists extends PathExists<String, Edge<String>> {
        GraphWithUniformWeights_PathExists() {
            super(
                graph(
                    edge("s", "a", 1),
                    edge("s", "b", 1),
                    edge("s", "t", 1),

                    edge("a", "b", 1),
                    edge("a", "t", 1),

                    edge("b", "t", 1)),
                spt(
                    edge("s", "t", 1)),
                new String[]{"s", "t"},
                1);
        }
    }

    @Test
    void findShortestPath_twiceOnSameInstance_returnsCorrectPaths() {
        SPTShortestPathFinder<Graph<String, Edge<String>>, String, Edge<String>> pathFinder
            = createShortestPathFinder();

        assertThat(pathFinder).findingShortestPath(
            graph(
                edge("s", "a", 1),
                edge("s", "b", 2),
                edge("a", "t", 3),
                edge("b", "t", 1)),
            "s", "t")
            .hasVertices("s", "b", "t")
            .hasWeightCloseTo(3);

        assertThat(pathFinder).findingShortestPath(
            graph(
                edge("s", "a", 1),
                edge("s", "b", 2),
                edge("a", "t", 1), // same graph shape, but this edge weight changed
                edge("b", "t", 1)),
            "s", "t")
            .hasVertices("s", "a", "t")
            .hasWeightCloseTo(2);
    }

    abstract class PathExists<V, E extends BaseEdge<V, E>> {
        final Graph<V, E> graph;
        final Map<V, E> spt;
        final V[] path;
        final V start;
        final V end;
        final double weight;

        PathExists(Graph<V, E> graph, Map<V, E> spt, V[] path, double weight) {
            this(graph, spt, path, path[0], path[path.length-1], weight);
        }

        PathExists(Graph<V, E> graph, Map<V, E> spt, V[] path, V start, V end, double weight) {
            this.graph = graph;
            this.spt = spt;
            this.path = path;
            this.start = start;
            this.end = end;
            this.weight = weight;
        }

        @Test
        void constructShortestPathsTree_returnsCorrectTree() {
            assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
                assertThatSPTOf(this.graph, this.start, this.end).containsAllEntriesOf(this.spt);
            });
        }

        @Test
        void extractShortestPath_returnsCorrectPath() {
            assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
                assertThatShortestPathOf(this.graph, this.spt, this.start, this.end)
                    .hasVertices(this.path)
                    .hasWeightCloseTo(this.weight);
            });
        }
    }

    abstract class PathDoesNotExist<V, E extends BaseEdge<V, E>> {
        final Graph<V, E> graph;
        final Map<V, E> spt;
        final V start;
        final V end;

        PathDoesNotExist(Graph<V, E> graph, Map<V, E> spt, V start, V end) {
            this.graph = graph;
            this.spt = spt;
            this.start = start;
            this.end = end;
        }

        @Test
        void constructShortestPathsTree_returnsCorrectTree() {
            assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
                assertThatSPTOf(this.graph, this.start, this.end).containsExactlyInAnyOrderEntriesOf(this.spt);
            });
        }

        @Test
        void extractShortestPath_returnsDoesNotExist() {
            assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
                assertThatShortestPathOf(this.graph, this.spt, this.start, this.end).doesNotExist();
            });
        }
    }
}

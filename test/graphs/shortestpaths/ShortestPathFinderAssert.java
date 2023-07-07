package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AbstractDoubleAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.MapAssert;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Asserts for ShortestPathFinders.
 */
public class ShortestPathFinderAssert<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends AbstractObjectAssert<ShortestPathFinderAssert<G, V, E>, SPTShortestPathFinder<G, V, E>> {

    public ShortestPathFinderAssert(SPTShortestPathFinder<G, V, E> actual) {
        super(actual, ShortestPathFinderAssert.class);
    }

    public ShortestPathAssert<V, E> findingShortestPath(G graph, V start, V end) {
        return new ShortestPathAssert<>(actual.findShortestPath(graph, start, end), graph, start, end);
    }

    public MapAssert<V, E> constructingShortestPathsTree(G graph, V start, V end) {
        return assertThat(actual.constructShortestPathsTree(graph, start, end));
    }

    public ShortestPathAssert<V, E> extractingShortestPathFromShortestPathsTree(G graph,
                                                                                Map<V, E> spt,
                                                                                V start,
                                                                                V end) {
        return new ShortestPathAssert<>(actual.extractShortestPath(spt, start, end), graph, start, end);
    }

    public static class ShortestPathAssert<V, E extends BaseEdge<V, E>>
        extends AbstractObjectAssert<ShortestPathAssert<V, E>, ShortestPath<V, E>> {

        public static final double EPSILON = .0001;
        private final Graph<V, E> graph;
        private final V start;
        private final V end;

        public ShortestPathAssert(ShortestPath<V, E> actual,
                                  Graph<V, E> graph,
                                  V start,
                                  V end) {
            super(actual, ShortestPathAssert.class);
            this.graph = graph;
            this.start = start;
            this.end = end;
        }

        public ShortestPath<V, E> getActual() {
            return this.actual;
        }

        private AbstractBooleanAssert<?> extractExists() {
            return extracting(ShortestPath::exists, InstanceOfAssertFactories.BOOLEAN)
                .as("result.exists()");
        }

        private AbstractDoubleAssert<?> extractWeight() {
            return extracting(ShortestPath::totalWeight, InstanceOfAssertFactories.DOUBLE)
                .as("result.totalWeight()");
        }

        // The types in this method aren't perfect, but Object is good enough for our test code.
        private ListAssert<Object> extractVertices() {
            return extracting(ShortestPath::vertices, InstanceOfAssertFactories.LIST)
                .as("result.vertices()");
        }

        public ShortestPathAssert<V, E> doesNotExist() {
            extractExists().isFalse();
            return this;
        }

        public ShortestPathAssert<V, E> exists() {
            extractExists().isTrue();
            return this;
        }

        @SafeVarargs
        public final ShortestPathAssert<V, E> hasVertices(V... vertices) {
            exists();
            pathIsValid();
            extractVertices().containsExactly(vertices);
            return this;
        }

        public ShortestPathAssert<V, E> hasSolutionEquivalentTo(ShortestPath<V, E> expected) {
            extractExists().isEqualTo(expected.exists());
            if (expected.exists()) {
                hasWeightCloseTo(expected.totalWeight());
                pathIsValid();
            }
            return this;
        }

        public ShortestPathAssert<V, E> hasWeightCloseTo(double expected) {
            extractWeight().isCloseTo(expected, within(EPSILON));
            return this;
        }

        public ShortestPathAssert<V, E> pathIsValid() {
            if (!actual.exists()) {
                // skip if not solved; validity check only matters when solution exists
                return this;
            }
            // check start and end match
            extractVertices().first().as(describe("first vertex")).isEqualTo(this.start);
            extractVertices().last().as(describe("last vertex")).isEqualTo(this.end);
            // check transitions are valid according to graph
            checkHasValidTransitions();
            return this;
        }

        private void checkHasValidTransitions() {
            V prev = null;
            boolean firstIteration = true;
            for (E edge : actual.edges()) {
                V from = edge.from();
                V to = edge.to();

                if (!firstIteration) {
                    assertThat(from).as("currEdge.from() should be prevEdge.to()").isEqualTo(prev);
                }

                if (!graph.outgoingEdgesFrom(from).contains(edge)) {
                    as(describe("solution")).
                        failWithMessage("Invalid edge found in solution: "
                            + describeFromTo(from, to));
                }

                prev = to;
                firstIteration = false;
            }
        }

        private String describe(String s) {
            return s + " of shortest path " + describeFromTo(this.start, this.end);
        }

        private String describeFromTo(V from, V to) {
            String fromString = from.toString().stripTrailing();
            String toString = to.toString().stripTrailing();
            if (fromString.contains("\n") || toString.contains("\n")) {
                return String.format("from:%n%s%n%nto:%s", fromString, toString);
            }
            return String.format("from <%s> to <%s>", fromString, toString);
        }
    }
}

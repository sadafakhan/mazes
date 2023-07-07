package mazes.logic;

import graphs.AdjacencyListUndirectedGraph;
import graphs.EdgeWithData;
import mazes.entities.Room;
import mazes.entities.Wall;

import java.util.Collection;

/**
 * A convenience class that sets the generic parameters for a {@link AdjacencyListUndirectedGraph}
 * representing a maze.
 */
public class MazeGraph extends AdjacencyListUndirectedGraph<Room, EdgeWithData<Room, Wall>> {
    /**
     * @see AdjacencyListUndirectedGraph#AdjacencyListUndirectedGraph(Collection)
     */
    public MazeGraph(Collection<EdgeWithData<Room, Wall>> edges) {
        super(edges);
    }
}

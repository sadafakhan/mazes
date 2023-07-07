package mazes.logic;

import graphs.EdgeWithData;
import graphs.shortestpaths.ShortestPath;
import graphs.shortestpaths.ShortestPathFinder;
import mazes.entities.Room;
import mazes.entities.Wall;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Essentially a wrapper around a {@link ShortestPathFinder} that includes a convenience method
 * to solve a maze specified by a list of pathway "walls."
 */
public class MazeSolver {
    private final ShortestPathFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> shortestPathFinder;

    public MazeSolver(ShortestPathFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> shortestPathFinder) {
        this.shortestPathFinder = shortestPathFinder;
    }

    /**
     * Returns an optional containing the list of pathway "walls" to traverse to get from start to
     * end, if possible. If no path exists, returns an empty Optional.
     */
    public Optional<List<Wall>> solveMaze(Set<Wall> pathways, Room start, Room end) {
        /*
        From the given walls, construct a new MazeGraph with an edge for each wall.

        (The vertices are the rooms on either side of the wall,
        and the weight is the distance between the centers of the two rooms.
        Each edge also stores the Wall instance it represents so that we can recover the
        Walls after running the ShortestPathFinder.)
        */
        List<EdgeWithData<Room, Wall>> edges = pathways.stream()
            .map(wall -> new EdgeWithData<>(wall.getRoom1(), wall.getRoom2(), wall.getDistance(), wall))
            .collect(Collectors.toList());
        MazeGraph graph = new MazeGraph(edges);

        ShortestPath<Room, EdgeWithData<Room, Wall>> shortestPath = shortestPathFinder.findShortestPath(
            graph, start, end);

        // Extract the wall from each edge in the shortest path.
        if (shortestPath.exists()) {
            List<Wall> walls = shortestPath.edges().stream().map(EdgeWithData::data).collect(Collectors.toList());
            return Optional.of(walls);
        }
        return Optional.empty();
    }
}

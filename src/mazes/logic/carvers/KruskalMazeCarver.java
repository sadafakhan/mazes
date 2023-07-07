package mazes.logic.carvers;

// import graphs.BaseEdge;
// import graphs.Edge;
import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.LineSegment;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

// import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {

        // create an edge representing each wall and add all of them to a set of edges "edges"
        Collection<EdgeWithData<Room, Wall>> edges = new ArrayList<>();
        for (Wall wall : walls) {
            double weight = rand.nextDouble();
            EdgeWithData edge = new EdgeWithData(wall.getRoom1(), wall.getRoom2(), weight, wall.getDividingLine());
            edges.add(edge);
        }

        // create a new mazegraph using said edges
        MazeGraph maze = new MazeGraph(edges);

        // find MST on this mazegraph
        MinimumSpanningTree mst = this.minimumSpanningTreeFinder.findMinimumSpanningTree(maze);

        // add back the walls from the MST
        Collection<EdgeWithData> mstEdges = mst.edges();
        Set<Wall> toRemove = new HashSet<>();

        for (EdgeWithData edge : mstEdges) {
            Wall wall = new Wall((Room) edge.from(), (Room) edge.to(), (LineSegment) edge.data());
            toRemove.add(wall);
        }

        return toRemove;
    }
}

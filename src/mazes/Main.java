package mazes;

import graphs.minspantrees.KruskalMinimumSpanningTreeFinder;
import graphs.shortestpaths.DijkstraShortestPathFinder;
import mazes.gui.MainWindow;
import mazes.logic.MazeSolver;
import mazes.logic.carvers.KruskalMazeCarver;
import mazes.logic.carvers.MazeCarver;
import mazes.logic.carvers.RandomMazeCarver;
import mazes.logic.generators.GridGenerator;
import mazes.logic.generators.HexagonGenerator;
import mazes.logic.generators.MazeBaseGenerator;
import mazes.logic.generators.VoronoiGenerator;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    /**
     * Starts the maze generator/solver application.
     */
    public static void main(String[] args) {
        /*
        This dictionary contains objects that are responsible for creating the initial
        "shape" of the maze. We've implemented two options for you: one that creates a
        grid of rectangle, and another that creates a more "cellular" shape.

        Feel free to tweak any of the constants below -- for example, if you want the
        grid generator to have a different number of rows and columns.

        (Note: we use LinkedHashMap so we can control the order in which we present
        the options)
        */
        Map<String, MazeBaseGenerator> baseGenerators = new LinkedHashMap<>();
        baseGenerators.put(
            "Grid",
            new GridGenerator(60, 80));
        baseGenerators.put(
            "Voronoi",
            new VoronoiGenerator(3000, 10, 5, 5));
        baseGenerators.put(
            "Hexagonal",
            new HexagonGenerator(36));

        /*
        This dictionary contains objects that are responsible for taking a
        maze and removing or "carving out" walls to produce an actually-usable maze.

        We've implemented one for you -- a `RandomMazeCarver` that deletes walls at random.
        You will need to implement a second carver that uses your Kruskal's algorithm
        implementation (see spec for details).
        */
        Map<String, MazeCarver> baseCarvers = new LinkedHashMap<>();
        baseCarvers.put(
            "Do not delete any walls",
            new RandomMazeCarver(1.0));
        baseCarvers.put(
            "Delete random walls (keep 30% of walls)",
            new RandomMazeCarver(0.3));
        baseCarvers.put(
            "Delete random walls (keep 50% of walls)",
            new RandomMazeCarver(0.5));
        baseCarvers.put(
            "Run (randomized) Kruskal",
            new KruskalMazeCarver(new KruskalMinimumSpanningTreeFinder<>()));

        // This actually launches the window itself and starts the program.
        MainWindow.launch(baseGenerators, baseCarvers, new MazeSolver(new DijkstraShortestPathFinder<>()));
    }
}

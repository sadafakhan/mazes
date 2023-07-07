package mazes.logic.carvers;

import edu.washington.cse373.BaseTest;
import graphs.minspantrees.KruskalMinimumSpanningTreeFinder;
import mazes.entities.Maze;
import mazes.entities.Wall;
import mazes.logic.generators.GridGenerator;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.util.Set;

public class KruskalMazeCarverTests extends BaseTest {
    public static final int NUM_ROWS = 40;
    public static final int NUM_COLS = 40;

    protected MazeCarver createMazeCarver() {
        return new KruskalMazeCarver(new KruskalMinimumSpanningTreeFinder<>());
    }

    protected MazeCarver createMazeCarver(long seed) {
        return new KruskalMazeCarver(new KruskalMinimumSpanningTreeFinder<>(), seed);
    }

    private static Maze generateMaze() {
        return new GridGenerator(NUM_ROWS, NUM_COLS).generateBaseMaze(
            new Rectangle(0, 0, 800, 800));
    }

    @Test
    void choose_returnsCorrectNumberOfWalls() {
        Maze maze = generateMaze();
        MazeCarver carver = createMazeCarver();

        Set<Wall> walls = carver.chooseWallsToRemove(maze.getRemovableWalls());

        assertThat(walls).hasSize(NUM_ROWS * NUM_COLS - 1);
    }

    @Test
    void choose_withDifferentSeeds_returnsDifferentWalls() {
        Maze maze = generateMaze();

        Set<Wall> removeOne = createMazeCarver(1).chooseWallsToRemove(maze.getRemovableWalls());
        Set<Wall> removeTwo = createMazeCarver(2).chooseWallsToRemove(maze.getRemovableWalls());

        assertThat(removeOne)
            .as("walls to remove should be different when using different random seeds.")
            .isNotEqualTo(removeTwo);
    }
}

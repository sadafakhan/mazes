package mazes.logic.carvers;

import mazes.entities.Maze;
import mazes.entities.Wall;

import java.util.HashSet;
import java.util.Set;

/**
 * Determines which walls to remove from an initial "maze" created by a `MazeBaseGenerator`
 * (a maze base, where there exists a wall between all adjacent rooms) in order to form a real maze.
 */
public abstract class MazeCarver {
    /**
     * Given a set of walls separating rooms in a maze base, returns a set of every wall that
     * should be removed to form a maze.
     */
    protected abstract Set<Wall> chooseWallsToRemove(Set<Wall> walls);

    /**
     * Accepts a maze base and partitions its walls into two sets: walls to remove and walls to
     * keep in order to form a maze.
     */
    public CarvedMaze carveMaze(Maze initialMaze) {
        Set<Wall> pathways = this.chooseWallsToRemove(initialMaze.getRemovableWalls());

        Set<Wall> walls = new HashSet<>(initialMaze.getRemovableWalls());
        walls.removeAll(pathways);
        walls.addAll(initialMaze.getUnremovableWalls());

        return new CarvedMaze(walls, pathways);
    }

    /**
     * The output of {@link #carveMaze}; contains `walls`, the set of walls to keep, and `pathways`,
     * the set of walls to remove.
     */
    public static class CarvedMaze {
        private final Set<Wall> walls;

        private final Set<Wall> pathways;

        public CarvedMaze(Set<Wall> walls, Set<Wall> pathways) {
            this.walls = walls;
            this.pathways = pathways;
        }

        public Set<Wall> walls() {
            return walls;
        }

        public Set<Wall> pathways() {
            return pathways;
        }
    }
}

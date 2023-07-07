package mazes.logic.carvers;

import mazes.entities.Wall;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Randomly removes walls, with a given probability.
 */
public class RandomMazeCarver extends MazeCarver {
    private final double probabilityOfKeepingWall;
    private final Random rand;

    public RandomMazeCarver(double probabilityOfKeepingWall) {
        this.probabilityOfKeepingWall = probabilityOfKeepingWall;
        this.rand = new Random();
    }
    public RandomMazeCarver(double probabilityOfKeepingWall, long seed) {
        this.probabilityOfKeepingWall = probabilityOfKeepingWall;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        Set<Wall> toRemove = new HashSet<>();
        for (Wall wall : walls) {
            if (this.rand.nextDouble() >= this.probabilityOfKeepingWall) {
                toRemove.add(wall);
            }
        }
        return toRemove;
    }
}

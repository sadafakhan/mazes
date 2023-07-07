package mazes.logic.generators;

import mazes.entities.LineSegment;
import mazes.entities.Maze;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * A class responsible for returning an initial maze with walls present between every single room.
 *
 * A `MazeCarver` class is then responsible for figuring out which walls to remove to form
 * a valid maze.
 */
public abstract class MazeBaseGenerator {
    /**
     * Generates an initial "maze".
     */
    public abstract Maze generateBaseMaze(Rectangle boundingBox);

    protected List<LineSegment> polygonToLineSegment(Polygon polygon) {
        List<LineSegment> output = new ArrayList<>();

        int[] xs = polygon.xpoints;
        int[] ys = polygon.ypoints;
        for (int i = 0; i < polygon.npoints; i++) {
            Point p1 = new Point(xs[i], ys[i]);
            Point p2 = new Point(
                    xs[(i + 1) % polygon.npoints],
                    ys[(i + 1) % polygon.npoints]);

            output.add(new LineSegment(p1, p2));
        }

        return output;
    }

    protected int round(double num) {
        return (int) Math.round(num);
    }

    protected int[] roundArray(double[] array, int length) {
        int[] output = new int[length];
        for (int i = 0; i < length; i++) {
            output[i] = round(array[i]);
        }
        return output;
    }

}

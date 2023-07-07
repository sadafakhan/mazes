package mazes.logic.generators;

import mazes.entities.LineSegment;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generates a grid-like maze, where every room is a rectangle with to up to four adjacent rooms.
 */
public class GridGenerator extends MazeBaseGenerator {
    private final int numRows;
    private final int numColumns;

    /**
     * Accepts the number of rows and columns the grid ought to have.
     */
    public GridGenerator(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;
    }

    public Maze generateBaseMaze(Rectangle boundingBox) {
        Room[][] grid = this.buildRooms(boundingBox);
        return new Maze(
                this.extractRooms(grid),
                this.extractWalls(grid),
                new HashSet<>(),
                grid[0][0],
                grid[grid.length - 1][grid[0].length - 1]);
    }

    private Room[][] buildRooms(Rectangle boundingBox) {
        Room[][] grid = new Room[this.numColumns][this.numRows];

        double yDelta = 1.0 * boundingBox.height / this.numRows;
        double xDelta = 1.0 * boundingBox.width / this.numColumns;

        for (int i = 0; i < numRows; i++) {
            int yMin = round(i * yDelta + boundingBox.y);
            int yMax = round((i + 1) * yDelta + boundingBox.y);
            for (int j = 0; j < numColumns; j++) {
                int xMin = round(j * xDelta + boundingBox.x);
                int xMax = round((j + 1) * xDelta + boundingBox.x);

                Point center = new Point(
                        round((xMin + xMax) / 2.0),
                        round((yMin + yMax) / 2.0));

                Polygon polygon = new Polygon(
                        new int[]{xMin, xMax, xMax, xMin},
                        new int[]{yMin, yMin, yMax, yMax},
                        4);

                grid[j][i] = new Room(center, polygon);
            }
        }

        return grid;
    }

    private Set<Room> extractRooms(Room[][] grid) {
        Set<Room> rooms = new HashSet<>();
        for (int x = 0; x < this.numColumns; x++) {
            for (int y = 0; y < this.numRows; y++) {
                rooms.add(grid[x][y]);
            }
        }
        return rooms;
    }

    private Set<Wall> extractWalls(Room[][] grid) {
        Set<Wall> walls = new HashSet<>();

        for (int x = 0; x < this.numColumns; x++) {
            for (int y = 0; y < this.numRows; y++) {
                Room room = grid[x][y];
                List<LineSegment> segments = this.polygonToLineSegment(room.getPolygon());

                if (x > 0) {
                    walls.add(new Wall(room, grid[x - 1][y], segments.get(3)));
                }
                if (y > 0) {
                    walls.add(new Wall(room, grid[x][y - 1], segments.get(0)));
                }
            }
        }

        return walls;
    }
}

package mazes.logic.generators;

import mazes.entities.LineSegment;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HexagonGenerator extends MazeBaseGenerator {
    // Since hexagons are symmetrical, the rows will determine how wide the shapes are and therefore the number of cols
    private final int rows;

    public HexagonGenerator(int rows) {
        this.rows = rows * 2;
    }

    @Override
    public Maze generateBaseMaze(Rectangle boundingBox) {
        Room[][] rooms = buildRooms(boundingBox);

        return new Maze(
            collectRooms(rooms),
            collectConnectingWalls(rooms),
            collectStaticWalls(rooms),
            rooms[0][0],
            rooms[rooms.length -1 ][rooms[0].length -1]);
    }

    private Room[][] buildRooms(Rectangle boundingBox) {
        double hexHeight = boundingBox.height / (rows / 2.0 + 0.5);
        double hexWidth = hexHeight / Math.cos(Math.PI / 6);
        double edgeLength = hexHeight * Math.tan(Math.PI / 6);

        int columns = (int) Math.floor((boundingBox.width - (hexWidth - edgeLength) / 2) / (hexWidth + edgeLength));
        int xOffset = boundingBox.x + round((
            boundingBox.width - columns * (hexWidth + edgeLength) - (hexWidth - edgeLength) / 2) / 2);
        Room[][] rooms = new Room[columns][rows];

        for (int y = 0; y < rows; y++) {
            double centerY = (y + 1) * hexHeight / 2 + boundingBox.y;
            for (int x = 0; x < columns; x++) {
                double centerX = xOffset + (hexWidth + edgeLength) * x;
                if (y % 2 == 0) {
                    centerX += hexWidth / 2;
                } else {
                    centerX += hexWidth + edgeLength / 2;
                }

                Point center = new Point(round(centerX), round(centerY));
                Polygon hex = hexagon(centerX, centerY, hexHeight, hexWidth, edgeLength);
                rooms[x][y] = new Room(center, hex);
            }
        }

        return rooms;
    }

    private Set<Room> collectRooms(Room[][] grid) {
        Set<Room> rooms = new HashSet<>();
        for (Room[] row : grid) {
            Collections.addAll(rooms, row);
        }

        return rooms;
    }

    private Set<Wall> collectConnectingWalls(Room[][] grid) {
        Set<Wall> walls = new HashSet<>();

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                Room currentRoom = grid[x][y];
                List<LineSegment> segments = this.polygonToLineSegment(currentRoom.getPolygon());


                if (y < grid[0].length - 2) {
                    walls.add(new Wall(currentRoom, grid[x][y + 2], segments.get(0)));
                }

                if (y < grid[0].length - 1 && (x < grid.length - 1 || y % 2 == 0)) {
                    int xDelta = y % 2 == 0 ? 0 : 1;
                    walls.add(new Wall(currentRoom, grid[x + xDelta][y + 1], segments.get(1)));
                }

                if (y < grid[0].length - 1 && (x > 0 || y % 2 == 1)) {
                    int xDelta = y % 2 == 0 ? -1 : 0;
                    walls.add(new Wall(currentRoom, grid[x + xDelta][y + 1], segments.get(5)));
                }
            }
        }

        return walls;
    }

    private Set<Wall> collectStaticWalls(Room[][] grid) {
        Set<Wall> walls = new HashSet<>();


        for (int x = 0; x < grid.length; x++) {
            Room topRoom = grid[x][0];
            List<LineSegment> topSegments = this.polygonToLineSegment(topRoom.getPolygon());

            walls.add(new Wall(topRoom, topRoom, topSegments.get(2)));
            walls.add(new Wall(topRoom, topRoom, topSegments.get(3)));
            walls.add(new Wall(topRoom, topRoom, topSegments.get(4)));

            Room secondTopRoom = grid[x][1];
            List<LineSegment> secondTopSegments = this.polygonToLineSegment(secondTopRoom.getPolygon());
            walls.add(new Wall(secondTopRoom, secondTopRoom, secondTopSegments.get(3)));

            Room bottomRoom = grid[x][grid[0].length - 1];
            List<LineSegment> bottomSegments = this.polygonToLineSegment(bottomRoom.getPolygon());

            walls.add(new Wall(bottomRoom, bottomRoom, bottomSegments.get(0)));
            walls.add(new Wall(bottomRoom, bottomRoom, bottomSegments.get(1)));
            walls.add(new Wall(bottomRoom, bottomRoom, bottomSegments.get(5)));

            Room secondBottomRoom = grid[x][grid[0].length - 2];
            List<LineSegment> secondBottomSegments = this.polygonToLineSegment(secondBottomRoom.getPolygon());
            walls.add(new Wall(secondBottomRoom, secondBottomRoom, secondBottomSegments.get(0)));
        }

        for (int y = 0; y < grid[0].length; y += 2) {
            Room leftRoom = grid[0][y];
            List<LineSegment> leftSegments = this.polygonToLineSegment(leftRoom.getPolygon());

            walls.add(new Wall(leftRoom, leftRoom, leftSegments.get(4)));
            walls.add(new Wall(leftRoom, leftRoom, leftSegments.get(5)));

            Room rightRoom = grid[grid.length - 1][y + 1];
            List<LineSegment> rightSegments = this.polygonToLineSegment(rightRoom.getPolygon());

            walls.add(new Wall(rightRoom, rightRoom, rightSegments.get(1)));
            walls.add(new Wall(rightRoom, rightRoom, rightSegments.get(2)));
        }

        return walls;
    }

    private Polygon hexagon(double centerX, double centerY, double hexHeight, double hexWidth, double edgeLength) {
        int[] xCoords = new int[]{
            round(centerX - edgeLength / 2),
            round(centerX + edgeLength / 2),
            round(centerX + hexWidth / 2),
            round(centerX + edgeLength / 2),
            round(centerX - edgeLength / 2),
            round(centerX - hexWidth / 2)
        };
        int[] yCoords = new int[] {
            round(centerY + hexHeight / 2),
            round(centerY + hexHeight / 2),
            round(centerY),
            round(centerY - hexHeight / 2),
            round(centerY - hexHeight / 2),
            round(centerY)
        };
        return new Polygon(xCoords, yCoords, 6);
    }
}

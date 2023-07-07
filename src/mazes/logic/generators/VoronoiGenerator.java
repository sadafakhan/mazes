package mazes.logic.generators;

import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;
import mazes.entities.LineSegment;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Generates a maze where the rooms are randomly distributed across the bounding box.
 * Walls are placed equidistant between two adjacent rooms.
 */
public class VoronoiGenerator extends MazeBaseGenerator {
    private final int numRooms;
    private final int sampleRate;
    private final int minimumWallLength;
    private final int minimumSpaceFromBoundingBox;
    private final Integer randomSeed;

    /**
     * @param numRooms  the number of rooms the graph should contain
     * @param sampleRate  how many times the algorithm should try to find a good position for a new node
     * @param minimumWallLength  if a generated wall is less than this length, we don't connect the two adjacent rooms
     * @param minimumSpaceFromBoundingBox  how far away the center of the room should be from the bounding box
     */
    public VoronoiGenerator(int numRooms, int sampleRate, int minimumWallLength, int minimumSpaceFromBoundingBox) {
        this(numRooms, sampleRate, minimumWallLength, minimumSpaceFromBoundingBox, null);
    }

    /**
     * @param numRooms  the number of rooms the graph should contain
     * @param sampleRate  how many times the algorithm should try to find a good position for a new node
     * @param minimumWallLength  if a generated wall is less than this length, we don't connect the two adjacent rooms
     * @param minimumSpaceFromBoundingBox  how far away the center of the room should be from the bounding box
     * @param seed  the random seed to use
     */
    public VoronoiGenerator(int numRooms, int sampleRate, int minimumWallLength, int minimumSpaceFromBoundingBox,
                            Integer seed) {
        this.numRooms = numRooms;
        this.sampleRate = sampleRate;
        this.minimumWallLength = minimumWallLength;
        this.minimumSpaceFromBoundingBox = minimumSpaceFromBoundingBox;
        this.randomSeed = seed;
    }

    public Maze generateBaseMaze(Rectangle boundingBox) {
        Rectangle centerBoundingBox = new Rectangle(
                boundingBox.x + this.minimumSpaceFromBoundingBox,
                boundingBox.y + this.minimumSpaceFromBoundingBox,
                boundingBox.width - 2 * this.minimumSpaceFromBoundingBox,
                boundingBox.height - 2 * this.minimumSpaceFromBoundingBox);

        // Generate initial room locations
        OpenList sites = this.generateCells(centerBoundingBox);
        PolygonSimple boundingPolygon = this.boundingBoxToPolygon(boundingBox);

        // Run Voronoi
        PowerDiagram diagram = new PowerDiagram();
        diagram.setSites(sites);
        diagram.setClipPoly(boundingPolygon);
        diagram.computeDiagram();

        // Convert sites into rooms
        Set<Room> rooms = new HashSet<>();
        Map<Point, Room> pointsToSite = new HashMap<>();
        for (Site site : sites) {
            Room room = this.siteToRoom(site);
            pointsToSite.put(room.getCenter(), room);
            rooms.add(room);
        }

        // Convert sites into walls
        Set<Wall> walls = new HashSet<>();
        Set<Wall> untouchableWalls = new HashSet<>();
        Map<LineSegment, Point> edgeToPoints = new HashMap<>();
        for (Room room : rooms) {
            Point vertex = room.getCenter();
            for (LineSegment seg : this.polygonToLineSegment(room.getPolygon())) {
                if (!edgeToPoints.containsKey(seg)) {
                    edgeToPoints.put(seg, vertex);
                } else {
                    Point otherVertex = edgeToPoints.get(seg);
                    Wall wall = new Wall(pointsToSite.get(vertex), pointsToSite.get(otherVertex), seg);
                    if (seg.length() > this.minimumWallLength) {
                        walls.add(wall);
                    } else {
                        untouchableWalls.add(wall);
                    }
                }
            }
        }

        Point start = new Point(boundingBox.x + 5, boundingBox.y + 5);
        Point end = new Point(boundingBox.x + boundingBox.width - 5, boundingBox.y + boundingBox.height - 5);
        return new Maze(rooms, walls, untouchableWalls, findRoom(rooms, start), findRoom(rooms, end));
    }

    private PolygonSimple boundingBoxToPolygon(Rectangle boundingBox) {
        PolygonSimple boundingPolygon = new PolygonSimple();
        boundingPolygon.add(boundingBox.getMinX(), boundingBox.getMinY());
        boundingPolygon.add(boundingBox.getMinX(), boundingBox.getMaxY());
        boundingPolygon.add(boundingBox.getMaxX(), boundingBox.getMaxY());
        boundingPolygon.add(boundingBox.getMaxX(), boundingBox.getMinY());
        return boundingPolygon;
    }

    private Room siteToRoom(Site site) {
        PolygonSimple oldPolygon = site.getPolygon();

        Point center = new Point(round(site.getX()), round(site.getY()));
        Polygon polygon = new Polygon(
                roundArray(oldPolygon.getXPoints(), oldPolygon.length),
                roundArray(oldPolygon.getYPoints(), oldPolygon.length),
                oldPolygon.length);

        return new Room(center, polygon);
    }

    private OpenList generateCells(Rectangle boundingBox) {
        OpenList output = new OpenList();
        List<Point> points = new ArrayList<>();

        Random rand;
        if (this.randomSeed == null) {
            rand = new Random();
        } else {
            rand = new Random(this.randomSeed);
        }

        for (int i = 0; i < this.numRooms; i++) {
            double bestDistance = 0;
            Point bestPoint = null;

            for (int j = 0; j < this.sampleRate; j++) {
                int randX = this.nextInt(rand, boundingBox.x, boundingBox.x + boundingBox.width);
                int randY = this.nextInt(rand, boundingBox.y, boundingBox.y + boundingBox.height);
                Point candidate = new Point(randX, randY);

                double distance = points.stream()
                    .mapToDouble(p -> p.distance(candidate))
                    .min().orElse(Double.MAX_VALUE);
                if (distance > bestDistance) {
                    bestDistance = distance;
                    bestPoint = candidate;
                }
            }

            assert bestPoint != null;
            output.add(new Site(bestPoint.x, bestPoint.y));
            points.add(bestPoint);
        }

        return output;
    }

    private int nextInt(Random rand, int min, int max) {
        return rand.nextInt(max - min) + min;
    }

    private Room findRoom(Set<Room> rooms, Point p) {
        for (Room room : rooms) {
            if (room.contains(p)) {
                return room;
            }
        }
        return null;
    }
}

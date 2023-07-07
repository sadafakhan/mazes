package mazes.entities;

import java.awt.Point;
import java.awt.Polygon;

public class Room {
    private final Point center;
    private final Polygon polygon;

    public Room(Point center, Polygon polygon) {
        this.center = center;
        this.polygon = polygon;
    }

    public boolean contains(Point point) {
        return polygon.contains(point);
    }

    public Point getCenter() {
        return this.center;
    }

    public Polygon getPolygon() {
        return this.polygon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        Room room = (Room) o;

        // Checking only the center is sufficient when trying to identify rooms.
        return center.equals(room.center);
    }

    @Override
    public int hashCode() {
        return center.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Room(x=%x, y=%x)", this.center.x, this.center.y);
    }
}

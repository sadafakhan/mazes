package mazes.entities;

import java.util.Objects;

/**
 * Represents a boundary between two rooms.
 */
public class Wall {
    private final Room room1;
    private final Room room2;
    private final LineSegment dividingLine;

    /**
     * Constructs a wall between the two given rooms, and automatically constructs
     * the distance.
     */
    public Wall(Room room1, Room room2, LineSegment dividingLine) {
        this.room1 = room1;
        this.room2 = room2;
        this.dividingLine = dividingLine;
    }

    /**
     * Returns the one of the two rooms separated by the wall.
     */
    public Room getRoom1() {
        return this.room1;
    }

    /**
     * Returns the other of the two rooms separated by the wall.
     */
    public Room getRoom2() {
        return this.room2;
    }

    /**
     * Returns the physical line dividing the two rooms.
     */
    public LineSegment getDividingLine() {
        return this.dividingLine;
    }

    /**
     * Returns the distance between the centers of the two rooms.
     */
    public double getDistance() {
        return this.room1.getCenter().distance(this.room2.getCenter());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Wall wall = (Wall) o;
        return Objects.equals(room1, wall.room1) &&
            Objects.equals(room2, wall.room2) &&
            Objects.equals(dividingLine, wall.dividingLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(room1, room2, dividingLine);
    }

    @Override
    public String toString() {
        return String.format("Wall(room1=%s, room2=%s)", this.room1, this.room2);
    }
}

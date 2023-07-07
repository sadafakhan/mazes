package mazes.entities;

import java.awt.Point;

/**
 * Represents a line between two points.
 */
public class LineSegment {
    public final Point start;
    public final Point end;

    public LineSegment(Point start, Point end) {
        if (start.x < end.x || (start.x == end.x && start.y <= end.y)) {
            this.start = start;
            this.end = end;
        } else {
            this.start = end;
            this.end = start;
        }
    }

    /**
     * Returns the length of this line.
     */
    public double length() {
        double deltaX = start.x - end.x;
        double deltaY = start.y - end.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Returns the point located within the center of this line segment.
     */
    public Point midpoint() {
        return new Point(
                (int) Math.round((start.x + end.x) / 2.0),
                (int) Math.round((start.y + end.y) / 2.0));
    }

    /**
     * Returns the point located at one end of the line.
     */
    public Point getStart() {
        return this.start;
    }

    /**
     * Returns the point located at the other end of the line.
     */
    public Point getEnd() {
        return this.end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        LineSegment that = (LineSegment) o;

        if (!start.equals(that.start)) { return false; }
        return end.equals(that.end);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("LineSegment(%s, %s)", this.start, this.end);
    }
}

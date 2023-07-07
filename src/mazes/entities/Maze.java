package mazes.entities;

import java.util.Collections;
import java.util.Set;

/**
 * Represents a maze.
 */
public class Maze {
    private final Set<Room> rooms;
    private final Set<Wall> walls;
    private final Set<Wall> untouchableWalls;
    private final Room start;
    private final Room end;

    public Maze(Set<Room> rooms, Set<Wall> walls, Set<Wall> untouchableWalls, Room start, Room end) {
        this.rooms = Collections.unmodifiableSet(rooms);
        this.walls = Collections.unmodifiableSet(walls);
        this.untouchableWalls = Collections.unmodifiableSet(untouchableWalls);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the set of all walls in the maze.
     */
    public Set<Room> getRooms() {
        return this.rooms;
    }

    /**
     * Returns the set of all removable walls between rooms.
     */
    public Set<Wall> getRemovableWalls() {
        return this.walls;
    }

    /**
     * Returns the set of all unremovable walls between rooms.
     *
     * In other words, when we're generating a maze, these walls must ALWAYS be present in the final
     * maze, for one reason or another.
     */
    public Set<Wall> getUnremovableWalls() {
        return this.untouchableWalls;
    }

    /**
     * Returns the starting room of the maze
     */
    public Room getStart() {
        return this.start;
    }

    /**
     * Returns the ending room of the maze
     */
    public Room getEnd() {
        return this.end;
    }
}

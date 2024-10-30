package org.communication.Infrastructure.robot;

public class Position {
    private final int x;
    private final int y;

    /**
     * Constructs a position with specified x and y coordinates.
     *
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the position.
     *
     * @return The x-coordinate of the position.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the position.
     *
     * @return The y-coordinate of the position.
     */
    public int getY() {
        return y;
    }

    /**
     * Checks if this position is equal to another object.
     *
     * @param o The object to compare.
     * @return true if the objects are equal (have the same coordinates), false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }

    /**
     * Returns a string representation of the position in the format [x, y].
     *
     * @return A string representation of the position.
     */
    @Override
    public String toString() {
        return "[" + this.x + ", " + this.y + "]";
    }

    /**
     * Checks if this position is within a specified bounding box defined by top-left and bottom-right positions.
     *
     * @param topLeft     The top-left position of the bounding box.
     * @param bottomRight The bottom-right position of the bounding box.
     * @return true if this position is within the bounding box, false otherwise.
     */
    public boolean isIn(Position topLeft, Position bottomRight) {
        boolean withinTop = this.y <= topLeft.getY();
        boolean withinBottom = this.y >= bottomRight.getY();
        boolean withinLeft = this.x >= topLeft.getX();
        boolean withinRight = this.x <= bottomRight.getX();
        return withinTop && withinBottom && withinLeft && withinRight;
    }

    /**
     * Calculates the Euclidean distance between this position and another position.
     *
     * @param other The other position to calculate the distance to.
     * @return The Euclidean distance between this position and the other position.
     */
    public int distanceTo(Position other){
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return (int) Math.sqrt(dx * dx + dy * dy);
    }
    
}

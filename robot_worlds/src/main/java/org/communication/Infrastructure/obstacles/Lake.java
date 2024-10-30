package org.communication.Infrastructure.obstacles;

public class Lake {
    private int x;
    private int y;

    /**
     * Constructs a Lake with the specified x and y coordinates.
     *
     * @param x the x coordinate of the lake
     * @param y the y coordinate of the lake
     */
    public Lake(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of the lake.
     *
     * @return the x coordinate of the lake
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the lake.
     *
     * @return the y coordinate of the lake
     */
    public int getY() {
        return y;
    }

    /**
     * Returns a string representation of the lake, including its position.
     *
     * @return a string representation of the lake
     */
    @Override
    public String toString() {
        return "Lake: - At position (" + getX() + ", " + getY() + ")";
    }
}

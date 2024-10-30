package org.communication.Infrastructure.obstacles;

public class Mountain {
    private int x;
    private int y;

    /**
     * Constructs a Mountain with the specified x and y coordinates.
     *
     * @param x the x coordinate of the mountain
     * @param y the y coordinate of the mountain
     */
    public Mountain(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of the mountain.
     *
     * @return the x coordinate of the mountain
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the mountain.
     *
     * @return the y coordinate of the mountain
     */
    public int getY() {
        return y;
    }

    /**
     * Returns a string representation of the mountain, including its position.
     *
     * @return a string representation of the mountain
     */
    @Override
    public String toString() {
        return "Mountain: - At position (" + getX() + ", " + getY() + ")";
    }
}

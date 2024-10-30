package org.communication.Infrastructure.obstacles;

public class Obstacle {
    private int x;
    private int y;

    /**
     * Constructs an Obstacle with the specified x and y coordinates.
     *
     * @param x the x coordinate of the obstacle
     * @param y the y coordinate of the obstacle
     */
    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of the obstacle.
     *
     * @return the x coordinate of the obstacle
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the obstacle.
     *
     * @return the y coordinate of the obstacle
     */
    public int getY() {
        return y;
    }

    /**
     * Returns a string representation of the obstacle, including its position.
     *
     * @return a string representation of the obstacle
     */
    @Override
    public String toString() {
        return "Obstacles: - At position (" + getX() + ", " + getY() + ")";
    }
}
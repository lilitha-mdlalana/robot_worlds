package org.communication.Infrastructure.obstacles;

//Represents a bottomless pit at a specific position on a grid.
public class BottomLessPit {
    private int x;
    private int y;

    /**
     * Constructs a bottomless pit with specified coordinates.
     * @param x The x-coordinate of the bottomless pit.
     * @param y The y-coordinate of the bottomless pit.
     */
    public BottomLessPit(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the bottomless pit.
     *
     * @return The x-coordinate of the bottomless pit.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the bottomless pit.
     *
     * @return The y-coordinate of the bottomless pit.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns a string representation of the bottomless pit, including its position.
     *
     * @return A string representation of the bottomless pit.
     */
    @Override
    public String toString() {
        return "Bottomless Pit: - At position (" + getX() + ", " + getY() + ")";
    }
}

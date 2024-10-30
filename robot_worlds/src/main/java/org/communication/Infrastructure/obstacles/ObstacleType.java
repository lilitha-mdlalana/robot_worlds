package org.communication.Infrastructure.obstacles;

public class ObstacleType {

    private String direction;
    private String type;
    private int distance;

    /**
     * Constructs an ObstacleType with the specified direction, type, and distance.
     *
     * @param direction the direction of the obstacle
     * @param type the type of the obstacle
     * @param distance the distance to the obstacle
     */
    public ObstacleType(String direction, String type, int distance) {
        this.direction = direction;
        this.type = type;
        this.distance = distance;
    }

    // Getter and setter methods

    /**
     * Returns the direction of the obstacle.
     *
     * @return the direction of the obstacle
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the direction of the obstacle.
     *
     * @param direction the new direction of the obstacle
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Returns the type of the obstacle.
     *
     * @return the type of the obstacle
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the obstacle.
     *
     * @param type the new type of the obstacle
     */

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the distance to the obstacle.
     *
     * @return the distance to the obstacle
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Sets the distance to the obstacle.
     *
     * @param distance the new distance to the obstacle
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }
}

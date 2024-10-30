package org.communication.Infrastructure.world;


/**
 * Configuration class for the game world.
 * Contains default world boundaries, visibility range, reload time, and repair time.
 */
public class Config {
    public static int DEFAULT_PORT = 5000;
    public static int DEFAULT_WORLD_SIZE = 100;
    public static int topLeftX_world = -DEFAULT_WORLD_SIZE;
    public static int topLeftY_world = DEFAULT_WORLD_SIZE;
    public static int bottomRightX_world = DEFAULT_WORLD_SIZE;
    public static int bottomRightY_world = -DEFAULT_WORLD_SIZE;
    public static int visibility = 50;
    public static int reloadTime = 5000;
    public static int repairTime = 5000;

    /**
     * Getter for the top-left X coordinate of the world.
     *
     * @return The top-left X coordinate of the world.
     */
    public static int getTopLeftX_world() {
        return topLeftX_world;
    }

    /**
     * Setter for the top-left X coordinate of the world.
     *
     * @param value The new value for the top-left X coordinate of the world.
     */
    public static void setTopLeftX_world(int value) {
        topLeftX_world = value;
    }

    /**
     * Getter for the top-left Y coordinate of the world.
     *
     * @return The top-left Y coordinate of the world.
     */
    public static int getTopLeftY_world() {
        return topLeftY_world;
    }

    /**
     * Setter for the top-left Y coordinate of the world.
     *
     * @param value The new value for the top-left Y coordinate of the world.
     */
    public static void setTopLeftY_world(int value) {
        topLeftY_world = value;
    }

    /**
     * Getter for the bottom-right X coordinate of the world.
     *
     * @return The bottom-right X coordinate of the world.
     */
    public static int getBottomRightX_world() {
        return bottomRightX_world;
    }

    /**
     * Setter for the bottom-right X coordinate of the world.
     *
     * @param value The new value for the bottom-right X coordinate of the world.
     */
    public static void setBottomRightX_world(int value) {
        bottomRightX_world = value;
    }

    /**
     * Getter for the bottom-right Y coordinate of the world.
     *
     * @return The bottom-right Y coordinate of the world.
     */
    public static int getBottomRightY_world() {
        return bottomRightY_world;
    }

    /**
     * Setter for the bottom-right Y coordinate of the world.
     *
     * @param value The new value for the bottom-right Y coordinate of the world.
     */
    public static void setBottomRightY_world(int value) {
        bottomRightY_world = value;
    }

    /**
     * Getter for the visibility range.
     *
     * @return The visibility range.
     */
    public static int getVisibility() {
        return visibility;
    }

    /**
     * Setter for the visibility range.
     *
     * @param value The new value for the visibility range.
     */
    public static void setVisibility(int value) {
        visibility = value;
    }

    /**
     * Getter for the reload time.
     *
     * @return The reload time.
     */
    public static int getReloadTime() {
        return reloadTime;
    }

    /**
     * Setter for the reload time.
     *
     * @param value The new value for the reload time.
     */
    public static void setReloadTime(int value) {
        reloadTime = value;
    }

    /**
     * Getter for the repair time.
     *
     * @return The repair time.
     */
    public static int getRepairTime() {
        return repairTime;
    }

    /**
     * Setter for the repair time.
     *
     * @param value The new value for the repair time.
     */
    public static void setRepairTime(int value) {
        repairTime = value;
    }

    /**
     * Return the default world size
     */
    public static int getWorldSize() {
        return DEFAULT_WORLD_SIZE;
    }
}

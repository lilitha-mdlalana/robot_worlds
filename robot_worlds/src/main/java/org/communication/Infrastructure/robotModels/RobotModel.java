package org.communication.Infrastructure.robotModels;

/**
 * The RobotModel interface defines the common methods that all robot models should implement.
 */
public interface RobotModel {
    /**
     * Returns the initial number of shields for the robot model.
     *
     * @return The initial number of shields as a String.
     */
    String getShield();

    /**
     * Returns the initial number of shots for the robot model.
     *
     * @return The initial number of shots as a String.
     */
    String getShots();
}
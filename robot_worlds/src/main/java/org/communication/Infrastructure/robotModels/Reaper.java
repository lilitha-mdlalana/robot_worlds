package org.communication.Infrastructure.robotModels;

/**
 * The Reaper class represents a robot model with specific initial values for shields and shots.
 */
public class Reaper implements RobotModel{
    private String shield; // Initial number of shields as a String
    private String shots; // Initial number of shots as a String

    /**
     * Constructor to initialize the Reaper robot with default shield and shots values.
     */
    public Reaper() {
        shield = "15";
        shots = "15";
    }

    /**
     * Get the initial number of shields.
     *
     * @return The initial number of shields as a String.
     */
    public String getShield() {
        return shield;
    }

    /**
     * Get the initial number of shots.
     *
     * @return The initial number of shots as a String.
     */
    public String getShots() {
        return shots;
    }
}

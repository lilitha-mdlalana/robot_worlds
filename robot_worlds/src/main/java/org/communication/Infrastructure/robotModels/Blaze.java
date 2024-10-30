package org.communication.Infrastructure.robotModels;

/**
 * The Blaze class represents a robot model with specific initial values for shields and shots.
 */
public class Blaze implements RobotModel{
    private String shield; // Initial number of shields as a String
    private String shots; // Initial number of shots as a String

    /**
     * Constructor to initialize the Blaze robot with default shield and shots values.
     */
    public Blaze() {
        shield = "16";
        shots = "20";
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

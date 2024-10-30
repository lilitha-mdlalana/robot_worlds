package org.communication.Infrastructure.robotModels;

/**
 * The Venom class represents a robot model with specific initial values for shields and shots.
 */
public class Venom implements RobotModel{
    private String shield; // Initial number of shields as a String
    private String shots; // Initial number of shots as a String

    /**
     * Constructor to initialize the Venom robot with default shield and shots values.
     */
    public Venom() {
        shield = "20";
        shots = "16";
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

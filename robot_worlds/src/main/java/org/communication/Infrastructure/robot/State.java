package org.communication.Infrastructure.robot;

public class State {
    private String position;
    private Direction direction;
    private int shields; // number of hits the shield can absorb
    private int shots;// number of shots left in the gun
    private String status;

    /**
     * Constructor to initialize the state with a number of shots.
     *
     * @param shots The initial number of shots.
     */
    public State(int shots){
        this.shots = shots;
    }

    /**
     * Constructor to initialize the state with shields and shots.
     *
     * @param shields The initial number of shields.
     * @param shots The initial number of shots.
     */
    public State(int shields, int shots){
        this.shields = shields;
        this.shots = shots;
        this.status = "NORMAL";
    }

    /**
     * Decrement the number of shots by 1. Ensures that shots do not go below 0.
     */
    public void decrementShots(){
        if (this.shots > 0){
            this.shots-=1;
        }else{
            this.shots = 0;
        }

    }

    /**
     * Decrement the number of shields by 1. Ensures that shields do not go below 0.
     */
    public void decrementShield(){
        if (this.shields > 0){
            this.shields-=1;
        }else{
            this.shields = 0;
        }

    }

    /**
     * Decrement the number of shields by a specified amount. Ensures that shields do not go below 0.
     *
     * @param amount The amount by which to decrement the shields.
     */
    public void decrementShieldBy(int amount) {
        this.shields = Math.max(0, this.shields - amount);
    }

    /**
     * Get the position of the robot.
     *
     * @return The position of the robot in the format "[x,y]".
     */
    public String getPosition() {
        return position;
    }

    /**
     * Set the position of the robot.
     *
     * @param position The position to set in the format "[x,y]".
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Get the current direction of the robot.
     *
     * @return The current direction of the robot.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Set the direction of the robot.
     *
     * @param direction The direction to set.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Get the number of shields.
     *
     * @return The number of shields.
     */
    public int getShields() {
        return shields;
    }

    /**
     * Set the number of shields.
     *
     * @param shields The number of shields to set.
     */
    public void setShields(int shields) {
        this.shields = shields;
    }

    /**
     * Get the number of shots.
     *
     * @return The number of shots.
     */
    public int getShots() {
        return shots;
    }

    /**
     * Set the number of shots.
     *
     * @param shots The number of shots to set.
     */
    public void setShots(int shots) {
        this.shots = shots;
    }

    /**
     * Get the status of the robot.
     *
     * @return The status of the robot.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status of the robot.
     *
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}

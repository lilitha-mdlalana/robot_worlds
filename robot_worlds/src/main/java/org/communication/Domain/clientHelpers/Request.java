package org.communication.Domain.clientHelpers;

import org.communication.Infrastructure.robot.Robot;

import com.google.gson.Gson;

/**
 * The Request class represents a command request for a robot, including
 * the robot's name, the command to be executed, the arguments for the command,
 * and the robot object itself.
 */
public class Request {
    private String robotName;
    private String command;
    private String[] arguments;
    private Robot robot;

    //Constructs an empty Request.
    public Request(){
    }


    /**
     * Constructs a new Request with the specified robot name and command.
     *
     * @param robotName The name of the robot.
     * @param command The command to be executed.
     */
    public Request(String robotName, String command){
        this.robotName = robotName;
        this.command = command;
    }


    /**
     * Constructs a new Request with the specified robot name, command,
     * arguments, and robot.
     *
     * @param robotName The name of the robot.
     * @param command The command to be executed.
     * @param arguments The arguments for the command.
     */
    public Request(String robotName, String command, String[] arguments){
        this.robotName = robotName;
        this.command = command;
        this.arguments = arguments;
    }


    // Getter and setter methods
    public String getRobotName() {
        return robotName;
    }


    //Sets the name of the robot.
    public void setRobot(String robot) {
        this.robotName = robot;
    }


    /**
     * Gets the command to be executed.
     * @return The command to be executed.
     */
    public String getCommand() {
        return command;
    }


    /**
     * Sets the command to be executed.
     * @param command The new command to be executed.
     */
    public void setCommand(String command) {
        this.command = command;
    }


    /**
     * Gets the arguments for the command.
     * @return The arguments for the command.
     */
    public String[] getArguments() {
        return arguments;
    }


    /**
     * Sets the arguments for the command.
     * @param arguments The new arguments for the command.
     */
    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }


    /**
     * Launches a robot with the specified name and command, and returns the
     * request as a JSON string.
     * @param robotName The name of the robot to launch.
     * @param command The command to be executed by the robot.
     * @return A JSON string representing the request.
     */
    public String launchRobot(String robotName, String command){
        Gson gson = new Gson();
        this.setRobot(robotName);
        this.setCommand(command);
        return gson.toJson(this);
    }


    /**
     * Commands a robot with the specified name, command, and arguments, and returns the
     * request as a JSON string.
     * @param robotName The name of the robot to command.
     * @param command The command to be executed by the robot.
     * @param arguments The arguments for the command.
     * @return A JSON string representing the request.
     */
    public String commandRobot(String robotName, String command, String[] arguments){
        Gson gson = new Gson();
        this.setRobot(robotName);
        this.setCommand(command);
        this.setArguments(arguments);
        return gson.toJson(this);
    }


    /**
     * Returns a string representation of the Request object.
     * @return A string representation of the Request object.
     */
    @Override
    public String toString() {
        return "RequestMessage{" +
                "robot='" + robot + '\'' +
                ", command='" + command + '\'' +
                ", arguments=" + arguments +
                '}';
    }
}

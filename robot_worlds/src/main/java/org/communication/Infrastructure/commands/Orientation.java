package org.communication.Infrastructure.commands;

import org.communication.Infrastructure.robot.Robot;
/**
 * Represents a command to make a robot Orientation around.
 * Extends the Command class and overrides the execute method to handle the Orientation action.
 */
public class Orientation extends Command {

    //Executes the Orientation command on the target robot.
    @Override
    public boolean execute(Robot target) {
        target.setStatus(String.valueOf(target.getCurrentDirection()));
        return false; // Return false indicating the command execution result
    }

    //Constructs the Orientation command with the specified argument.
    public Orientation(String argument){
        super("orientation", argument);
    }

}

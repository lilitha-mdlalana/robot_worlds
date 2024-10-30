package org.communication.Infrastructure.commands;

import org.communication.Infrastructure.robot.Robot;

/**
 * Represents a command to turn a robot left.
 * Extends the Command class and overrides the execute method to handle the left turn action.
 */
public class LeftCommand extends Command {

    //Executes the left turn command on the target robot.
    @Override
    public boolean execute(Robot target) {

        target.updateDirection(false);
        target.setStatus("Done"); // Turn the robot left
        return true; // Set status message

    }

    //Constructs a left turn command with the specified argument.
    public LeftCommand(String argument) {
        super("left", argument);
    }


}
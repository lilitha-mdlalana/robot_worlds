package org.communication.Infrastructure.commands;

import org.communication.Infrastructure.robot.Robot;

public class RightCommand extends Command {
    /**
     * Command to turn the robot to the right.
     */
    @Override
    public boolean execute(Robot target) {
        /**
         * Executes the command to turn the robot to the right.
         *
         * @param target The robot on which to execute the command.
         * @return true indicating the command was successfully executed.
         */
        target.updateDirection(true);
        target.setStatus("Done");
        return true;
    }
    /**
     * Constructs a RightCommand with the specified argument.
     *
     * @param argument The argument for the right command.
     */
    public RightCommand(String argument){
        super("right", argument);
    }


}

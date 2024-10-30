package org.communication.Infrastructure.commands;

import org.communication.Infrastructure.robot.Robot;

/**
 * Represents a command to move a robot backwards by a specified number of steps.
 * Extends the Command class and overrides the execute method to handle the back movement.
 */
public class BackCommand extends Command {

    /**
     * Executes the back command to move the target robot backwards by a specified number of steps.
     * @param target The robot on which the command is executed.
     * @return true if the command execution is successful, false otherwise.
     */
    @Override
    public boolean execute(Robot target) {
        // Update the position of the target robot
        int nrSteps = Integer.parseInt(getArgument());
        if (target.updatePosition(-nrSteps)){
            target.setStatus("Done");
        } else if(target.positionCheck) {
            target.setStatus("Obstructed!");
        } else if(target.pathCheck) {
            target.setStatus("Obstructed!");
        }else{
            target.setStatus("At the Edge!");
        }
        return true; // Return true indicating successful execution of the command
    }

    /**
     * Represents a command to move a robot backwards by a specified number of steps.
     * Extends the Command class and initializes it with the command type and argument.
     * @param argument The argument specifying the number of steps to move backwards.
    */
    public BackCommand(String argument) {
        super("backward", argument);
    }
}

package org.communication.Infrastructure.commands;

import org.communication.Infrastructure.robot.Robot;

/**
 * Represents a command to move a robot forward by a specified number of steps.
 * Extends the Command class and overrides the execute method to handle the forward movement.
 */
public class ForwardCommand extends Command {

    // Update the position of the target robot
    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());
        if (target.updatePosition(nrSteps)){
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

     //Constructs a forward command with the specified argument.
    public ForwardCommand(String argument) {
        super("forward", argument);
    }
}


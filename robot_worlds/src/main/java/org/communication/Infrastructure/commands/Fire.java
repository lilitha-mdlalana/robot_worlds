package org.communication.Infrastructure.commands;

import org.communication.Infrastructure.robot.Robot;

/**
 * Represents a command to fire shots from a robot.
 * Extends the Command class and overrides the execute method to execute the firing action.
 */
public class Fire extends Command {
    public static Robot damagedRobot;

    //Executes the fire command to make the target robot fire shots.
    @Override
    public boolean execute(Robot target) {
        damagedRobot = target.fireShots();
        return false;
    }

     //Constructs a fire command with the specified argument.
    public Fire(String argument){
        super("fire", argument);
    }

}

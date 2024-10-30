package org.communication.Infrastructure.commands;

import org.communication.Database.SaveWorld;
import org.communication.Infrastructure.robot.Robot;
import org.communication.Infrastructure.world.World;

import java.util.Scanner;

public class SaveWorldCommand extends Command {

    public SaveWorldCommand() {
        super("save");
    }

    @Override
    public boolean execute(Robot target) {
        World world = World.getInstance();
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter a name for the world to save: ");
            String worldName = sc.nextLine();  // Get the world name from the user

            SaveWorld.getInstance();
            SaveWorld.saveWorld(world, worldName,false);  // Pass the world name to the save method
        }

        System.out.println("Your world has been saved successfully");

        return true;
    }
}

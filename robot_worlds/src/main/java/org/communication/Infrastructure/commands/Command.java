package org.communication.Infrastructure.commands;

import org.communication.Infrastructure.robot.Robot;

/**
 * Represents an abstract command that can be executed on a robot.
 * Provides basic properties and methods for command execution.
 */
public abstract class Command {
    private final String name;
    private String argument;

    public abstract boolean execute(Robot target);

    public Command(String name){
        this.name = name.trim().toLowerCase();
        this.argument = "";
    }
    //constructs a command with a specified name and argument
    public Command(String name, String argument) {
        this(name);
        this.argument = argument.trim();
    }
    public String getName() {                                                                    
        return name;
    }

    public String getArgument() {
        return this.argument;
    }

    /**
     * Creates a specific Command object based on the provided instruction.
     * @param instruction The instruction string specifying the command to create.
     * @return A Command object corresponding to the given instruction.
     * @throws IllegalArgumentException If the instruction does not match any supported command.
     */
    public static Command create(String instruction) {

        String[] args = instruction.toLowerCase().trim().split(" ");

        switch (args[0]){

            case "forward":
                return new ForwardCommand(args[1]);
            case "back":
                return new BackCommand(args[1]);
            case "right":
                return new RightCommand(args[0]);
            case "left":
                return new LeftCommand(args[0]);
            case "look":
                return new Look(args[0]);
            case "fire":
                return new Fire(args[0]);
            case "orientation":
                return new Orientation(args[0]);
            default:
                throw new IllegalArgumentException("Unsupported command: " + instruction);
        }
    }
}


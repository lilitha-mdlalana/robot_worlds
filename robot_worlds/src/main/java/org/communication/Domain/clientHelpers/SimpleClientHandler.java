package org.communication.Domain.clientHelpers;

import java.io.*;
import java.util.*;
import com.google.gson.*;

import org.communication.Client;
import org.communication.Infrastructure.robotModels.*;
import static org.communication.Domain.headers.DisplayHeaders.*;
import static org.communication.Domain.serverHandler.SimpleServer.validCommands;
import static org.communication.Infrastructure.world.Config.*;

/**
 * The SimpleClientHandler class handles client interactions, including
 * sending responses to the client, processing JSON data, and managing a list
 * of robot models.
 */
public class SimpleClientHandler {

    private PrintStream out;
    private Gson gson;
    private ArrayList<String> robotModels;
    private boolean robotLaunched;
    /**
     * Constructs a SimpleClientHandler with the specified PrintStream for output,
     * and initializes the Gson object and the list of robot models.
     *
     * @param out The PrintStream used for sending responses to the client.
     */
    public SimpleClientHandler(PrintStream out, Gson gson, ArrayList<String> robotModels) {
        this.out = out;
        this.gson = gson;
        this.robotModels = robotModels;
        this.robotLaunched = false;
    }

    /**
 * Handles user input commands from the scanner and processes them accordingly.
 * Commands include launching actions and handling valid commands based on input parts.
 */
    public void handleUserInput(Scanner sc) {
        while (Client.keepRunning) {
            String userInput = sc.nextLine().toLowerCase();
            String[] parts = userInput.split(" ");

            if (userInput.startsWith("quit")) {
                System.out.println(RED_BRIGHT + "Exiting game..." + RED_BRIGHT);
                System.exit(0);
            }

            try {
                if (!Client.repairing && !Client.reloading) {
                    if (parts[0].equalsIgnoreCase("launch") && parts.length == 3) {
                        handleLaunchCommand(parts);
                    } else if(robotLaunched){
                        if (validCommands.contains(parts[0])){
                            handleValidCommand(parts);
                        }else {
                            displayInvalidCommandMessage(userInput);
                        }
                    }else {
                        System.out.println(RED_BRIGHT + "Please launch a robot to start!" + RESET);
                    }
                } else {
                    System.out.println(Client.reloading ? YELLOW_BRIGHT + "Busy Reloading..." + RESET : YELLOW_BRIGHT + "Busy Repairing..." + RESET);
                }
            } catch (Exception e) {
                displayInvalidCommandMessage(userInput);
            }
        }
    }

    /**
 * Handles the launch command specified by the user input parts.
 * @param parts The parts of the user input command.
 * @throws Exception If there's an error during robot instantiation or method invocation.
 */
    private void handleLaunchCommand(String[] parts) throws Exception {
        //Check if the robot model exists in the list of robot models and if launch is allowed
        if (robotModels.contains(parts[1]) && Client.launchCount) {
            //Disable further launches
            Client.launchCount = false;
            robotLaunched = true; // Set the flag to indicate a robot has been launched
            // Create an instance of the robot
            Object robot = createRobotInstance(parts[1]);

            // If robot instance is successfully created
            if (robot != null) {
                // Invoke methods to get shield and shots information using reflection
                String shield = (String) robot.getClass().getMethod("getShield").invoke(robot);
                String shots = (String) robot.getClass().getMethod("getShots").invoke(robot);

                // Prepare arguments for the command
                String[] stringArgs = {parts[1], shield, shots};

                // Create a Request object with robot name, command, and arguments
                Request request = new Request(parts[2], parts[0], stringArgs);
                out.println(gson.toJson(request));
                out.flush();
            }
        } else {
            // Print error message if launch is not allowed or robot model is invalid
            System.out.println(RED_BRIGHT + "You have already launched or invalid robot model!" + RESET);
        }
    }

    /**
 * Handles a valid command based on the user input parts.
 * @param parts The parts of the user input command.
 */
    public void handleValidCommand(String[] parts) {
        // Create a new Request object
        Request request = new Request();

        // Set the command for the Request object
        request.setCommand(parts[0]);

        // Check if there is exactly one argument provided and it is not a valid command
        if (parts.length == 2 && !validCommands.contains(parts[1])) {
            // Set the argument for the Request object
            request.setArguments(new String[]{parts[1]});
        }
        // Convert Request object to JSON and send it to output
        out.println(gson.toJson(request));
        out.flush();
    }

    /**
 * Displays an invalid command message for the given user input.
 * If the user input starts with "help", it invokes the help menu method from the Client class.
 * @param userInput The user input that resulted in an invalid command.
 */
    private void displayInvalidCommandMessage(String userInput) {
        // Print an error message indicating invalid command or arguments
        System.out.println(RED_BRIGHT + "Invalid Command or Arguments. Try again or enter 'help'" + RESET);

        // Check if user input starts with "help"
        if (userInput.startsWith("help")) {
            Client.helpMenu();
        }
    }

    /**
 * Handles responses received from the server and processes them accordingly.
 * @param in The BufferedReader used to read server responses.
 * @throws IOException If there's an error reading from the BufferedReader.
 */
    public void handleServerResponse(BufferedReader in) throws IOException {
        String serverResponse;
        while ((serverResponse = in.readLine()) != null) {
            if (serverResponse.equals("quit")) {
                System.out.println(YELLOW_BRIGHT + "Game Over, bye!" + RESET);
                Client.keepRunning = false;
                System.exit(0);
                break;
            }
            if (serverResponse.equals("shot")) {
                System.out.println(YELLOW_BRIGHT + "You have been killed by another robot!!!" + RESET);
                System.out.println(YELLOW_BRIGHT + "Game Over, bye!" + RESET);
                Client.keepRunning = false;
                System.exit(0);
                break;
            }
            if (serverResponse.equals("pit")) {
                System.out.println(YELLOW_BRIGHT + "You have fallen into a bottomless pit!!!" + RESET);
                System.out.println(YELLOW_BRIGHT + "Game Over, bye!" + RESET);
                Client.keepRunning = false;
                System.exit(0);
                break;
            }
            if (serverResponse.contains("Too many of you in this world")) {
                Client.launchCount = true; // Reset the launchCount flag to allow launching a new robot
            }
            if (serverResponse.contains("Reloading Complete!")) {
                handleReloading();
            }
            if (serverResponse.contains("Repairing Complete!")) {
                handleRepairing();
            }
            displayServerResponse(serverResponse);
            System.out.println(BLUE_BRIGHT + "____________________________________________________" + RESET);
            System.out.println(RED_BRIGHT + "Hey Playa! Whats your next move ... ?" + RESET);
        }
    }

    /**
 * Handles the reloading process by setting the reloading flag,
 * waiting for a specified reload time, and then clearing the reloading flag.
 */
    private void handleReloading() {
        Client.reloading = true;
        try {
            // Simulate reloading time by sleeping the current thread
            Thread.sleep(reloadTime);
        } catch (InterruptedException e) {
            // Print stack trace if interrupted during sleep
            e.printStackTrace();
        }
        Client.reloading = false;
    }

    /**
 * Handles the repairing process by setting the repairing flag,
 * waiting for a specified repair time, and then clearing the repairing flag.
 */
    private void handleRepairing() {
        Client.repairing = true;
        try {
            // Simulate repair time by sleeping the current thread
            Thread.sleep(repairTime);
        } catch (InterruptedException e) {
            // Print stack trace if interrupted during sleep
            e.printStackTrace();
        }
        Client.repairing = false;
    }


    /**
 * Creates an instance of a robot based on the provided model name.
 * @param model The name of the robot model to instantiate.
 * @return An instance of the specified robot model, or null if the model is invalid.
 * @throws Exception If there's an error instantiating the robot.
 */
    private Object createRobotInstance(String model) throws Exception {
        switch (model.toLowerCase()) {
            case "warpath":
                return new Warpath();
            case "venom":
                return new Venom();
            case "reaper":
                return new Reaper();
            case "demolisher":
                return new Demolisher();
            case "blaze":
                return new Blaze();
            default:
                // Print error message for invalid robot model
                System.out.println("Invalid robot model: " + model);
                return null;
        }
    }

    /**
 * Displays the server response by forwarding it to the Client's display method.
 * @param serverResponse The response received from the server to display.
 */
    private void displayServerResponse(String serverResponse) {
        // Forward the server response to the Client's display method
        Client.displayServerResponse(serverResponse);
    }
}

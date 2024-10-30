package org.communication;

import java.net.*;

import static org.communication.Domain.headers.DisplayHeaders.*;

import java.io.*;
import java.util.*;
import com.google.gson.*;

import org.communication.Domain.clientHelpers.SimpleClientHandler;
import org.communication.Domain.headers.DisplayHeaders;
import org.communication.Domain.headers.ServerResponseHandler;
import org.communication.Infrastructure.world.Config;

//Represents a client application with state variables related to its operation.

public class Client {

    public static boolean keepRunning = true;
    public static boolean launchCount = true;
    public static boolean reloading = false;
    public static boolean repairing = false;

    //Displays the help menu by invoking the helpMenu method from the DisplayHeaders class.
    public static void helpMenu() {
        DisplayHeaders.helpMenu();
    }

    /**
 * Displays the server response by invoking the displayServerResponse method from the DisplayHeaders class.
 * This method facilitates the display of server responses in the application's user interface.
 * @param serverResponse The response received from the server to display.
 */
    public static void displayServerResponse(String serverResponse) {
        ServerResponseHandler.displayServerResponse(serverResponse);
    }

    /**
 * Main entry point for the client application.
 * Establishes a connection to the server, initializes necessary components,
 * and manages user input and server responses using a multi-threaded approach.
 *
 * @param args Command-line arguments (not used in this implementation).
 * @throws IOException If there's an error during socket communication or I/O operations.
 */
    public static void main(String[] args) throws IOException {

        // Initialize scanner for user input, Gson for JSON serialization, and list of robot models
        Scanner sc = new Scanner(System.in);
        Gson gson = new Gson();
        ArrayList<String> robotModels = new ArrayList<>(Arrays.asList("warpath", "demolisher", "reaper", "venom", "blaze"));

        try (
                // Establish socket connection with the server, and initialize input and output streams
                Socket socket = new Socket("localhost", Config.DEFAULT_PORT);
                PrintStream out = new PrintStream(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            displayHeaderRobot();
            displayRobotStats();

            // Create instance of SimpleClientHandler to handle communication with the server
            SimpleClientHandler handler = new SimpleClientHandler(out, gson, robotModels);

            // Start a new thread to handle user input
            Thread inputThread = new Thread(() -> handler.handleUserInput(sc));
            inputThread.start();

            handler.handleServerResponse(in);

            inputThread.interrupt();

        } catch (IOException e) {
             // Handle connection-related exceptions
            if (e instanceof ConnectException) {
                System.out.println(RED_BRIGHT + "No Server found... Please run the server!" + RESET);
            } else {
                e.printStackTrace();
            }
        }
    }

}

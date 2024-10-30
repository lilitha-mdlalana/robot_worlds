package org.communication;

import org.communication.Domain.serverHandler.MultiServerHandler;
import org.communication.Infrastructure.obstacles.Obstacle;
import org.communication.Infrastructure.world.*;

import static org.communication.Domain.headers.DisplayHeaders.displayHeader;
import static org.communication.Domain.headers.DisplayHeaders.displayMenu;
import static org.communication.Domain.headers.DisplayHeaders.displayWaitingForConnections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The main server class responsible for initializing the server, handling user input,
 * and managing client connections.
 */
public class Server {

    public static boolean flag;
    public static List<Socket> socketList = new ArrayList<>();

    /**
     * Main method to start the server.
     *
     * @param args Command-line arguments.
     * @throws IOException If an I/O error occurs while creating the server socket or handling connections.
     */
    public static void main(String[] args) throws IOException {
        ConfigParser configParser = new ConfigParser(args);

        // Set the configurations
        int port = configParser.getPort();
        int worldSize = configParser.getWorldSize();
        List<Obstacle> obstacles = configParser.getObstacles();

        // Use default configuration if no arguments are provided
        if (configParser.isDefaultConfig()) {
            port = Config.DEFAULT_PORT;
            worldSize = 100;  // Or whatever the default world size is
            obstacles = World.createRandomObstacles();
        }

        configureWorld(worldSize);

        Scanner sc = new Scanner(System.in);
        displayHeader();
        displayMenu();

        // Create a handler for managing multiple server-related tasks
        MultiServerHandler handler = new MultiServerHandler(socketList);

        handler.handleWorldConfiguration(sc);

        // Open a server socket on the predefined port
        ServerSocket serverSocket = new ServerSocket(port);
        displayWaitingForConnections();
        flag = false;

        // Start a separate thread to handle user input while accepting client connections
        handler.startUserInputThread(sc);
        handler.acceptClientConnections(serverSocket);
    }

    // Configures world size
    private static void configureWorld(int size) {
        Config.setTopLeftX_world(-size);
        Config.setTopLeftY_world(size);
        Config.setBottomRightX_world(size);
        Config.setBottomRightY_world(-size);
    }
}

package org.communication.Infrastructure.world;

import org.communication.Domain.headers.DisplayHeaders;
import org.communication.Infrastructure.obstacles.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class ConfigParser {

    private int port;
    private int worldSize;
    private List<Obstacle> obstacles;

    /**
     * Handles parsing the configuration parameters from the command line arguments.
     * This  method is called by the Server file should the server be started from the command line.
     */
    public ConfigParser(String[] args) {
        // Set default values
        this.port = Config.DEFAULT_PORT;
        this.worldSize = 1;
        this.obstacles = new ArrayList<>();

        // Parse command-line arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-p":
                    port = Integer.parseInt(args[++i]);
                    break;
                case "-s":
                    worldSize = Integer.parseInt(args[++i]);
                    break;
                case "-o":
                    if (i + 1 < args.length) {
                        String[] obstacleParams = args[++i].split(",");
                        int x = Integer.parseInt(obstacleParams[0]);
                        int y = Integer.parseInt(obstacleParams[1]);
                        String type = "Obstacle";
                        if (obstacleParams.length == 3) {
                            type = obstacleParams[2];
                        }
                        obstacles.add(new Obstacle(x, y));
                    }
                    break;
                case "-h":
                case "--help":
                    DisplayHeaders.printHelp();
                    System.exit(0); // Exit after displaying help
                    break;
                default:
                    System.out.println("Unknown argument: " + args[i]);
                    DisplayHeaders.printHelp();
                    System.exit(1); // Exit on unknown argument
            }
        }
    }

    /**
     * Get's the configured port number or default port
     */
    public int getPort() {
        return port;
    }
    
    /**
     * Get's the configured world size or default world size
     */
    public int getWorldSize() {
        return worldSize;
    }

    /**
     * Get's the obstacles
     */
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public boolean isDefaultConfig() {
        return port == Config.DEFAULT_PORT && worldSize == 1 && obstacles.isEmpty();
    }
}

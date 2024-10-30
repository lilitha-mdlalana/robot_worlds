package org.communication.WebAPI;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.communication.Database.RestoreWorld;
import org.communication.Database.TestDataBaseOperations;
import org.communication.Infrastructure.obstacles.Obstacle;
import org.communication.Infrastructure.robot.Robot;
import org.communication.Infrastructure.world.World;
import org.communication.Domain.serverHandler.SimpleServer;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RobotWorldApi {

    public static void getCurrentWorld(Context ctx) {
        World world = World.getInstance();

        Map<String, Object> worldData = new HashMap<>();

        if (world != null) {


            // Create a list to hold the combined data for obstacles
            List<Map<String, Object>> obstacles = new ArrayList<>();

            // Get the list of obstacles and their corresponding types
            List<Object> obstacleList = world.getObstacles();

            for (Object obstacle : obstacleList) {
                Map<String, Object> obstacleData = new HashMap<>();
                obstacleData.put("obstacleType", world.getObstacleType(obstacle));

                // Assuming the obstacle has x and y coordinates
                obstacleData.put("x", world.getX(obstacle));
                obstacleData.put("y", world.getY(obstacle));

                obstacles.add(obstacleData);
            }

            worldData.put("obstacles", obstacles);
            worldData.put("name", "currentWorld");
            worldData.put("worldSize", world.getWorldSize());
        }

        ctx.json(worldData);
    }

    public static void restoreSavedWorld(Context ctx){
        String worldName = ctx.pathParam("world");
        RestoreWorld.restoreWorld(worldName,false);

        World world = World.getInstance();

        Map<String, Object> worldData = new HashMap<>();

        if (world != null) {


            // Create a list to hold the combined data for obstacles
            List<Map<String, Object>> obstacles = new ArrayList<>();

            // Get the list of obstacles and their corresponding types
            List<Object> obstacleList = world.getObstacles();

            for (Object obstacle : obstacleList) {
                Map<String, Object> obstacleData = new HashMap<>();
                obstacleData.put("obstacleType", world.getObstacleType(obstacle));

                // Assuming the obstacle has x and y coordinates
                obstacleData.put("x", world.getX(obstacle));
                obstacleData.put("y", world.getY(obstacle));

                obstacles.add(obstacleData);
            }

            worldData.put("obstacles", obstacles);
            worldData.put("name", worldName);
            worldData.put("worldSize", world.getWorldSize());
        }
        ctx.json(worldData);
    }

    public static void addRobot(Context ctx) {
        // Extract the robot data from the request body
        Map<String, String> requestBody = ctx.bodyAsClass(Map.class);
        String robotName = requestBody.get("name");
        String robotType = requestBody.get("type"); // Assume type is also sent in the request

        if (robotName != null && robotType != null) {
            // Create a new Robot object
            Robot newRobot = new Robot(robotName, robotType);

            // Add the robot name and object to the respective lists
            SimpleServer.robotNames.add(robotName);
            SimpleServer.robotObjects.add(newRobot);

            // Prepare the response body
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("robot", newRobot);  // You might need to serialize it
            responseBody.put("command", "launch");

            // Create the 'arguments' array with kind, shield strength, and shots
            List<Object> arguments = new ArrayList<>();
            arguments.add(robotType);  // 'kind'
            arguments.add(3);
            arguments.add(5);


            responseBody.put("arguments", arguments);

            // Respond with a success message
            ctx.status(200).json(responseBody);
        } else {
            // Handle missing parameters
            ctx.status(400).result("Robot name and type are required.");
        }
    }
}



//////////////////////////

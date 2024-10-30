package org.communication.Domain.serverHandler;

import static org.communication.Domain.headers.DisplayHeaders.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.*;

import org.communication.Domain.clientHelpers.Request;
import org.communication.Domain.response.Response;
import org.communication.Infrastructure.commands.Command;
import org.communication.Infrastructure.commands.Fire;
import org.communication.Infrastructure.obstacles.*;
import org.communication.Infrastructure.robot.Robot;
import org.communication.Infrastructure.robot.State;
import org.communication.Infrastructure.robotModels.*;
import org.communication.Infrastructure.world.Config;
import org.communication.Infrastructure.world.World;

public class SimpleServer implements Runnable {
    /**
     * Default port number for the server.
     */
    public static final int PORT = Config.DEFAULT_PORT;
    private final BufferedReader in;
    private final PrintStream out;
    public static ArrayList<String> robotNames = new ArrayList<>(); // ArrayList to store robot names
    public static ArrayList<String> validCommands = new ArrayList<>(Arrays.asList("forward", "back", "look", "turn", "state", "fire", "orientation", "reload", "repair")); //ArrayList to store robots valid commands
    public static ArrayList<String> turns = new ArrayList<>(Arrays.asList("left", "right")); //ArrayList to store robots valid commands
    public static CopyOnWriteArrayList<Robot> robotObjects = new CopyOnWriteArrayList<>();
    public static Map<String, Socket> listOfRobotSockets = new HashMap<>();
    Gson gson = new Gson();
    public Socket socket;

    /**
     * Constructor for initializing the server with a socket.
     *
     * @param socket The socket used for communication.
     * @throws IOException If there's an issue with socket input or output streams.
     */
    public SimpleServer(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Main execution logic of the server.
     */
    public void run() {
        World world = World.getInstance();
        Robot robot = null;
        String messageFromClient;

        try{
            while ((messageFromClient = in.readLine()) != null) {

                try{
                    Request request = gson.fromJson(messageFromClient, Request.class);

                    if (request.getCommand().equals("launch")) {
                        launchRobot(request,robot,world);

                    }else if (validCommands.contains(request.getCommand()) && !request.getCommand().equals("look") && !request.getCommand().equals("state") && !request.getCommand().equals("fire") && !request.getCommand().equals("orientation") && !request.getCommand().equals("reload") && !request.getCommand().equals("repair")) {

                        try {
                            if (!turns.contains(request.getArguments()[0])) {
                                String newRobotCommand = request.getCommand() + " " + request.getArguments()[0];
                                Command command = Command.create(newRobotCommand);
                                robot.handleCommand(command);
                                String jsonToClient = successfulResponse(robot, gson, "OK", robot.getState().getShields(), robot.getState().getShots());
                                out.println(jsonToClient);

                            } else if (turns.contains(request.getArguments()[0])) {
                                String newRobotCommand = request.getArguments()[0];
                                Command command = Command.create(newRobotCommand);
                                robot.handleCommand(command);
                                String jsonToClient = successfulResponse(robot, gson, "OK", robot.getState().getShields(), robot.getState().getShots());
                                out.println(jsonToClient);

                            } else {
                                String errorResponse = errorResponse(robot, gson, "ERROR", "Could not parse arguments");
                                out.println(errorResponse);
                            }

                        } catch (IllegalArgumentException | NullPointerException e) {
                            if (e instanceof ConcurrentModificationException){
                                System.out.println(RED_BRIGHT + "A player has been terminated..." + RESET);
                            }
                            String errorResponse = errorResponse(robot, gson, "ERROR", "Could not parse arguments.");
                            out.println(errorResponse);

                        }

                    }else if (validCommands.contains(request.getCommand()) && request.getCommand().equals("fire") && request.getArguments()==null){
                        Command command = Command.create(request.getCommand());
                        robot.handleCommand(command);

                        if (Fire.damagedRobot != null) {
                            // Robot hit something, send hit response
                            String jsonToClient = sendFireResponseHit(Fire.damagedRobot, robot, gson, robot.getState().getShields(), robot.getState().getShots());
                            out.println(jsonToClient);
                        } else {
                            // Robot missed, send miss response
                            String jsonToClient = sendFireResponseMiss(gson, robot.getState().getShots());
                            out.println(jsonToClient);
                        }

                    }else if (request.getCommand().equals("look") && validCommands.contains("look")) {
                        String newRobotCommand = request.getCommand();
                        Command command = Command.create(newRobotCommand);
                        robot.handleCommand(command);
                        String jsonToClient = successfulLookResponse(robot, gson, "OK", robot.getState().getShields(), robot.getState().getShots());
                        out.println(jsonToClient);

                    }else if (request.getCommand().equals("state") && validCommands.contains("state")) {
                        assert robot != null;
                        String jsonToClient = sendStateResponseToClient(robot, gson, robot.getState().getShields(), robot.getState().getShots());
                        out.println(jsonToClient);

                    }else if (request.getCommand().equals("orientation") && validCommands.contains("orientation")) {
                        assert robot != null;
                        String jasonToClient = sendOrientationResponseToClient(robot, gson);
                        out.println(jasonToClient);

                    }else if (request.getCommand().equals("reload") && validCommands.contains("reload")){
                        assert robot != null;
                        String jasonToClient = reloadResponse(robot, gson);
                        out.println(jasonToClient);

                    }else if (request.getCommand().equals("repair") && validCommands.contains("repair")){
                        assert robot != null;
                        String jasonToClient = repairResponse(robot, gson);
                        out.println(jasonToClient);

                    }else {
                        String errorResponse = errorResponse(robot, gson, "ERROR", "Could not parse arguments");
                        out.println(errorResponse);
                    }

                }catch (IllegalArgumentException | NullPointerException e) {
                    String errorResponse = errorResponse(robot, gson, "ERROR", "Could not parse arguments.");
                    out.println(errorResponse);

                }catch (Exception e) {
                    System.out.println("invalid json received!");
                }
            }

        }catch (IOException e){
            if (e instanceof SocketException){
                System.out.println(RED_BRIGHT + "A player has been terminated... bye" + RESET);
            }else {
                e.printStackTrace();
            }
        }

    }

    public void launchRobot(Request request, Robot robot, World world) {
        String robotName = request.getRobotName();
        String robotType = request.getArguments()[0];

        if (!robotNames.contains(robotName)) {
            robot = new Robot(robotName, robotType);
            robot.setName(robotName);
            request.setRobot(robotName);
            robotNames.add(robotName);

            int shield = Integer.parseInt(request.getArguments()[1]);
            int shots = Integer.parseInt(request.getArguments()[2]);
            System.out.println(RED_BRIGHT + "\n" + request.getRobotName() + " joined the game..." + " at position " + robot.getPosition() + " " + RESET);

            String responseJson = sendResponsetoClient(robot, gson, shield, shots);
            out.println(responseJson);

            robotObjects.add(robot);
            world.obstacles.add(robot);
            listOfRobotSockets.put(robotName, socket);
        } else {
            String jsonToClient = errorResponse(robot, gson, "ERROR", "Too many of you in this world");
            out.println(jsonToClient);
        }
    }


    /**
     * Sends a response to the client indicating a successful fire action (miss).
     *
     * @param gson Gson object for JSON serialization.
     * @param shots Current shots available for the firing robot.
     * @return JSON string response indicating a miss.
     */
    private String sendFireResponseMiss( Gson gson, int shots){
        Map<String, Object> data = new HashMap<>();
        Response response = new Response();
        State state = new State(shots);
        if (shots == 0){
            data.put("message", "please reload bullets");
        }else{
            data.put("message", "Miss");
        }
        data.put("shots", state.getShots());
        response.setState(state);
        response.setData(data);

        return gson.toJson(response);

    }


    /**
     * Sends a response to the client indicating a successful fire action (hit).
     *
     * @param hitRobot The robot that was hit by the fire.
     * @param robot The robot initiating the fire.
     * @param gson Gson object for JSON serialization.
     * @param shields Current shield value of the firing robot.
     * @param shots Current shots available for the firing robot.
     * @return JSON string response indicating a hit.
     */
    private String sendFireResponseHit(Robot hitRobot, Robot robot, Gson gson, int shields, int shots) {
        Map<String, Object> data = new HashMap<>();
        Response response = new Response();

        response.setResult("OK");
        data.put("message", "Hit");
        data.put("distance", robot.getDistance());
        data.put("robot", hitRobot.getName());
        Map<String, Object> hitRobotState = new HashMap<>();
        hitRobotState.put("position", hitRobot.coordinatePosition());
        hitRobotState.put("direction", hitRobot.getCurrentDirection());
        if (hitRobot.getState().getShields() == 0){
            hitRobot.getState().setStatus("DEAD");
        }
        hitRobotState.put("shields", hitRobot.getState().getShields());
        hitRobotState.put("shots", hitRobot.getState().getShots());
        hitRobotState.put("status", hitRobot.getState().getStatus());
        data.put("state", hitRobotState);

        response.setData(data);
        State state = new State(shields, shots);
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        state.setStatus("NORMAL");
        response.setState(state);
        return gson.toJson(response);
    }

    /**
     * Sends a state response to the client.
     *
     * @param robot The robot for which the state is being sent.
     * @param gson Gson object for JSON serialization.
     * @param shield Current shield value of the robot.
     * @param shots Current shots available for the robot.
     * @return JSON string response containing the robot's state.
     */
    private String sendStateResponseToClient(Robot robot, Gson gson, int shield, int shots){
        Response response = new Response();
        // Create and set the state object
        State state = new State(shield, shots);
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        response.setState(state);
        return gson.toJson(response);
    }

    /**
     * Sends an orientation response to the client.
     *
     * @param robot The robot for which the orientation is being sent.
     * @param gson Gson object for JSON serialization.
     * @return JSON string response containing the robot's orientation.
     */
    private String sendOrientationResponseToClient(Robot robot, Gson gson){
        Response response = new Response();
        Map<String, Object> data = new HashMap<>();
        data.put("direction", robot.getCurrentDirection() );
        response.setData(data);
        // Create and set the state object
        return gson.toJson(response);
    }

    /**
     * Sends a general response to the client containing robot's status and other details.
     *
     * @param robot The robot for which the response is being sent.
     * @param gson Gson object for JSON serialization.
     * @param shield Current shield value of the robot.
     * @param shots Current shots available for the robot.
     * @return JSON string response containing the robot's status and details.
     */
    private String sendResponsetoClient(Robot robot, Gson gson, int shield, int shots){
        Response response = new Response();
        // Create and set the state object
        State state = new State(shield,shots);
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        robot.setState(state);
        state.setStatus("NORMAL");
        response.setState(state);

        response.setResult("OK");
        // create and set the data map
        Map<String, Object> data = new HashMap<>();
        data.put("position", robot.coordinatePosition());
        data.put("visibility", Config.getVisibility());
        data.put("reload", "5 seconds");
        data.put("repair", "5 seconds");
        data.put("shields", robot.getState().getShields());
        response.setData(data);
        return gson.toJson(response);
    }

    /**
     * Sends an error response to the client.
     *
     * @param robot The robot for which the error response is being sent.
     * @param gson Gson object for JSON serialization.
     * @param setResult Result type of the response (e.g., "ERROR").
     * @param message Error message to be sent.
     * @return JSON string error response.
     */
    private String errorResponse(Robot robot, Gson gson, String setResult, String message){
        Response response = new Response();
        response.setResult(setResult);
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        response.setData(data);
        return gson.toJson(response);

    }

    /**
     * Generates a successful response as a JSON string.
     *
     * @param robot      The Robot object representing the current state of the robot.
     * @param gson       The Gson object used for JSON serialization/deserialization.
     * @param setResult  The result message to be set in the response.
     * @param shield     The current shield value of the robot.
     * @param shots      The current number of shots available for the robot.
     * @return           The successful response as a JSON string.
     */
    private String successfulResponse(Robot robot, Gson gson, String setResult, int shield, int shots){
        Response response = new Response();
        response.setResult(setResult);
        Map<String, Object> data = new HashMap<>();
        data.put("message", robot.getStatus());
        response.setData(data);

        //Create and set the state object
        State state = new State(shield, shots);
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        state.setStatus("NORMAL");
        response.setState(state);

        return gson.toJson(response);

    }

    /**
     * Generates a successful look response as a JSON string, including information about obstacles.
     *
     * @param robot      The Robot object representing the current state of the robot.
     * @param gson       The Gson object used for JSON serialization/deserialization.
     * @param setResult  The result message to be set in the response.
     * @param shield     The current shield value of the robot.
     * @param shots      The current number of shots available for the robot.
     * @return           The successful look response as a JSON string.
     */
    private String successfulLookResponse(Robot robot, Gson gson, String setResult, int shield, int shots) {
        // Create the response object
        Map<String, Object> data = new HashMap<>();
        ArrayList<ObstacleType> objects = new ArrayList<>();
        Response response = new Response();

        // Add North obstacles
        if (robot.obstacleSteps.containsKey("North_obstacle") && robot.obstacleSteps.get("North_obstacle") != 0) {
            objects.add(new ObstacleType("North", "obstacle", robot.obstacleSteps.get("North_obstacle")));
        }
        if (robot.obstacleSteps.containsKey("North_mountain") && robot.obstacleSteps.get("North_mountain") != 0){
            objects.add(new ObstacleType("North", "mountain", robot.obstacleSteps.get("North_mountain")));
        }
        if (robot.obstacleSteps.containsKey("North_robot") && robot.obstacleSteps.get("North_robot") != 0) {
            objects.add(new ObstacleType("North", "robot", robot.obstacleSteps.get("North_robot")));
        }

        // Add East obstacles
        if (robot.obstacleSteps.containsKey("East_obstacle") && robot.obstacleSteps.get("East_obstacle") != 0) {
            objects.add(new ObstacleType("East", "obstacle", robot.obstacleSteps.get("East_obstacle")));
        }
        if (robot.obstacleSteps.containsKey("East_mountain") && robot.obstacleSteps.get("East_mountain") != 0){
            objects.add(new ObstacleType("East", "mountain", robot.obstacleSteps.get("East_mountain")));
        }
        if (robot.obstacleSteps.containsKey("East_robot") && robot.obstacleSteps.get("East_robot") != 0) {
            objects.add(new ObstacleType("East", "robot", robot.obstacleSteps.get("East_robot")));
        }

        // Add South obstacles
        if (robot.obstacleSteps.containsKey("South_obstacle") && robot.obstacleSteps.get("South_obstacle") != 0) {
            objects.add(new ObstacleType("South", "obstacle", robot.obstacleSteps.get("South_obstacle")));
        }
        if (robot.obstacleSteps.containsKey("South_mountain") && robot.obstacleSteps.get("South_mountain") != 0){
            objects.add(new ObstacleType("South", "mountain", robot.obstacleSteps.get("South_mountain")));
        }
        if (robot.obstacleSteps.containsKey("South_robot") && robot.obstacleSteps.get("South_robot") != 0) {
            objects.add(new ObstacleType("South", "robot", robot.obstacleSteps.get("South_robot")));
        }

        // Add West obstacles
        if (robot.obstacleSteps.containsKey("West_obstacle") && robot.obstacleSteps.get("West_obstacle") != 0) {
            objects.add(new ObstacleType("West", "obstacle", robot.obstacleSteps.get("West_obstacle")));
        }
        if (robot.obstacleSteps.containsKey("West_mountain") && robot.obstacleSteps.get("West_mountain") != 0){
            objects.add(new ObstacleType("West", "mountain", robot.obstacleSteps.get("West_mountain")));
        }
        if (robot.obstacleSteps.containsKey("West_robot") && robot.obstacleSteps.get("West_robot") != 0) {
            objects.add(new ObstacleType("West", "robot", robot.obstacleSteps.get("West_robot")));
        }

        response.setResult(setResult);

        // Create the data map and populate it
        data.put("message", robot.getStatus());
        data.put("object", objects);
        response.setData(data);

        // Create and set the state object
        State state = new State(shield, shots);
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        state.setStatus("NORMAL");
        response.setState(state);

        // Convert response to JSON
        return gson.toJson(response);
    }

    /**
     * Generates a response for reloading the robot's shots.
     *
     * @param robot  The Robot object representing the current state of the robot.2
     * @param gson   The Gson object used for JSON serialization/deserialization.
     * @return       The reload response as a JSON string.
     */
    private String reloadResponse(Robot robot, Gson gson) {
        if (robot.getRobotType().equalsIgnoreCase("venom")){
            Venom venom = new Venom();
            int reloadShots = Integer.parseInt(venom.getShots());
            robot.getState().setShots(reloadShots);

        }else if (robot.getRobotType().equalsIgnoreCase("blaze")){
            Blaze blaze = new Blaze();
            int reloadShots = Integer.parseInt(blaze.getShots());
            robot.getState().setShots(reloadShots);

        }else if (robot.getRobotType().equalsIgnoreCase("demolisher")) {
            Demolisher demolisher = new Demolisher();
            int reloadShots = Integer.parseInt(demolisher.getShots());
            robot.getState().setShots(reloadShots);

        }else if (robot.getRobotType().equalsIgnoreCase("reaper")) {
            Reaper reaper = new Reaper();
            int reloadShots = Integer.parseInt(reaper.getShots());
            robot.getState().setShots(reloadShots);

        }else if (robot.getRobotType().equalsIgnoreCase("warpath")) {
            Warpath warpath = new Warpath();
            int reloadShots = Integer.parseInt(warpath.getShots());
            robot.getState().setShots(reloadShots);
        }

        Response response = new Response();
        response.setResult("Reloaded");
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Reloading Complete!");
        State state = new State(robot.getState().getShields(), robot.getState().getShots());
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        response.setState(state);
        response.setData(data);
        return gson.toJson(response);
    }

    /**
     * Generates a response for repairing the robot's shields.
     *
     * @param robot  The Robot object representing the current state of the robot.
     * @param gson   The Gson object used for JSON serialization/deserialization.
     * @return       The repair response as a JSON string.
     */
    private String repairResponse(Robot robot, Gson gson){
        if (robot.getRobotType().equalsIgnoreCase("venom")){
            Venom venom = new Venom();
            int repairShield = Integer.parseInt(venom.getShield());
            robot.getState().setShields(repairShield);
        }else if (robot.getRobotType().equalsIgnoreCase("blaze")){
            Blaze blaze = new Blaze();
            int repairShield = Integer.parseInt(blaze.getShield());
            robot.getState().setShields(repairShield);
        }else if (robot.getRobotType().equalsIgnoreCase("demolisher")) {
            Demolisher demolisher = new Demolisher();
            int repairShield = Integer.parseInt(demolisher.getShield());
            robot.getState().setShields(repairShield);
        }else if (robot.getRobotType().equalsIgnoreCase("reaper")) {
            Reaper reaper = new Reaper();
            int repairShield = Integer.parseInt(reaper.getShield());
            robot.getState().setShields(repairShield);
        }else if (robot.getRobotType().equalsIgnoreCase("warpath")) {
            Warpath warpath = new Warpath();
            int repairShield = Integer.parseInt(warpath.getShield());
            robot.getState().setShields(repairShield);
        }

        Response response = new Response();
        response.setResult("Repair");
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Repairing Complete!");
        State state = new State(robot.getState().getShields(), robot.getState().getShots());
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        response.setState(state);
        response.setData(data);
        return gson.toJson(response);

    }

}




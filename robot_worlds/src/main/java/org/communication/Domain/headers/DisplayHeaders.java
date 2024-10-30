package org.communication.Domain.headers;

import org.communication.Domain.serverHandler.SimpleServer;
import org.communication.Infrastructure.obstacles.BottomLessPit;
import org.communication.Infrastructure.obstacles.Lake;
import org.communication.Infrastructure.obstacles.Mountain;
import org.communication.Infrastructure.obstacles.Obstacle;
import org.communication.Infrastructure.robot.Position;
import org.communication.Infrastructure.robot.Robot;
import org.communication.Infrastructure.world.World;

import java.util.ArrayList;
import java.util.Iterator;

public class DisplayHeaders {

    // High Intensity Colour
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String RESET = "\033[0m";  // Text Reset
    public static ArrayList<Position> launchingSpots = new ArrayList<>();

    /**
     * Displays the world commands menu.
     */
    public static void viewMenu(){
        System.out.println(RED_BRIGHT + "\nWorld Commands" + RESET);
        System.out.println(BLUE_BRIGHT +"'quit'" + RESET + YELLOW_BRIGHT +" - Disconnects all robots and ends the world " + RESET);
        System.out.println(BLUE_BRIGHT +"'robots'" + RESET + YELLOW_BRIGHT +" - Lists all robots including the robot's name and state"+ RESET);
        System.out.println(BLUE_BRIGHT +"'view'" + RESET + YELLOW_BRIGHT +" - displays all the available/acceptable commands"+ RESET);
        System.out.println(BLUE_BRIGHT +"'dump'" + RESET + YELLOW_BRIGHT +" - Displays a representation of the worlds state"+ RESET);
        System.out.println(BLUE_BRIGHT +"'save'" + RESET + YELLOW_BRIGHT +" - Saves the world state"+ RESET);
        System.out.println(BLUE_BRIGHT +"'restore'" + RESET + YELLOW_BRIGHT +" - Restore the world state"+ RESET);
        System.out.println(BLUE_BRIGHT +"'world'" + RESET + YELLOW_BRIGHT +" - Display the constraints of the world\n"+ RESET);

    }

    /**
     * Displays the help menu with available server configurations.
     */
    public static void printHelp() {
        System.out.println("Usage: java -jar my-server.jar [-p port] [-s world_size] [-o x,y] [-h|--help]");
        System.out.println("Options:");
        System.out.println("  -p port         Specify the port number (default: 5000)");
        System.out.println("  -s world_size   Specify the size of the world (default: 1, i.e., 1x1)");
        System.out.println("  -o x,y          Specify obstacle position (default: none)");
        System.out.println("  -h, --help      Display this help message");
        System.out.println("Example:");
        System.out.println("  java -jar my-server.jar -p 5000 -s 100 -o 10,5");
    }

    /**
     * Displays the header for the robot connection.
     */
    public static void displayHeaderRobot(){
        System.out.println(RED_BRIGHT + "Connection Successful...\uD83D\uDE0A" + RESET);
        System.out.println(BLUE_BRIGHT + "\n*************************************************" + RESET);
        System.out.println(YELLOW_BRIGHT +"  🤖✨ Welcome to the Amazing Robot World! ✨🤖" + RESET);
        System.out.println(BLUE_BRIGHT + "*************************************************" + RESET);
    }

    /**
     * Displays the general header for the game.
     */
    public static void displayHeader(){
        System.out.println(BLUE_BRIGHT + "\n**********************************************************************" + RESET);
        System.out.println(YELLOW_BRIGHT+ "            🤖✨ Welcome to the Amazing Robot World! ✨🤖" + RESET);
        System.out.println(BLUE_BRIGHT + "**********************************************************************" + RESET);
    }

    /**
     * Displays a message indicating the server is running and waiting for connections.
     */
    public static void displayWaitingForConnections(){
        System.out.println(RED_BRIGHT + "\t\tServer running & waiting for client connections." + RESET);
        System.out.println(BLUE_BRIGHT + "----------------------------------------------------------------------" + RESET);
    }

    /**
     * Displays the help menu with available commands.
     */
    public static void helpMenu() {
        System.out.println(RED_BRIGHT + "\nHelp Menu" + RESET);
        System.out.println(BLUE_BRIGHT + "'launch'" +RESET + YELLOW_BRIGHT +" - launch a new robot into the world"+ RESET);
        System.out.println(BLUE_BRIGHT +"'look'" +RESET + YELLOW_BRIGHT +" - Allows your robot to look around"+ RESET);
        System.out.println(BLUE_BRIGHT +"'state'" +RESET + YELLOW_BRIGHT +"- View the current state of your robot\n"+ RESET);
        System.out.println(BLUE_BRIGHT +"'forward'"+RESET + YELLOW_BRIGHT +" - move the robot forward e.g forward 10 "+ RESET);
        System.out.println(BLUE_BRIGHT +"'back'" +RESET + YELLOW_BRIGHT +"- move the robot backwards e.g back 50"+ RESET);
        System.out.println(BLUE_BRIGHT +"'turn'"+RESET + YELLOW_BRIGHT + " - turn the robot either left or right e.g turn left"+ RESET);
        System.out.println(BLUE_BRIGHT +"'fire'" +RESET + YELLOW_BRIGHT +" - shoot your shot"+ RESET);
        System.out.println(BLUE_BRIGHT +"'orientation'" +RESET + YELLOW_BRIGHT +" - finds out which direction the robot is facing"+ RESET);
        System.out.println(BLUE_BRIGHT +"'quit'" +RESET + YELLOW_BRIGHT +" - terminates the connection from the server and shuts down the application"+ RESET);
    }



    /**
     * Displays the configuration menu for setting the world size.
     */
    public static void configMenu(){
        System.out.println(RED_BRIGHT + "\nLet's start by configuring the world size:"+ RESET);
        System.out.println(RED_BRIGHT + "Please choose an index below:" + RESET);
        System.out.println(YELLOW_BRIGHT + "*Hint*" + RESET + RED_BRIGHT + " - These are coordinates representing (x,y) values" + RESET);
        System.out.println(BLUE_BRIGHT + "1) (-100, 100) X (100, -100) -" + RESET + YELLOW_BRIGHT+ " EASY\n" +RESET +
                BLUE_BRIGHT +"2) (-200, 200) X (200, -200) -"+ RESET + YELLOW_BRIGHT+ " MEDIUM\n" + RESET +
                BLUE_BRIGHT +"3) (-300, 300) X (300, -300) -" + RESET + YELLOW_BRIGHT+ " HARD" + RESET ) ;
    }

    /**
     * Displays the initial game menu.
     */
    public static void displayMenu() {

        System.out.println(YELLOW_BRIGHT + "                   Welcome to your new robot world " +
                "\n        Crafted with a default size of 100 clicks by 100 clicks.\n" +
                "     Each robot has a standard 50-clicks-per-direction view radius!\n" +RESET);

        System.out.println(YELLOW_BRIGHT + "                    Want to customize your world? \n" +
                "               Type 'config' to tweak the settings, or\n" +
                "          Press 'Enter' to embark on your exciting adventure! \n" +
                "                          Press 'q' to quit" + RESET);
    }

    /**
     * Displays the robot statistics and available characters.
     */
    public static void displayRobotStats() {
        System.out.println(YELLOW_BRIGHT + "   \uD83D\uDE0AGet ready to start this exciting journey\uD83D\uDE0A\n" + RESET);
        System.out.println(YELLOW_BRIGHT + "launch a robot to start the game: " + RESET);
        System.out.println(YELLOW_BRIGHT + "\nChoose your Robot Character:" + RESET);
        System.out.println(YELLOW_BRIGHT + "Robot Stats:" + RESET);
        displayRobotStats("1) Blaze", 16, 20);
        displayRobotStats("2) Demolisher", 14, 20);
        displayRobotStats("3) Reaper", 15, 15);
        displayRobotStats("4) Venom", 20, 16);
        displayRobotStats("5) Warpath", 18, 16);
        System.out.println(YELLOW_BRIGHT + "\nTo join the game enter e.g 'launch reaper james'\n" + RESET);
    }

    /**
     * Displays the statistics for a specific robot.
     *
     * @param robotName The name of the robot.
     * @param shield The shield value of the robot.
     * @param shots The number of shots the robot has.
     */
    public static void displayRobotStats(String robotName, int shield, int shots) {
        System.out.println(RED_BRIGHT + robotName + ":" + RESET);
        System.out.println(BLUE_BRIGHT + "   Shield: " + RESET + generateVisualRepresentation(shield, '▧'));
        System.out.println(BLUE_BRIGHT + "   Shots:  " + RESET + generateVisualRepresentation(shots, '✪'));

    }

    /**
     * Generates a visual representation of a value using a specified symbol.
     *
     * @param value The value to be represented.
     * @param symbol The symbol used for the representation.
     * @return A string representing the visual representation.
     */
    public static String generateVisualRepresentation(int value, char symbol) {
        StringBuilder visualRepresentation = new StringBuilder();
        for (int i = 0; i < value; i++) {
            visualRepresentation.append(symbol);
        }
        return visualRepresentation.toString();
    }

    /**
     * Displays the obstacles and robots present in the world.
     */
    public static void displayObstaclesAndRobots(){
        World world = World.getInstance();
        System.out.println(RED_BRIGHT + "The world contains these types of obstacles:" + RESET);
        for (Object obs : world.obstacles){
            if (obs instanceof Obstacle obstacle) {
                String printObstacle =String.format(BLUE_BRIGHT + "- At position " + obstacle.getX() + ", " +  obstacle.getY() + " (to " +  (obstacle.getX()+4) + ", " + (obstacle.getY()+4) +") there is a " +RESET + YELLOW_BRIGHT + "obstacle"+ RESET);
                System.out.println(printObstacle);
            }else if (obs instanceof Mountain mountain) {
                String printObstacle =String.format(BLUE_BRIGHT +"- At position " + mountain.getX() + ", " +  mountain.getY() + " (to " +  (mountain.getX()+4) + ", " + (mountain.getY()+4) +") there is a "+RESET + YELLOW_BRIGHT + "mountain" + RESET);
                System.out.println(printObstacle);
            }else if (obs instanceof Lake lake) {
                String printObstacle =String.format(BLUE_BRIGHT +"- At position " + lake.getX() + ", " +  lake.getY() + " (to " +  (lake.getX()+4) + ", " + (lake.getY()+4) +") there is a "+RESET + YELLOW_BRIGHT + "lake" + RESET);
                System.out.println(printObstacle);
            }else if (obs instanceof BottomLessPit bottomLessPit){
                String printObstacle =String.format(BLUE_BRIGHT +"- At position " + bottomLessPit.getX() + ", " +  bottomLessPit.getY() + " (to " +  (bottomLessPit.getX()+4) + ", " + (bottomLessPit.getY()+4) +") there is a "+RESET + YELLOW_BRIGHT + "bottomless pit" + RESET);
                System.out.println(printObstacle);
            }
        }
        System.out.println(RED_BRIGHT + "\nNumber of robots in the world : " +RESET + YELLOW_BRIGHT + SimpleServer.robotObjects.size() + RESET);
        if (!SimpleServer.robotObjects.isEmpty()) {
            for (Robot robot: SimpleServer.robotObjects) {
                System.out.println(BLUE_BRIGHT + "Bot " + RESET + YELLOW_BRIGHT +  robot.getName() + RESET + BLUE_BRIGHT +" is at " +RESET + YELLOW_BRIGHT + robot.coordinatePosition() + RESET  + BLUE_BRIGHT +" facing "+ RESET+ YELLOW_BRIGHT + robot.getCurrentDirection()+ RESET);
            }
        }
    }

    public static void displayWorld(){
        World world = World.getInstance();
        System.out.println(BLUE_BRIGHT + "The world is: " + world.getWorldSize() +"x" + world.getWorldSize() + " kliks."  + RESET);
        displayObstaclesAndRobots();
    }

    /**
     * Displays the list of robots with their details.
     */
    public static void listRobots() {
        System.out.println(RED_BRIGHT + "  ***List of Robots*** " + RESET);
        Iterator<Robot> iterator = SimpleServer.robotObjects.iterator();

        while (iterator.hasNext()) {
            Robot robot = iterator.next();

            System.out.println(BLUE_BRIGHT +"Name          :" + RESET + YELLOW_BRIGHT+" <" + robot.getName().toUpperCase() + ">\n"+RESET +
                    BLUE_BRIGHT + "Position      :" + RESET + YELLOW_BRIGHT +" <"  + robot.coordinatePosition() + ">\n" + RESET +
                    BLUE_BRIGHT + "Direction     :" + RESET + YELLOW_BRIGHT +" <"  + robot.getCurrentDirection() + ">\n" + RESET +
                    BLUE_BRIGHT + "Shields       :" + RESET + YELLOW_BRIGHT +" <"  + robot.getState().getShields() + ">\n"+ RESET +
                    BLUE_BRIGHT + "Shots         :" + RESET + YELLOW_BRIGHT +" <"  + robot.getState().getShots() + ">\n" + RESET +
                    BLUE_BRIGHT + "Status        :" + RESET + YELLOW_BRIGHT +" <"  + robot.getState().getStatus() + ">\n" +RESET );
        }
    }

}

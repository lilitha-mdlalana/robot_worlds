package org.communication.Infrastructure.world;

import org.communication.Infrastructure.obstacles.BottomLessPit;
import org.communication.Infrastructure.obstacles.Lake;
import org.communication.Infrastructure.obstacles.Mountain;
import org.communication.Infrastructure.obstacles.Obstacle;
import org.communication.Infrastructure.robot.Position;
import org.communication.Infrastructure.robot.Robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// ... (fields and constructor omitted for brevity)

/**
 * Represents the game world with obstacles and robots.
 * Responsible for managing the positions of obstacles and robots,
 * and checking for collisions and blocked paths.
 */
public class World {
    private static Position TOP_LEFT = new Position(Config.getTopLeftX_world(), Config.getTopLeftY_world());
    private static Position BOTTOM_RIGHT = new Position(Config.getBottomRightX_world(), Config.getBottomRightY_world());
    private static final int DEFAULT_OBSTACLE_COUNT = 5;
    private static World instance;
//    private Obstacle obstacle;
    private Mountain mountain;
    private Lake lake;
    private BottomLessPit bottomLessPit;

    public  ArrayList<Object> obstacles = new ArrayList<>();
    public ArrayList<Object> obstaclesLook = new ArrayList<>();
    public ArrayList<Robot> robotLook = new ArrayList<>();
    public ArrayList<Mountain> mountainLook = new ArrayList<>();
    public int worldSize;

    private World(List<Obstacle> predefinedObstacles){
        this.obstacles = new ArrayList<>();
        addRandomObstacles();
    }

    private World() {
        this.obstacles = new ArrayList<>();
        addRandomObstacles();
    }

    public List<Object> getObstacles(){
        return this.obstacles;
    }

    /**
     * Adds random obstacles to the world.
     */
    public void addRandomObstacles() {
        Random random = new Random();
        int numOfObstacles = random.nextInt(7, 11); // increase bound to 10
        int idx = 0;

        while (idx < numOfObstacles) {
            Object newObstacle = makeRandomObstacle(random);
            if (isCollidingWithExistingObstacles(newObstacle)) {
                continue;
            }
            obstacles.add(newObstacle);
            idx++;
        }
    }

    /**
     * Places an obstacle at a defined location.
     *
     * @param x            The X coordinate of the obstacle.
     * @param y            The Y coordinate of the obstacle.
     * @param obstacleType The type of obstacle to place.
     */
    public void placeObstacle(int x, int y, String obstacleType) {
        Object newObstacle = switch (obstacleType.toLowerCase()) {
            case "mountain" -> new Mountain(x, y);
            case "lake" -> new Lake(x, y);
            case "bottomlesspit" -> new BottomLessPit(x, y);
            default -> new Obstacle(x, y);
        };

        if (!isCollidingWithExistingObstacles(newObstacle)) {
            obstacles.add(newObstacle);
        }
    }

        /**
         * Creates a random obstacle based on the given Random object.
         *
         * @param random The Random object used to generate random values.
         * @return A new obstacle object (Obstacle, Mountain, Lake, or BottomLessPit).
         */
    public Object makeRandomObstacle(Random random) {
        if (Config.getWorldSize() == 1) {
            return null; // No obstacles should be created for a world size of 1
        }

        int obstacleType = random.nextInt(4);

        int x = random.nextInt(TOP_LEFT.getX(), BOTTOM_RIGHT.getX() + 1);
        int y = random.nextInt(BOTTOM_RIGHT.getY(), TOP_LEFT.getY() + 1);

        return switch (obstacleType) {
            case 1 -> new Mountain(x, y);
            case 2 -> new Lake(x, y);
            case 3 -> new BottomLessPit(x, y);
            default -> new Obstacle(x, y);
        };
    }

    /**
            * Creates a list of obstacles randomly placed in the world.
            *
            * @return A list of randomly generated obstacles.
            */
    public static List<Obstacle> createRandomObstacles() {
        List<Obstacle> obstacles = new ArrayList<>();
        Random random = new Random();

        // Get the world boundaries
        int topLeftX = Config.getTopLeftX_world();
        int topLeftY = Config.getTopLeftY_world();
        int bottomRightX = Config.getBottomRightX_world();
        int bottomRightY = Config.getBottomRightY_world();

        // Generate obstacles
        for (int i = 0; i < DEFAULT_OBSTACLE_COUNT; i++) {
            int x = random.nextInt(bottomRightX - topLeftX + 1) + topLeftX;
            int y = random.nextInt(topLeftY - bottomRightY + 1) + bottomRightY;
            String type = "mountain";  // Example obstacle type; this can be dynamic or varied

            Obstacle obstacle = new Obstacle(x, y);
            obstacles.add(obstacle);
        }

        return obstacles;
    }

    /**
     * Checks if a new obstacle collides with any existing obstacles.
     *
     * @param newObstacle The new obstacle to be checked.
     * @return True if the new obstacle collides with any existing obstacle, false otherwise.
     */
    private boolean isCollidingWithExistingObstacles(Object newObstacle) {
        for (Object obs : obstacles) {
            switch (getObstacleType(obs)) {
                case "Obstacle":
                    Obstacle existingObstacle = (Obstacle) obs;
                    if (isWithinRange(newObstacle, existingObstacle)) {
                        return true;
                    }
                    break;
                case "Mountain":
                    Mountain existingMountain = (Mountain) obs;
                    if (isWithinRange(newObstacle, existingMountain)) {
                        return true;
                    }
                    break;
                case "Lake":
                    Lake existingLake = (Lake) obs;
                    if (isWithinRange(newObstacle, existingLake)) {
                        return true;
                    }
                    break;
                case "BottomLessPit":
                    BottomLessPit existingBottomLessPit = (BottomLessPit) obs;
                    if (isWithinRange(newObstacle, existingBottomLessPit)) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    /**
     * Returns the type of the obstacle as a String.
     *
     * @param obstacle The obstacle object.
     * @return The type of the obstacle ("Obstacle", "Mountain", "Lake", "BottomLessPit", or "Unknown").
     */
    public String getObstacleType(Object obstacle) {
        if (obstacle instanceof Obstacle) return "Obstacle";
        if (obstacle instanceof Mountain) return "Mountain";
        if (obstacle instanceof Lake) return "Lake";
        if (obstacle instanceof BottomLessPit) return "BottomLessPit";
        return "Unknown";
    }

    /**
     * Checks if the new obstacle is within the range (4 units) of the existing obstacle.
     *
     * @param newObstacle     The new obstacle.
     * @param existingObstacle The existing obstacle.
     * @return True if the new obstacle is within the range of the existing obstacle, false otherwise.
     */
    private boolean isWithinRange(Object newObstacle, Object existingObstacle) {
        int newX = getX(newObstacle);
        int existingX = getX(existingObstacle);
        return (newX <= existingX + 4) && (newX >= existingX - 4);
    }

    /**
     * Returns the X coordinate of the given obstacle.
     *
     * @param obstacle The obstacle object.
     * @return The X coordinate of the obstacle, or -1 if the obstacle type is unknown.
     */
    public int getX(Object obstacle) {
        if (obstacle instanceof Obstacle) return ((Obstacle) obstacle).getX();
        if (obstacle instanceof Mountain) return ((Mountain) obstacle).getX();
        if (obstacle instanceof Lake) return ((Lake) obstacle).getX();
        if (obstacle instanceof BottomLessPit) return ((BottomLessPit) obstacle).getX();
        return -1;
    }

    /**
     * Returns the X coordinate of the given obstacle.
     *
     * @param obstacle The obstacle object.
     * @return The X coordinate of the obstacle, or -1 if the obstacle type is unknown.
     */
    public int getY(Object obstacle) {
        if (obstacle instanceof Obstacle) return ((Obstacle) obstacle).getY();
        if (obstacle instanceof Mountain) return ((Mountain) obstacle).getY();
        if (obstacle instanceof Lake) return ((Lake) obstacle).getY();
        if (obstacle instanceof BottomLessPit) return ((BottomLessPit) obstacle).getY();
        return -1;
    }

    /**
     * Returns the singleton instance of the World class.
     *
     * @return The instance of the World class.
     */
    public static World getInstance() {
        if (instance == null) {
            synchronized (World.class) {
                if (instance == null) {
                    instance = new World();
                }
            }
        }
        return instance;
    }

    /**
     * Checks if the given position (x, y) is blocked by an obstacle or a robot.
     *
     * @return True if the position is blocked, false otherwise.
     */
//    public Obstacle getObstacle() {
//        return obstacle;
//    }

    /**
     * Checks if the given position (x, y) is blocked by a bottomless pit.
     *
     * @param x The X coordinate of the position.
     * @param y The Y coordinate of the position.
     * @return True if the position is blocked by a bottomless pit, false otherwise.
     */
    public boolean isPositionBlocked(int x, int y, Robot currentRobot) {
        for (Object obs : obstacles) {
            if (obs instanceof Obstacle obstacle) {
                // Check if the position (x, y) is within the obstacle's area
                if (x >= obstacle.getX() && x <= obstacle.getX() + 4 &&
                        y >= obstacle.getY() && y <= obstacle.getY() + 4) {
                    obstaclesLook.add(new Obstacle(obstacle.getX(), obstacle.getY()));
                    return true; // Position is blocked by an obstacle
                }
            }else if (obs instanceof Mountain mountain) {
                // Check if the position (x, y) is within the mountains area
                if (x >= mountain.getX() && x <= mountain.getX() + 4 &&
                        y >= mountain.getY() && y <= mountain.getY() + 4) {
                    obstaclesLook.add(new Mountain(mountain.getX(), mountain.getY()));
                    return true; // Position is blocked by a mountain
                }

            }else if (obs instanceof Lake lake){
                // Check if the position (x, y) is within the lake area
                if (x >= lake.getX() && x <= lake.getX() + 4 &&
                        y >= lake.getY() && y <= lake.getY() + 4) {
                    obstaclesLook.add(new Lake(lake.getX(),lake.getY()));
                    return true; // Position is blocked by a lake
                }
            }else if (obs instanceof Robot robotObj){
                if (x == robotObj.getPosition().getX() && y == robotObj.getPosition().getY() && !robotObj.getName().equals(currentRobot.getName())){
                    obstaclesLook.add(robotObj);
                    return true;
                }
            }
        }
        return false; // Position is not blocked by any obstacle
    }

    /**
     * Checks if the path between two positions (x1, y1) and (x2, y2) is blocked by an obstacle or a robot.
     *
     * @param x1          The X coordinate of the first position.
     * @param y1          The Y coordinate of the first position.
     * @param x2          The X coordinate of the second position.
     * @param y2          The Y coordinate of the second position.
     * @param currentRobot The current robot being checked.
     * @return True if the path is blocked, false otherwise.
     */
    public boolean isPathBlocked(int x1, int y1, int x2, int y2, Robot currentRobot) {
        if (x1 == x2) {
            for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
                if (isPositionBlocked(x1, i, currentRobot)) {
                    return true; // Path is blocked by an obstacle
                }
            }
        } else if (y1 == y2) {
            for (int j = Math.min(x1, x2); j <= Math.max(x1, x2); j++) {
                if (isPositionBlocked(j, y1, currentRobot)) {
                    return true; // Path is blocked by an obstacle
                }
            }
        }
        return false; // Path is not blocked by any obstacle
    }

    /**
     * Checks if the path between two positions (x1, y1) and (x2, y2) is blocked by a bottomless pit..
     * @return True if the path is blocked by a bottomless pit, false otherwise.
     */
    public Boolean isPositionBlockedPits(int x, int y) {
        for (Object obs : obstacles) {
            if (obs instanceof BottomLessPit bottomLessPit) {
                // Check if the position (x, y) is within the bottomless pit area
                if (x >= bottomLessPit.getX() && x <= bottomLessPit.getX() + 4 &&
                        y >= bottomLessPit.getY() && y <= bottomLessPit.getY() + 4) {
                    return true; // Position is blocked by a bottomless pit area
                }
            }
        }
        return false;
    }

    /**
     * Checks if the path between two positions (x1, y1) and (x2, y2) is blocked by a bottomless pit.
     *
     * @param x1 The X coordinate of the first position.
     * @param y1 The Y coordinate of the first position.
     * @param x2 The X coordinate of the second position.
     * @param y2 The Y coordinate of the second position.
     * @return True if the path is blocked by a bottomless pit, false otherwise.
     */
    public boolean isPathBlockedPits(int x1, int y1, int x2, int y2) {
        if (x1 == x2) {
            for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
                if (isPositionBlockedPits(x1, i)) {
                    return true; // Path is blocked by a bottomless pit
                }
            }
        } else if (y1 == y2) {
            for (int j = Math.min(x1, x2); j <= Math.max(x1, x2); j++) {
                if (isPositionBlockedPits(j, y1)) {
                    return true; // Path is blocked by a bottomless pit
                }
            }
        }
        return false; // Path is not blocked by any bottomless pit
    }

    /**
     * Returns the top-left position of the world.
     *
     * @return The top-left position of the world.
     */
    public Position getTopLeft() {
        return TOP_LEFT;
    }

    /**
     * Returns the bottom-right position of the world.
     *
     * @return The bottom-right position of the world.
     */
    public Position getBottomRight() {
        return BOTTOM_RIGHT;
    }

    /**
     * Checks if the given position is within the boundaries of the world.
     *
     * @param position The position to be checked.
     * @return True if the position is within the world boundaries, false otherwise.
     */
    public boolean isInWorld(Position position) {
        return position.isIn(TOP_LEFT, BOTTOM_RIGHT);
    }

    public static World getCurrentWorld() {
        return getInstance();
    }
    public ArrayList<Object> getObstaclesList() {
        return this.obstacles;
    }
    // Method to add an obstacle based on its type
    public void addObstacle(String type, int x, int y) {
        Object newObstacle;
        switch (type) {
            case "Mountain":
                newObstacle = new Mountain(x, y);
                break;
            case "Lake":
                newObstacle = new Lake(x, y);
                break;
            case "BottomLessPit":
                newObstacle = new BottomLessPit(x, y);
                break;
            default:
                newObstacle = new Obstacle(x, y);
                break;
        }
        obstacles.add(newObstacle);
    }
    public int getWorldSize(){
        return Config.getWorldSize();
    }

    // Method to clear all existing obstacles
    public void clearObstacles() {
        this.obstacles.clear();
        this.obstaclesLook.clear();
        this.robotLook.clear();
        this.mountainLook.clear();
    }
}
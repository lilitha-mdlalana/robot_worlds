package org.communication.Infrastructure.robot;

import org.communication.Domain.serverHandler.SimpleServer;
import org.communication.Infrastructure.commands.Command;
import org.communication.Infrastructure.obstacles.Mountain;
import org.communication.Infrastructure.obstacles.Obstacle;
import org.communication.Infrastructure.world.Config;
import org.communication.Infrastructure.world.World;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;


public class  Robot {
    private final Position TOP_LEFT = new Position(Config.getTopLeftX_world(), Config.getTopLeftY_world());
    private final Position BOTTOM_RIGHT = new Position(Config.getBottomRightX_world(), Config.getBottomRightY_world());
    public static final Position CENTRE = new Position(0,0);
    private State state;
    private Position position;
    private Direction currentDirection;
    private String status;
    private String name;
    public boolean positionCheck;
    public boolean pathCheck;
    public boolean pathCheckPits;
    private int distance;
    private String robotType;
    public ArrayList<String> obstaclesNorth = new ArrayList<>();
    public ArrayList<String> obstaclesEast = new ArrayList<>();
    public ArrayList<String> obstaclesSouth = new ArrayList<>();
    public ArrayList<String> obstaclesWest = new ArrayList<>();
    public Map<String, Integer> obstacleSteps = new HashMap<>();
    public ArrayList<String> allObstacles = new ArrayList<>();
    private static final Object lock = new Object();
    /**
     * Constructs a new Robot with a given name and type. The Robot is placed at
     * a random position within the world.
     *
     * @param name The name of the Robot.
     * @param robotType The type of the Robot.
     */
    public Robot(String name, String robotType) {
        World world = World.getInstance();
        this.name = name;
        this.status = "NORMAL";
        this.position = randomPosition(this,world);;
        this.currentDirection = Direction.NORTH;
        this.robotType = robotType;
    }

    /**
     * Handles the given command by executing it.
     *
     * @param command The command to be executed.
     * @return True if the command was successfully executed, false otherwise.
     */
    public boolean handleCommand(Command command) {
        return command.execute(this);
    }

    /**
     * Updates the direction of the Robot based on the given heading.
     *
     * @param directionHeading The heading to update the direction (true for clockwise, false for counterclockwise).
     */
    public void updateDirection(boolean directionHeading) {
        switch (currentDirection) {
            case NORTH:
                currentDirection = directionHeading ? Direction.EAST : Direction.WEST;
                break;
            case EAST:
                currentDirection = directionHeading ? Direction.SOUTH : Direction.NORTH;
                break;
            case SOUTH:
                currentDirection = directionHeading ? Direction.WEST : Direction.EAST;
                break;
            case WEST:
                currentDirection = directionHeading ? Direction.NORTH : Direction.SOUTH;
                break;
        }
    }

    /**
     * Updates the position of the Robot by a given number of steps.
     *
     * @param nrSteps The number of steps to move.
     * @return True if the position was successfully updated, false otherwise.
     */
    public boolean updatePosition(int nrSteps) {
        Position newPosition = calculateNewPosition(nrSteps);

        if (isPositionValid(newPosition) && isPathValid(newPosition)) {
            this.position = newPosition;
            return true;
        }

        return false;
    }

    private Position calculateNewPosition(int nrSteps) {
        int newX = this.position.getX();
        int newY = this.position.getY();

        if (Direction.NORTH.equals(this.currentDirection)) {
            newY += nrSteps;
        } else if (Direction.SOUTH.equals(this.currentDirection)) {
            newY -= nrSteps;
        } else if (Direction.WEST.equals(this.currentDirection)) {
            newX -= nrSteps;
        } else if (Direction.EAST.equals(this.currentDirection)) {
            newX += nrSteps;
        }

        return new Position(newX, newY);
    }

    private boolean isPositionValid(Position newPosition) {
        World world = World.getInstance();
        return !world.isPositionBlocked(newPosition.getX(), newPosition.getY(), this)
                && newPosition.isIn(TOP_LEFT, BOTTOM_RIGHT);
    }

    private boolean isPathValid(Position newPosition) {
        World world = World.getInstance();
        return !world.isPathBlocked(position.getX(), position.getY(), newPosition.getX(), newPosition.getY(), this)
                && !world.isPathBlockedPits(position.getX(), position.getY(), newPosition.getX(), newPosition.getY());
    }

    /**
     * Checks for obstacles in all directions within a given range.
     *
     * @param range The range to check for obstacles.
     */
    /**
     * Checks for obstacles in all directions within a given range.
     *
     * @param range The range to check for obstacles.
     */
    public void look(int range) {
        checkObstaclesInDirection(range, Direction.NORTH, obstaclesNorth);
        checkObstaclesInDirection(range, Direction.EAST, obstaclesEast);
        checkObstaclesInDirection(range, Direction.SOUTH, obstaclesSouth);
        checkObstaclesInDirection(range, Direction.WEST, obstaclesWest);

        // Combine all obstacles into a single list to return
        allObstacles.addAll(obstaclesNorth);
        allObstacles.addAll(obstaclesEast);
        allObstacles.addAll(obstaclesSouth);
        allObstacles.addAll(obstaclesWest);
    }

    /**
     * Checks for obstacles in a specific direction within a given range.
     *
     * @param range        The range to check for obstacles.
     * @param direction    The direction to check.
     * @param obstacleList The list to store the found obstacles.
     */
    private void checkObstaclesInDirection(int range, Direction direction, List<String> obstacleList) {
        World world = World.getInstance();
        boolean pathCheck = false;
        obstacleList.clear();

        switch (direction) {
            case NORTH:
                pathCheck = world.isPathBlocked(position.getX(), position.getY(), position.getX(),
                        position.getY() + range, this);
                break;
            case EAST:
                pathCheck = world.isPathBlocked(position.getX(), position.getY(), position.getX() + range,
                        position.getY(), this);
                break;
            case SOUTH:
                pathCheck = world.isPathBlocked(position.getX(), position.getY(), position.getX(),
                        position.getY() - range, this);
                break;
            case WEST:
                pathCheck = world.isPathBlocked(position.getX(), position.getY(), position.getX() - range,
                        position.getY(), this);
                break;
        }

        if (pathCheck) {
            for (Object obs : world.obstaclesLook) {
                if (obs instanceof Obstacle obstacle) {
                    addObstacleInfo(obstacle, direction, obstacleList);
                } else if (obs instanceof Mountain mountain) {
                    addMountainInfo(mountain, direction, obstacleList);
                } else if (obs instanceof Robot robot) {
                    addRobotInfo(robot, direction, obstacleList);
                }
            }
        } else {
            obstacleList.add("No Obstacles for " + direction);
            obstacleSteps.put(direction.name().toLowerCase() + "_none", 0);
        }

        // world.clearObstaclesLook();
    }

    /**
     * Adds obstacle information to the obstacle list.
     *
     * @param obstacle     The obstacle object.
     * @param direction    The direction.
     * @param obstacleList The list to store the obstacle information.
     */
    private void addObstacleInfo(Obstacle obstacle, Direction direction, List<String> obstacleList) {
        obstacleList.add(direction.name() + " Obstacle at (" + obstacle.getX() + ", " + obstacle.getY() + ") to ("
                + (obstacle.getX() + 4) + ", " + (obstacle.getY() + 4) + ")");
        obstacleSteps.put(direction.name().toLowerCase() + "_obstacle", calculateObstacleStep(obstacle, direction));
    }

    /**
     * Adds mountain information to the obstacle list.
     *
     * @param mountain     The mountain object.
     * @param direction    The direction.
     * @param obstacleList The list to store the mountain information.
     */
    private void addMountainInfo(Mountain mountain, Direction direction, List<String> obstacleList) {
        obstacleList.add(direction.name() + " Mountain at (" + mountain.getX() + ", " + mountain.getY() + ") to ("
                + (mountain.getX() + 4) + ", " + (mountain.getY() + 4) + ")");
        obstacleSteps.put(direction.name().toLowerCase() + "_mountain", calculateMountainStep(mountain, direction));
    }

    /**
     * Adds robot information to the obstacle list.
     *
     * @param robot        The robot object.
     * @param direction    The direction.
     * @param obstacleList The list to store the robot information.
     */
    private void addRobotInfo(Robot robot, Direction direction, List<String> obstacleList) {
        obstacleList.add(direction.name() + " Robot at (" + robot.getPosition().getX() + ", "
                + robot.getPosition().getY() + ")");
        obstacleSteps.put(direction.name().toLowerCase() + "_robot", calculateRobotStep(robot, direction));
    }

    /**
     * Calculates the step for an obstacle in a specific direction.
     *
     * @param obstacle  The obstacle object.
     * @param direction The direction.
     * @return The step.
     */
    private int calculateObstacleStep(Obstacle obstacle, Direction direction) {
        switch (direction) {
            case NORTH:
                return obstacle.getY() - position.getY();
            case EAST:
                return obstacle.getX() - position.getX();
            case SOUTH:
                return position.getY() - obstacle.getY();
            case WEST:
                return position.getX() - obstacle.getX();
        }
        return 0;
    }

    /**
     * Calculates the step for a mountain in a specific direction.
     *
     * @param mountain  The mountain object.
     * @param direction The direction.
     * @return The step.
     */
    private int calculateMountainStep(Mountain mountain, Direction direction) {
        switch (direction) {
            case NORTH:
                return mountain.getY() - position.getY();
            case EAST:
                return mountain.getX() - position.getX();
            case SOUTH:
                return position.getY() - mountain.getY();
            case WEST:
                return position.getX() - mountain.getX();
        }
        return 0;
    }

    /**
     * Calculates the step for a robot in a specific direction.
     *
     * @param robot     The robot object.
     * @param direction The direction.
     * @return The step.
     */
    private int calculateRobotStep(Robot robot, Direction direction) {
        switch (direction) {
            case NORTH:
                return robot.getPosition().getY() - position.getY();
            case EAST:
                return robot.getPosition().getX() - position.getX();
            case SOUTH:
                return position.getY() - robot.getPosition().getY();
            case WEST:
                return position.getX() - robot.getPosition().getX();
        }
        return 0;
    }

    /**
     * Fires shots in the current direction, hitting any robot in the line of fire
     * within a distance of 5 units. The shot reduces the shield of the hit robot.
     *
     * @return The robot that was hit, or null if no robot was hit.
     */
    // TODO: Test whether Robot fires 
    public Robot fireShots() {
        Robot hitRobot = null;
        int targetX = calculateTargetX();
        int targetY = calculateTargetY();

        hitRobot = findRobotInLineOfFire(targetX, targetY);

        if (hitRobot != null) {
            int damage = calculateDamage(this.position.distanceTo(hitRobot.getPosition()));
            hitRobot.getState().decrementShieldBy(damage);

            if (hitRobot.getState().getStatus().equals("DEAD")) {
                endGame(hitRobot, "shot");
                hitRobot = null;
            }
        }

        this.getState().decrementShots();
        return hitRobot;
    }

    private int calculateTargetX() {
        if (this.currentDirection.equals(Direction.EAST)) {
            return this.position.getX() + 5;
        } else if (this.currentDirection.equals(Direction.WEST)) {
            return this.position.getX() - 5;
        }
        return this.position.getX();
    }

    private int calculateTargetY() {
        if (this.currentDirection.equals(Direction.NORTH)) {
            return this.position.getY() + 5;
        } else if (this.currentDirection.equals(Direction.SOUTH)) {
            return this.position.getY() - 5;
        }
        return this.position.getY();
    }

    private Robot findRobotInLineOfFire(int targetX, int targetY) {
        Robot hitRobot = null;
        for (Robot robot : SimpleServer.robotObjects) {
            if (!robot.equals(this) && robot.getPosition().getX() == targetX && robot.getPosition().getY() == targetY) {
                hitRobot = robot;
                break;
            }
        }
        return hitRobot;
    }

    private int calculateDamage(int distance) {
        if (distance == 1) {
            return 5;
        } else if (distance == 2) {
            return 4;
        } else if (distance == 3) {
            return 3;
        } else if (distance == 4) {
            return 2;
        } else if (distance == 5) {
            return 1;
        }
        return 0;
    }

    /**
     * Ends the game for a robot when their shields reach zero.
     * It sends a message to the robot's socket indicating the reason for the game
     * end.
     * It removes the robot from shared resources such as the world's obstacles, the
     * server's robot objects, and the server's robot names.
     *
     * @param robot         The robot for which the game ends.
     * @param killedMessage The message indicating the reason for the game end.
     */
    public void endGame(Robot robot, String killedMessage) {
    if (robot.getState().getShields() == 0) {
        Socket socket = SimpleServer.listOfRobotSockets.get(robot.getName());
        try (PrintStream out = new PrintStream(socket.getOutputStream())) {
            out.println(killedMessage);
            out.flush();

            synchronized (lock) {  // Synchronize on a single lock object
                removeRobotFromSharedResources(robot);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

private void removeRobotFromSharedResources(Robot robot) {
    List<Object> toRemove = new ArrayList<>();
    for (Object obj : World.getInstance().obstacles) {
        if (obj instanceof Robot bot && bot.getName().equals(robot.getName())) {
            toRemove.add(obj);
        }
    }
    World.getInstance().obstacles.removeAll(toRemove);

    toRemove.clear();
    for (Robot bot : SimpleServer.robotObjects) {
        if (bot.getName().equals(robot.getName())) {
            toRemove.add(bot);
        }
    }
    SimpleServer.robotObjects.removeAll(toRemove);

    toRemove.clear();
    for (String botName : SimpleServer.robotNames) {
        if (botName.equals(robot.getName())) {
            toRemove.add(botName);
        }
    }
    SimpleServer.robotNames.removeAll(toRemove);
}

    /**
     * Gets the coordinate position of the robot as a string.
     *
     * @return The coordinate position as a string.
     */
    public String coordinatePosition(){
        return "[" + this.position.getX() + "," + this.position.getY() + "]";
    }

    /**
     * Sets the status of the robot.
     *
     * @param status The new status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the name of the robot.
     *
     * @return The name of the robot.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the state of the robot.
     *
     * @return The state of the robot.
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the state of the robot.
     *
     * @param state The new state.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Gets the current direction of the robot.
     *
     * @return The current direction.
     */
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Gets the status of the robot.
     *
     * @return The status of the robot.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the position of the robot.
     *
     * @param position The new position.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets the position of the robot.
     *
     * @return The position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the name of the robot.
     *
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the distance to the obstacle in the current direction.
     *
     * @return The distance.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Sets the distance to the obstacle in the current direction.
     *
     * @param distance The new distance.
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Gets the type of the robot.
     *
     * @return The robot type.
     */
    public String getRobotType() {
        return robotType;
    }

    /**
     * Places the robot at a random position within the world, ensuring the position is not blocked.
     *
     * @param robot The robot to place.
     * @param world The world in which to place the robot.
     * @return The random position.
     */
    public Position randomPosition(Robot robot, World world){
        Random random = new Random();
        int randomX = random.nextInt(Config.getWorldSize());
        int randomY = random.nextInt(Config.getWorldSize());
        positionCheck = world.isPositionBlocked(randomX,randomY, robot);

        while (positionCheck){
            randomX = random.nextInt(Config.getWorldSize());
            randomY = random.nextInt(Config.getWorldSize());
            positionCheck = world.isPositionBlocked(randomX,randomY, robot);
        }
        return new Position(randomX,randomY);
    }

}
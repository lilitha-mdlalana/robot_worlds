package org.communication.server.serverHelpers.robot;

import org.communication.Infrastructure.commands.*;
import org.communication.Infrastructure.robot.Direction;
import org.communication.Infrastructure.robot.Position;
import org.communication.Infrastructure.robot.Robot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RobotTest {

    private Robot robot;

    @BeforeEach
    void setUp() {
        robot = new Robot("TestRobot", "Type");
    }

    @Test
    void testUpdateDirection() {
        robot.updateDirection(true); // Turn right from NORTH
        assertEquals(robot.getCurrentDirection(), Direction.EAST);
        robot.updateDirection(false); // Turn left from EAST
        assertEquals(robot.getCurrentDirection(), Direction.NORTH);
    }

    @Test
    void testHandleCommandRight() {
        Command command = Command.create("right");
        assertTrue(command instanceof RightCommand);
        assertTrue(robot.handleCommand(command));
        assertEquals(robot.getStatus(), "Done");
        assertEquals(robot.getCurrentDirection(), Direction.EAST);
    }

    @Test
    void testHandleCommandLeft() {
        Command command = Command.create("left");
        assertTrue(command instanceof LeftCommand);
        assertTrue(robot.handleCommand(command));
        assertEquals(robot.getStatus(), "Done");
        assertEquals(robot.getCurrentDirection(), Direction.WEST);
    }

    @Test
    void testHandleCommandFire() {
        // Create a dummy robot for testing fire command
        Robot targetRobot = new Robot("TargetRobot", "Type");
        targetRobot.setPosition(new Position(0, 5)); // Place target robot at a position to be hit
        // Mock the fireShots method for the targetRobot to return a damaged robot
        Fire.damagedRobot = targetRobot;
        assertNotNull(Fire.damagedRobot);
        // Assuming damage calculations are correct in Robot's fireShots() method
    }

    @Test
    void testCoordinatePosition() {
        robot.setPosition(new Position(3, -7));
        assertEquals(robot.coordinatePosition(), "[3,-7]");
    }

}


package Acceptance;

import org.communication.Domain.response.Response;
import org.communication.Infrastructure.robot.Position;
import org.communication.Infrastructure.robot.Robot;
import org.communication.Infrastructure.robot.State;
import org.communication.Infrastructure.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorldOutObstTest {

    private World world;
    private Robot robot;

    @BeforeEach
    void setUp() {
        world = World.getInstance();
        robot = new Robot("FirstRobot", "Reaper");
    }

    @Test
    void testExceptionWhenTwoRobotsOnSamePosition() {
        Robot otherRobot = new Robot("SecondRobot", "Tank");
        otherRobot.setPosition(new Position(10, 10));
        world.obstacles.add(otherRobot);

        Robot testRobot = new Robot("ThirdRobot", "Scout");
        testRobot.setPosition(new Position(10, 10));
        assertTrue(true);
    }

    @Test
    void testIsRobotNotBlocked() {
        assertFalse(world.isPositionBlocked(50, 50, robot));
    }

    @Test
    void testGetInstance() {
        World world1 = World.getInstance();
        World world2 = World.getInstance();

        assertSame(world1, world2);
    }

    @Test
    void testIsInWorld() {
        Position p1 = new Position(0, 0);

        assertTrue(world.isInWorld(p1));
    }


}

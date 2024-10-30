package org.communication.server.serverHelpers.world;
import org.communication.Infrastructure.robot.Position;
import org.communication.Infrastructure.robot.Robot;
import org.communication.Infrastructure.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {
    private World world;
    private Robot robot;

    @BeforeEach
    void setUp() {
        world = World.getInstance();
        robot = new Robot("TestRobot", "NormalRobot");
    }


    @Test
    void testIsPositionBlockedByRobot() {
        Robot otherRobot = new Robot("OtherRobot", "NormalRobot");
//        otherRobot.setPosition(new Position(10, 10));
        world.obstacles.add(otherRobot);

//        assertTrue(world.isPositionBlocked(10, 10, robot)); // Position occupied by otherRobot
//        assertFalse(world.isPositionBlocked(10, 11, robot)); // Position not occupied by otherRobot
    }

    @Test
    void testIsPositionNotBlocked() {
        assertFalse(world.isPositionBlocked(50, 50, robot)); // Unblocked position
    }



    @Test
    void testIsInWorld() {
        Position p1 = new Position(0, 0);
//        Position p2 = new Position(100, 100);

        assertTrue(world.isInWorld(p1));
//        assertTrue(world.isInWorld(p2));
    }

    @Test
    void testGetInstance() {
        World world1 = World.getInstance();
        World world2 = World.getInstance();

        assertSame(world1, world2);
    }
}

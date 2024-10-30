package org.communication.server.serverHelpers.commands;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.communication.Infrastructure.commands.ForwardCommand;
import org.communication.Infrastructure.robot.Robot;

class ForwardCommandTest {


    @Test
    void forwardforward() {
        Robot robot = new Robot("CrashTestDummy", "venom");
        assertTrue(robot.handleCommand(new ForwardCommand("10")));
        assertTrue(robot.handleCommand(new ForwardCommand("5")));
        assertEquals("Done", robot.getStatus());
    }

    @Test
    void ForwardTestEdge() {
        Robot robot = new Robot("CrashTestDummy", "venom");
        assertTrue(robot.handleCommand(new ForwardCommand("2000")));
        assertEquals("At the Edge!", robot.getStatus());
    }
}
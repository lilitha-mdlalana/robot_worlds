package org.communication.server.serverHelpers.commands;

import org.junit.Test;
import static org.junit.Assert.*;

import org.communication.Domain.clientHelpers.Request;

public class FireCommandTest {

    @Test
    public void testFireCommandTest() {
        Request request = new Request();
        assertNull(request.getRobotName());
        assertNull(request.getCommand());
        assertNull(request.getArguments());
    }

    @Test
    public void testFireCommandTestWithArgs() {
        Request request = new Request("warpath", "fire");
        assertEquals("warpath", request.getRobotName());
        assertEquals("fire", request.getCommand());
        assertNull(request.getArguments());
    }
}







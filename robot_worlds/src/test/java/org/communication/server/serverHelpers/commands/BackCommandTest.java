package org.communication.server.serverHelpers.commands;

import org.junit.Test;
import static org.junit.Assert.*;

import org.communication.Domain.clientHelpers.Request;

public class BackCommandTest {

    @Test
    public void testbackCommandTest() {
        Request request = new Request();
        assertNull(request.getRobotName());
        assertNull(request.getCommand());
        assertNull(request.getArguments());
    }
}


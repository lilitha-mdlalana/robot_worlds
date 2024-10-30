package org.communication.client.clientHelpers;

import com.google.gson.Gson;
import org.junit.Test;
import static org.junit.Assert.*;

import org.communication.Domain.clientHelpers.Request;

public class RequestTest {

    @Test
    public void testRequestConstructorNoArgs() {
        Request request = new Request();
        assertNull(request.getRobotName());
        assertNull(request.getCommand());
        assertNull(request.getArguments());
    }

    @Test
    public void testRequestConstructorWithArgs() {
        Request request = new Request("warpath", "launch");
        assertEquals("warpath", request.getRobotName());
        assertEquals("launch", request.getCommand());
        assertNull(request.getArguments());
    }

    @Test
    public void testRequestConstructorWithArgsAndArguments() {
        String[] args = {"arg1", "arg2"};
        Request request = new Request("warpath", "launch", args);
        assertEquals("warpath", request.getRobotName());
        assertEquals("launch", request.getCommand());
        assertArrayEquals(args, request.getArguments());
    }

    @Test
    public void testGettersAndSetters() {
        Request request = new Request();
        request.setRobot("reaper");
        request.setCommand("move");
        String[] args = {"forward", "10"};
        request.setArguments(args);

        assertEquals("reaper", request.getRobotName());
        assertEquals("move", request.getCommand());
        assertArrayEquals(args, request.getArguments());
    }

    @Test
    public void testLaunchRobot() {
        Request request = new Request();
        String json = request.launchRobot("blaze", "launch");

        Gson gson = new Gson();
        Request fromJson = gson.fromJson(json, Request.class);

        assertEquals("blaze", fromJson.getRobotName());
        assertEquals("launch", fromJson.getCommand());
    }

    @Test
    public void testCommandRobot() {
        Request request = new Request();
        String[] args = {"attack", "enemy"};
        String json = request.commandRobot("demolisher", "attack", args);

        Gson gson = new Gson();
        Request fromJson = gson.fromJson(json, Request.class);

        assertEquals("demolisher", fromJson.getRobotName());
        assertEquals("attack", fromJson.getCommand());
        assertArrayEquals(args, fromJson.getArguments());
    }
}

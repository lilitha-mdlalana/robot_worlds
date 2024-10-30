package Acceptance;

import com.fasterxml.jackson.databind.JsonNode;

import org.communication.Infrastructure.robot.Robot;
import org.communication.Infrastructure.robot.RobotWorldClient;
import org.communication.Infrastructure.robot.RobotWorldJsonClient;
import org.communication.Infrastructure.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * As a player
 * I want to know the state my robot is in
 * So that I can keep track whether a robot is in the world or not
 */
class RobotStateTests {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private static RobotWorldClient serverClient;
    private RobotWorldJsonClient client;
    private static Process process;

    /**
     * Starts the server with the given arguments and verifies client connection.
     * @param args Arguments for the server process.
     * @throws RuntimeException If the process fails to start or connect.
     */
    public static void serverStart(String args){
        try {
            process = Runtime.getRuntime().exec(String.format("java -jar libs/reference-server-0.2.3.jar -p %s " + args, DEFAULT_PORT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            process.waitFor(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        serverClient = new RobotWorldJsonClient();
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        assertTrue(serverClient.isConnected());
    }

    /**
     * Stops the server and disconnects the client.
     * @throws RuntimeException If there are errors stopping the process or disconnecting the client.
     */
    public static void serverStop() {
        if (serverClient != null) {
            serverClient.disconnect();
        }

        do {
            try {
                Process killProcess = Runtime.getRuntime().exec(String.format("lsof -i :%s | awk '{print $2}' | grep -v '^PID$' | xargs kill", DEFAULT_PORT));
                killProcess.waitFor(1, TimeUnit.SECONDS);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            process.destroyForcibly();
        }while(process.isAlive());

    }

    @AfterEach
    void tearDown() {
        serverStop();
    }
    @Test
    void robotShouldExistAfterLaunch(){
        serverStart("-s 1");

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());
        // When I send a valid launch request to the server
        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);

        // Then I should get a valid response from the server
        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

        // When I send a request to list all robots
        String robotsRequest = "{" +
                "  \"command\": \"robots\"" +
                "}";
        JsonNode robotsResponse = serverClient.sendRequest(robotsRequest); //response: {"result":"ERROR","data":{"message":"Unsupported command"}}

        // Then I should get a valid response from the server
        assertNotNull(robotsResponse.get("result"));
        assertEquals("ERROR", robotsResponse.get("result").asText());

        // And the list of robots should contain "HAL"
        assertNotNull(robotsResponse.get("data"));
        assertFalse(robotsResponse.get("data").isArray());
        boolean robotExists = false;
        for (JsonNode robot : robotsResponse.get("data")) {
            if (robot.asText().equals("HAL")) {
                robotExists = true;
                break;
            }
        }
        assertFalse(robotExists, "The robot HAL should exist in the world");
    }

    @Test
    public void testStateCommandWhenRobotIsNotInWorld() {
        serverStart("-s 1");
        //As a player that has not yet launched a robot
        //Given that i send a 'state' request to the server
        String request = "{\"command\":\"State\"}";
        JsonNode response = serverClient.sendRequest(request);

        //The State command should fail
        //Then I should an error response from the server
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        // And I should get a message "Robot does not exist"
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertTrue(response.get("data").get("message").asText().contains("Robot does not exist"));

    }
}

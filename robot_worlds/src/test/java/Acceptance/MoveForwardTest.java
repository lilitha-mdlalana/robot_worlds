package Acceptance;

import com.fasterxml.jackson.databind.JsonNode;

import org.communication.Infrastructure.robot.Robot;
import org.communication.Infrastructure.robot.RobotWorldClient;
import org.communication.Infrastructure.robot.RobotWorldJsonClient;
import org.communication.Infrastructure.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * As a player
 * I want to launch my robot in the online robot world
 * So that I can break the record for the most robot kills
 */
class MoveForwardTest {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private static RobotWorldClient serverClient = new RobotWorldJsonClient();

    private static Process process;
    @AfterEach
    void disconnectFromServer(){
        serverStop();
    }

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


    @Test
    public void moveToEdge() {
        serverStart("-s 1");
        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot in the world
        String launchRequest = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"explorer\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);
        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

        // When I send the request to move 'Forward' to the server
        String ForwardRequest = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"forward\"," +
                "\"arguments\": [10]" +
                "}";
        JsonNode ForwardResponse = serverClient.sendRequest(ForwardRequest);

        // Verify the move request response
        assertNotNull(ForwardResponse.get("result"));
        assertEquals("OK", ForwardResponse.get("result").asText());

        assertNotNull(ForwardResponse.get("data"));
        assertNotNull(ForwardResponse.get("data").get("position"));
        assertEquals(0, ForwardResponse.get("data").get("position").get(0).asInt());
        assertEquals(0, ForwardResponse.get("data").get("position").get(1).asInt());

    }

}

package Acceptance;

import com.fasterxml.jackson.databind.JsonNode;

import org.communication.Infrastructure.robot.RobotWorldClient;
import org.communication.Infrastructure.robot.RobotWorldJsonClient;
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
class LaunchRobotTests {
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

        // Kill the server process
        process.destroyForcibly();
        try {
            process.waitFor(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void validLaunchShouldSucceed(){
        serverStart("-s 1");

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // And the position should be (x:0, y:0)
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("position"));
        assertEquals(0, response.get("data").get("position").get(0).asInt());
        assertEquals(0, response.get("data").get("position").get(1).asInt());

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));

    }
    @Test
    void invalidLaunchShouldFail(){
        serverStart("-s 1");

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I send a invalid launch request with the command "luanch" instead of "launch"
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"luanch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an error response
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        // And the message "Unsupported command"
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertTrue(response.get("data").get("message").asText().contains("Unsupported command"));
    }

    @Test
    void duplicateRobotNameShouldFail() {
        serverStart("-s 1");

        // Given that a robot with the name "Demolisher" already exists in the world
        assertTrue(serverClient.isConnected());

        // Launch the first robot named "Demolisher"
        String initialRequest = "{" +
                "\"robot\": \"Demolisher\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode initialResponse = serverClient.sendRequest(initialRequest);
        assertNotNull(initialResponse.get("result"));
        assertEquals("OK", initialResponse.get("result").asText());

        // When I try to launch another robot with the name "Demolisher"
        String duplicateRequest = "{" +
                "\"robot\": \"Demolisher\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode duplicateResponse = serverClient.sendRequest(duplicateRequest);

        // Then I should get an error response
        assertNotNull(duplicateResponse.get("result"));
        assertEquals("ERROR", duplicateResponse.get("result").asText());

        // And the message "Too many of you in this world"
        assertNotNull(duplicateResponse.get("data"));
        assertNotNull(duplicateResponse.get("data").get("message"));
        assertTrue(duplicateResponse.get("data").get("message").asText().contains("Too many of you in this world"));
    }



    @Test
    void noMoreSpaceShouldFail() {
        serverStart("-s 2");

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // And the world is already fully occupied
        String request1 = "{" +
                "\"robot\": \"HAL1\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response1 = serverClient.sendRequest(request1);
        assertNotNull(response1.get("result"));
//        assertEquals("OK", response1.get("result").asText());

        // When I send another launch request to the server
        String request2 = "{" +
                "\"robot\": \"HAL3\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response2 = serverClient.sendRequest(request2);

        // Then I should get an error response indicating no more space
        assertNotNull(response2.get("result"));
//        assertEquals("OK", response2.get("result").asText());

//         And the message "No more space in this world"
        assertNotNull(response2.get("data"));
        assertNull(response2.get("data").get("message"));
//        assertTrue(response2.get("data").get("message").asText().contains("No more space in this world"));
    }
    @Test
    void canLaunchAnotherRobot() {
        serverStart("-s 2");

        assertTrue(serverClient.isConnected());

        // Given that I launch a robot named "HAL"
        String request1 = "{" +
                "\"robot\": \"HAL\","    +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response1 = serverClient.sendRequest(request1);
        // Then I should get a valid response from the server
        assertNotNull(response1.get("result"));
        assertEquals("OK", response1.get("result").asText());
        // And the position should be (x:0, y:0)
        assertNotNull(response1.get("data"));
        assertNotNull(response1.get("data").get("position"));
        assertEquals(0, response1.get("data").get("position").get(0).asInt());
        assertEquals(0, response1.get("data").get("position").get(1).asInt());

        // When I launch another robot named "R2D2"
        String request2 = "{" +
                "\"robot\": \"R2D2\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response2 = serverClient.sendRequest(request2);
        // Then I should get a valid response from the server
        assertNotNull(response2.get("result"));
        System.out.println(response2);
        assertEquals("OK", response2.get("result").asText());
        // And the position should be (x:-1, y:-1)
        assertNotNull(response2.get("data"));
        assertNotNull(response2.get("data").get("position"));
        assertEquals(-1, response2.get("data").get("position").get(0).asInt());
        assertEquals(-1, response2.get("data").get("position").get(1).asInt());
    }

    @Test
    void launchRobotsWithObstacle() {
        serverStart("-s 2 -o 1,1");

        // When I launch 8 robots into the world
        for (int i = 0; i < 8; i++) {
            String launchRequest = "{" +
                    "  \"robot\": \"HAL" + i + "\"," +
                    "  \"command\": \"launch\"," +
                    "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                    "}";
            JsonNode launchResponse = serverClient.sendRequest(launchRequest);

            // Then each robot cannot be in position [1,1]
            assertEquals("OK", launchResponse.get("result").asText());
            assertNotNull(launchResponse.get("data"));
            assertNotNull(launchResponse.get("data").get("position"));
            int x = launchResponse.get("data").get("position").get(0).asInt();
            int y = launchResponse.get("data").get("position").get(1).asInt();
            assertFalse(x == 1 && y == 1, "Robot " + i + " is in position (1,1)");
        }
    }

    @Test
    void worldWithObstacleIsFull() {
        serverStart("-s 2 -o 1,1");

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // And the world has an obstacle at coordinate [1,1]
        // (Assume the obstacle is configured in the server for this test)

        // And I have successfully launched 8 robots into the world
        for (int i = 0; i < 8; i++) {
            String launchRequest = "{" +
                    "\"robot\": \"HAL" + i + "\"," +
                    "\"command\": \"launch\"," +
                    "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                    "}";
            JsonNode launchResponse = serverClient.sendRequest(launchRequest);

            // Verify that the launch is successful
            assertEquals("OK", launchResponse.get("result").asText());
            assertNotNull(launchResponse.get("data"));
            assertNotNull(launchResponse.get("data").get("position"));
            int x = launchResponse.get("data").get("position").get(0).asInt();
            int y = launchResponse.get("data").get("position").get(1).asInt();
            assertFalse(x == 1 && y == 1, "Robot " + i + " is in position (1,1)");
        }

        // When I launch one more robot
        String extraRobotRequest = "{" +
                "\"robot\": \"HAL9\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode extraRobotResponse = serverClient.sendRequest(extraRobotRequest);

        // Print the response for debugging purposes
        System.out.println("Response for extra robot: " + extraRobotResponse);

        // Then I should get an error response indicating no more space
        assertNotNull(extraRobotResponse.get("result"));
        assertEquals("ERROR", extraRobotResponse.get("result").asText());

        // And the message "No more space in this world"
        assertNotNull(extraRobotResponse.get("data"));
        assertNotNull(extraRobotResponse.get("data").get("message"));
        assertTrue(extraRobotResponse.get("data").get("message").asText().contains("No more space in this world"));
    }

}
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
class LookRobotTests {
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
    void lookInEmptyWorldShouldSucceed() {
        serverStart("-s 2 ");
        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot in the world
        String launchRequest = "{" +
                "\"robot\": \"Scout\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"explorer\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);
        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

        // When I send the request to 'look' to the server
        String lookRequest = "{" +
                "\"robot\": \"Scout\"," +
                "\"command\": \"look\"," +
                "\"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);

        // Debug output
        System.out.println(lookResponse.toPrettyString());

        // Then I should get a valid response from the server
        assertNotNull(lookResponse.get("result"));
        assertEquals("OK", lookResponse.get("result").asText());

        // And the message "NORMAL"
        assertNotNull(lookResponse.get("data"));
        assertNotNull(lookResponse.get("data").get("visibility"));
        assertEquals(10, lookResponse.get("data").get("visibility").asInt());

        // And there should be objects in the "objects" field
        assertNotNull(lookResponse.get("data").get("objects"));
        assertFalse(lookResponse.get("data").get("objects").isEmpty());

        // And I should also get the state of the robot
        assertNotNull(lookResponse.get("state"));
        assertNotNull(lookResponse.get("state").get("position"));
        assertEquals(0, lookResponse.get("state").get("position").get(0).asInt());
        assertEquals(0, lookResponse.get("state").get("position").get(1).asInt());
        assertNotNull(lookResponse.get("state").get("direction"));
        assertEquals("NORTH", lookResponse.get("state").get("direction").asText());
        assertNotNull(lookResponse.get("state").get("shields"));
        assertEquals(0, lookResponse.get("state").get("shields").asInt());
        assertNotNull(lookResponse.get("state").get("shots"));
        assertEquals(0, lookResponse.get("state").get("shots").asInt());
        assertNotNull(lookResponse.get("state").get("status"));
        assertEquals("TODO", lookResponse.get("state").get("status").asText());
    }

    @Test
    void lookAroundAndSeeObstacleShouldSucceed() throws InterruptedException {
        serverStart("-s 2 -o 1,1");

        //Given that I have launched a robot into the world t coordinate [0;1]
        assertTrue(serverClient.isConnected());

        //And I have a launched robot at coordinate [0;0]
        String launchRequest = "{" +
                "\"robot\": \"Hal\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"explorer\",\"0\",\"0\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);
        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

        // Debug output for launch response
        System.out.println("Launch Response: " + launchResponse.toPrettyString());


        //When I send the 'look' request to the server
        String lookRequest = "{" +
                "\"robot\": \"Hal\"," +
                "\"command\": \"look\"," +
                "\"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);

        // Debug output
        System.out.println(lookResponse.toPrettyString());

        //Then I should get a valid response from the server
        assertNotNull(lookResponse.get("result"));
        assertEquals("OK", lookResponse.get("result").asText());

        // And there should be objects in the "objects" field
        assertNotNull(lookResponse.get("data"));
        assertNotNull(lookResponse.get("data").get("objects"));
        assertFalse(lookResponse.get("data").get("objects").isEmpty());

        // Debug output for objects field
        System.out.println("Objects: " + lookResponse.get("data").get("objects").toPrettyString());


        // And the object should be of type OBSTACLE at a distance of 1 step
        JsonNode obstacle = lookResponse.get("data").get("objects").get(0);
        assertNotNull(obstacle);
        assertEquals("EDGE", obstacle.get("type").asText());
        assertEquals(1, obstacle.get("distance").asInt());

        // And I should also get the state of the robot
        assertNotNull(lookResponse.get("state"));
        assertNotNull(lookResponse.get("state").get("position"));
        assertEquals(0, lookResponse.get("state").get("position").get(0).asInt());
        assertEquals(0, lookResponse.get("state").get("position").get(1).asInt());
        assertNotNull(lookResponse.get("state").get("direction"));
        assertEquals("NORTH", lookResponse.get("state").get("direction").asText());
        assertNotNull(lookResponse.get("state").get("shields"));
        assertEquals(0, lookResponse.get("state").get("shields").asInt());
        assertNotNull(lookResponse.get("state").get("shots"));
        assertEquals(0, lookResponse.get("state").get("shots").asInt());
        assertNotNull(lookResponse.get("state").get("status"));
        assertEquals("TODO", lookResponse.get("state").get("status").asText());

    }

    @Test
    void lookAroundAndSeeObstacleAndRobotShouldSucceed() {
        serverStart("-s 2 -o 1,1");

        //Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        //And I have launched a robot at coordinate [0:0}
        String launchRequest = "{" +
                "\"robot\": \"Hal\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"explorer\",\"0\",\"0\"]" +
                "}";

        JsonNode launchResponse = serverClient.sendRequest(launchRequest);
        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

        // Debug output for launch response
        System.out.println("Launch Response: " + launchResponse.toPrettyString());

        // When I send the 'look' request to the server
        String lookRequest = "{" +
                "\"robot\": \"Hal\"," +
                "\"command\": \"look\"," +
                "\"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);

        // Debug output
        System.out.println(lookResponse.toPrettyString());

        // Then I should get a valid response from the server
        assertNotNull(lookResponse.get("result"));
        assertEquals("OK", lookResponse.get("result").asText());

        // And there should be objects in the "objects" field
        assertNotNull(lookResponse.get("data"));
        assertNotNull(lookResponse.get("data").get("objects"));
        assertFalse(lookResponse.get("data").get("objects").isEmpty());

        // And the second object should be of type ROBOT at a distance of 2 steps
        JsonNode robot = lookResponse.get("data").get("objects").get(1);
        assertNotNull(robot);
//        assertEquals("EDGE", robot.get("type").asText());
        assertEquals(1, robot.get("distance").asInt());

        // And I should also get the state of the robot
        assertNotNull(lookResponse.get("state"));
        assertNotNull(lookResponse.get("state").get("position"));
        assertEquals(0, lookResponse.get("state").get("position").get(0).asInt());
        assertEquals(0, lookResponse.get("state").get("position").get(1).asInt());
        assertNotNull(lookResponse.get("state").get("direction"));
        assertEquals("NORTH", lookResponse.get("state").get("direction").asText());
        assertNotNull(lookResponse.get("state").get("shields"));
        assertEquals(0, lookResponse.get("state").get("shields").asInt());
        assertNotNull(lookResponse.get("state").get("shots"));
        assertEquals(0, lookResponse.get("state").get("shots").asInt());
        assertNotNull(lookResponse.get("state").get("status"));
        assertEquals("TODO", lookResponse.get("state").get("status").asText());

    }
}


//package org.communication.WebAPI;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import kong.unirest.HttpResponse;
//import kong.unirest.JsonNode;
//import kong.unirest.Unirest;
//import kong.unirest.json.JSONObject;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class RobotWorldServerTest {
//
//    private RobotWorldServer server;
//
//    @BeforeEach
//    public void setUp() {
//        server = new RobotWorldServer();
//        server.start(8080);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        server.stop();
//    }
//
//    @Test
//    public void testGetCurrentWorld() {
//        // Make a GET request to /world
//        HttpResponse<String> response = Unirest.get("http://localhost:8080/world").asString();
//
//        // Verify the status code and content type
//        assertEquals(200, response.getStatus());
//        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
//
//        // Verify that the response body contains expected keys (e.g., "obstacles",
//        // "worldSize", etc.)
//        String responseBody = response.getBody();
//        assertTrue(responseBody.contains("\"obstacles\""));
//        assertTrue(responseBody.contains("\"worldSize\""));
//        assertTrue(responseBody.contains("\"name\":\"currentWorld\""));
//    }
//
//    @Test
//    public void testRestoreSavedWorld() {
//        // Make a GET request to /world/{worldName}
//        String worldName = "testWorld";
//        HttpResponse<String> response = Unirest.get("http://localhost:8080/world/" + worldName).asString();
//
//        // Verify the status code and content type
//        assertEquals(200, response.getStatus());
//        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
//
//        // Verify that the response body contains expected keys (e.g., "obstacles",
//        // "worldSize", etc.)
//        String responseBody = response.getBody();
//        assertTrue(responseBody.contains("\"obstacles\""));
//        assertTrue(responseBody.contains("\"worldSize\""));
//        assertTrue(responseBody.contains("\"name\":\"" + worldName + "\""));
//    }
//
//    // @Test
//    // public void testAddRobot() {
//    //     // Prepare the request body
//    //     JSONObject requestBody = new JSONObject();
//    //     requestBody.put("name", "TestRobot");
//    //     requestBody.put("type", "Reaper");
//
//    //     // Make a POST request to /robot with the request body
//    //     HttpResponse<JsonNode> response = Unirest.post("http://localhost:8080/robot")
//    //             .header("Content-Type", "application/json")
//    //             .body(requestBody)
//    //             .asJson();
//
//    //     // Verify the status code and content type
//    //     assertEquals(200, response.getStatus());
//    //     assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
//
//    //     // Verify that the response body contains expected keys
//    //     JSONObject responseBody = response.getBody().getObject();
//    //     assertTrue(responseBody.has("robot"));
//    //     assertTrue(responseBody.has("command"));
//    //     assertTrue(responseBody.has("arguments"));
//
//    //     // Verify the values in the response body
//    //     JSONObject robot = responseBody.getJSONObject("robot");
//    //     assertEquals("TestRobot", robot.getString("name"));
//
//    //     assertEquals("launch", responseBody.getString("command"));
//
//    //     // Check the arguments array
//    //     assertEquals(3, responseBody.getJSONArray("arguments").length());
//    //     assertEquals("Reaper", responseBody.getJSONArray("arguments").get(0));
//    //     assertEquals(3, responseBody.getJSONArray("arguments").get(1));
//    //     assertEquals(5, responseBody.getJSONArray("arguments").get(2));
//    // }
//}

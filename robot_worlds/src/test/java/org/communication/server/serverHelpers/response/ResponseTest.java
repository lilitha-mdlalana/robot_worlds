package org.communication.server.serverHelpers.response;

import org.communication.Domain.response.Response;
import org.communication.Infrastructure.robot.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    private Response response;
    private State state;

    @BeforeEach
    void setUp() {
        state = new State(10, 5); // Example values for shots and shields
        response = new Response();
    }

    @Test
    void testGettersAndSetters() {
        // Test setResult and getResult methods
        response.setResult("Success");
        assertEquals(response.getResult(), "Success");

        // Test setData and getData methods
        Map<String, Object> testData = new HashMap<>();
        testData.put("key1", "value1");
        testData.put("key2", 123);
        response.setData(testData);
        assertEquals(response.getData(), testData);

        // Test setState and getState methods
        response.setState(state);
        assertEquals(response.getState(), state);
    }

    @Test
    void testDefaultValues() {
        // Ensure default values are null initially
        assertNull(response.getResult());
        assertNull(response.getData());
        assertNull(response.getState());
    }
}
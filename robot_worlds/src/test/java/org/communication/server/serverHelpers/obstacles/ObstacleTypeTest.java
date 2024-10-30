package org.communication.server.serverHelpers.obstacles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.communication.Infrastructure.obstacles.ObstacleType;

public class ObstacleTypeTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        String direction = "north";
        String type = "mountain";
        int distance = 10;

        // When
        ObstacleType obstacleType = new ObstacleType(direction, type, distance);

        // Then
        assertEquals(obstacleType.getDirection(), direction);
        assertEquals(obstacleType.getType(), type);
        assertEquals(obstacleType.getDistance(), distance);
    }

    @Test
    void testSetters() {
        // Given
        ObstacleType obstacleType = new ObstacleType("north", "mountain", 10);

        // When
        obstacleType.setDirection("east");
        obstacleType.setType("pit");
        obstacleType.setDistance(15);

        // Then
        assertEquals(obstacleType.getDirection(), "east");
        assertEquals(obstacleType.getType(), "pit");
        assertEquals(obstacleType.getDistance(), 15);
    }
}

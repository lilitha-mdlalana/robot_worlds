package org.communication.server.serverHelpers.obstacles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.communication.Infrastructure.obstacles.Obstacle;

public class ObstacleTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        int x = 5;
        int y = 10;

        // When
        Obstacle obstacle = new Obstacle(x, y);

        // Then
        assertEquals(obstacle.getX(), x);
        assertEquals(obstacle.getY(), y);
    }

    @Test
    void testToString() {
        // Given
        Obstacle obstacle = new Obstacle(3, 7);

        // When
        String toStringResult = obstacle.toString();

        // Then
        assertEquals(toStringResult, "Obstacles: - At position (3, 7)");
    }
}

package org.communication.server.serverHelpers.obstacles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.communication.Infrastructure.obstacles.Lake;

public class LakeTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        int x = 5;
        int y = 10;

        // When
        Lake lake = new Lake(x, y);

        // Then
        assertEquals(lake.getX(), x);
        assertEquals(lake.getY(), y);
    }

    @Test
    void testToString() {
        // Given
        Lake lake = new Lake(3, 7);

        // When
        String toStringResult = lake.toString();

        // Then
        assertEquals(toStringResult, "Lake: - At position (3, 7)");
    }
}

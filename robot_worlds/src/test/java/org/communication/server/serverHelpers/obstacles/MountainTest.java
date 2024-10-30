package org.communication.server.serverHelpers.obstacles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.communication.Infrastructure.obstacles.Mountain;

public class MountainTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        int x = 5;
        int y = 10;

        // When
        Mountain mountain = new Mountain(x, y);

        // Then
        assertEquals(mountain.getX(), x);
        assertEquals(mountain.getY(), y);
    }

    @Test
    void testToString() {
        // Given
        Mountain mountain = new Mountain(3, 7);

        // When
        String toStringResult = mountain.toString();

        // Then
        assertEquals(toStringResult, "Mountain: - At position (3, 7)");
    }
}

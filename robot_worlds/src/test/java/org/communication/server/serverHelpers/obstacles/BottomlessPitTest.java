package org.communication.server.serverHelpers.obstacles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.communication.Infrastructure.obstacles.BottomLessPit;

public class BottomlessPitTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        int x = 5;
        int y = 10;

        // When
        BottomLessPit pit = new BottomLessPit(x, y);

        // Then
        assertEquals(pit.getX(), x);
        assertEquals(pit.getY(), y);
    }

    @Test
    void testToString() {
        // Given
        BottomLessPit pit = new BottomLessPit(3, 7);

        // When
        String toStringResult = pit.toString();

        // Then
        assertEquals(toStringResult, "Bottomless Pit: - At position (3, 7)");
    }
}

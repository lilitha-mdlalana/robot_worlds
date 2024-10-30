package org.communication.server.serverHelpers.robot;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.communication.Infrastructure.robot.State;

public class RobotStateTest {

    @Test
    public void testDecrementShots() {
        State state = new State(5); // Initialize with 5 shots

        // Decrement shots once
        state.decrementShots();

        // Verify that shots have been decremented by 1
        assertEquals(4, state.getShots());

        // Decrement shots until it reaches 0
        for (int i = 0; i < 4; i++) {
            state.decrementShots();
        }

        // Verify that shots have reached 0
        assertEquals(0, state.getShots());

        // Decrement shots again (should stay at 0)
        state.decrementShots();

        // Verify that shots remain at 0
        assertEquals(0, state.getShots());
    }

    @Test
    public void testDecrementShield() {
        State state = new State(3, 0); // Initialize with 3 shields

        // Decrement shield once
        state.decrementShield();

        // Verify that shields have been decremented by 1
        assertEquals(2, state.getShields());

        // Decrement shield until it reaches 0
        for (int i = 0; i < 2; i++) {
            state.decrementShield();
        }

        // Verify that shields have reached 0
        assertEquals(0, state.getShields());

        // Decrement shield again (should stay at 0)
        state.decrementShield();

        // Verify that shields remain at 0
        assertEquals(0, state.getShields());
    }

    // You can add more test cases to cover other methods and scenarios as needed
}



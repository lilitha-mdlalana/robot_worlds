package org.communication.server.serverHelpers.world;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.communication.Infrastructure.world.Config;

class ConfigTest {

    @AfterAll
    public static void Clean(){
        Config.setTopLeftX_world(-100);
        Config.setTopLeftY_world(100);
        Config.setBottomRightX_world(100);
        Config.setBottomRightY_world(-100);

    }

//    @Test
//    void testDefaultValues() {
//        assertEquals(-200, Config.getTopLeftX_world());
//        assertEquals(200, Config.getTopLeftY_world());
//        assertEquals(200, Config.getBottomRightX_world());
//    }

    @Test
    void testSetTopLeftX_world() {
        Config.setTopLeftX_world(-100);
        assertEquals(-100, Config.getTopLeftX_world());
    }

    @Test
    void testSetTopLeftY_world() {
        Config.setTopLeftY_world(150);
        assertEquals(150, Config.getTopLeftY_world());
    }

    @Test
    void testSetBottomRightX_world() {
        Config.setBottomRightX_world(250);
        assertEquals(250, Config.getBottomRightX_world());
    }

    @Test
    void testSetBottomRightY_world() {
        Config.setBottomRightY_world(-250);
        assertEquals(-250, Config.getBottomRightY_world());
    }

    @Test
    void testSetVisibility() {
        Config.setVisibility(100);
        assertEquals(100, Config.getVisibility());
    }

    @Test
    void testSetReloadTime() {
        Config.setReloadTime(10000);
        assertEquals(10000, Config.getReloadTime());
    }

    @Test
    void testSetRepairTime() {
        Config.setRepairTime(8000);
        assertEquals(8000, Config.getRepairTime());
    }
}
package org.communication.server.serverHelpers.robotModels;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.communication.Infrastructure.robotModels.Blaze;
import org.communication.Infrastructure.robotModels.Demolisher;
import org.communication.Infrastructure.robotModels.Reaper;
import org.communication.Infrastructure.robotModels.Venom;
import org.communication.Infrastructure.robotModels.Warpath;

public class RobotModelsTest {

    @Test
    public void testBlazeConstructor() {
        Blaze blaze = new Blaze();

        assertEquals("16", blaze.getShield());
        assertEquals("20", blaze.getShots());
    }

    @Test
    public void testGetShieldBlaze() {
        Blaze blaze = new Blaze();

        assertEquals("16", blaze.getShield());
    }

    @Test
    public void testGetShotsBlaze() {
        Blaze blaze = new Blaze();

        assertEquals("20", blaze.getShots());
    }
    @Test
    public void testDemolisherConstructor() {
        Demolisher demolisher = new Demolisher();

        assertEquals("14", demolisher.getShield());
        assertEquals("20", demolisher.getShots());
    }


    @Test
    public void testGetShieldDemolisher() {
        Demolisher demolisher = new Demolisher();

        assertEquals("14", demolisher.getShield());
    }

    @Test
    public void testGetShotsDemolisher() {
        Demolisher demolisher = new Demolisher();

        assertEquals("20", demolisher.getShots());
    }

    @Test
    public void testReaperConstructorReaper() {
        Reaper reaper = new Reaper();

        assertEquals("15", reaper.getShield());
        assertEquals("15", reaper.getShots());
    }

    @Test
    public void testGetShieldReaper() {
        Reaper reaper = new Reaper();

        assertEquals("15", reaper.getShield());
    }

    @Test
    public void testGetShotsReaper() {
        Reaper reaper = new Reaper();

        assertEquals("15", reaper.getShots());
    }
    @Test
    public void testVenomConstructorVenom() {
        Venom venom = new Venom();

        assertEquals("20", venom.getShield());
        assertEquals("16", venom.getShots());
    }

    @Test
    public void testGetShieldVenom() {
        Venom venom = new Venom();

        assertEquals("20", venom.getShield());
    }

    @Test
    public void testGetShotsVenom() {
        Venom venom = new Venom();

        assertEquals("16", venom.getShots());
    }

    @Test
    public void testWarpathConstructorWarpath() {
        Warpath warpath = new Warpath();

        assertEquals("18", warpath.getShield());
        assertEquals("16", warpath.getShots());
    }

    @Test
    public void testGetShieldWarpath() {
        Warpath warpath = new Warpath();

        assertEquals("18", warpath.getShield());
    }

    @Test
    public void testGetShotsWarpath() {
        Warpath warpath = new Warpath();

        assertEquals("16", warpath.getShots());
    }

}

package org.communication.server.serverHelpers.world;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.communication.Database.SaveWorld;
import org.communication.Infrastructure.world.Config;
import org.communication.Infrastructure.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SaveTest {

    private static final String DB_URL = "jdbc:sqlite:robot_worlds.db"; // Use a test database

    @BeforeEach
    public void setUp() throws SQLException {
        // Clear the test database before each test
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM World_Size WHERE name = 'TestWorld2'")) {
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Obstacles WHERE world_name = 'TestWorld2'")) {
                pstmt.executeUpdate();
            }
        }

        // Set up a dummy world state
        Config.setTopLeftX_world(-100);
        Config.setTopLeftY_world(100);
        Config.setBottomRightX_world(100);
        Config.setBottomRightY_world(-100);


    }

    @Test
    public void testSaveWorld() throws SQLException {
        World world = World.getInstance();
        world.clearObstacles();
        world.addObstacle("Lake", 10, 10);        // Execute the save
        SaveWorld.saveWorld(world,"TestWorld2",true);

        // Validate that the world size was saved correctly
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM World_Size WHERE name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, "TestWorld2");
                ResultSet rs = pstmt.executeQuery();

                assertTrue(rs.next());
                assertEquals(-100, rs.getInt("top_left_x"));
                assertEquals(100, rs.getInt("top_left_y"));
                assertEquals(100, rs.getInt("bottom_right_x"));
                assertEquals(-100, rs.getInt("bottom_right_y"));
            }

            // Validate that the obstacle was saved correctly
            String queryObstacles = "SELECT * FROM Obstacles WHERE world_name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(queryObstacles)) {
                pstmt.setString(1, "TestWorld2");
                ResultSet rs = pstmt.executeQuery();

                assertTrue(rs.next());
                assertEquals("Lake", rs.getString("type"));
                assertEquals(10, rs.getInt("x"));
                assertEquals(10, rs.getInt("y"));
            }
        }
    }
}
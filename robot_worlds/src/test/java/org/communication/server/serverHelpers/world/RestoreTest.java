package org.communication.server.serverHelpers.world;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.communication.Database.DatabaseHelper;
import org.communication.Database.RestoreWorld;
import org.communication.Infrastructure.world.Config;
import org.communication.Infrastructure.world.World;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RestoreTest {

    public static final String DB_URL = "jdbc:sqlite:robot_worlds.db"; // Use DatabaseHelper's DATABASE_URL directly

    @BeforeEach
    public void setUp() throws SQLException {
        // Clear the test database before each test
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM World_Size WHERE name = 'TestWorld'")) {
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Obstacles WHERE world_name = 'TestWorld'")) {
                pstmt.executeUpdate();
            }
        }
    }

    @Test
    public void testRestoreWorld() throws SQLException {
        // Prepopulate the database with test data
        try (Connection connection = DriverManager.getConnection(DB_URL)) {


            try (PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO World_Size (name, top_left_x, top_left_y, bottom_right_x, bottom_right_y) VALUES (?, ?, ?, ?, ?)")) {
                pstmt.setString(1, "TestWorld");
                pstmt.setInt(2, -100);
                pstmt.setInt(3, 100);
                pstmt.setInt(4, 100);
                pstmt.setInt(5, -100);
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO Obstacles (world_name, type, x, y) VALUES (?, ?, ?, ?)")) {
                pstmt.setString(1, "TestWorld");
                pstmt.setString(2, "Obstacle");
                pstmt.setInt(3, 0);
                pstmt.setInt(4, 0);
                pstmt.executeUpdate();
            }
        }
        World world = World.getCurrentWorld();
        RestoreWorld.restoreWorld("TestWorld",true);

        // Validate the restored world size
        assertEquals(-100, Config.getTopLeftX_world());
        assertEquals(100, Config.getTopLeftY_world());
        assertEquals(100, Config.getBottomRightX_world());
        assertEquals(-100, Config.getBottomRightY_world());

        ArrayList<Object> obstacles = world.getObstaclesList();
        assertNotNull(obstacles);
        assertEquals(1, obstacles.size());


    }
}

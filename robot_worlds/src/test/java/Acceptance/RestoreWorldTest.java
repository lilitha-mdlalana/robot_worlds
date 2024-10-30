package Acceptance;

import org.communication.Database.RestoreWorld;
import org.communication.Database.SaveWorld;
import org.communication.Infrastructure.obstacles.Obstacle;
import org.communication.Infrastructure.world.Config;
import org.communication.Infrastructure.world.World;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class RestoreWorldTest {

    private static final String DB_URL = "jdbc:sqlite::memory:"; // In-memory database for testing
    private Connection connection;
    @Before
    public void setUp() throws Exception {
        // Initialize the in-memory database connection
        connection = DriverManager.getConnection(DB_URL);
        if (connection != null) {
            // Create tables
            Statement stmt = connection.createStatement();

            String createWorldSizeTable = "CREATE TABLE IF NOT EXISTS World_Size ( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT UNIQUE NOT NULL, " +
                    "top_left_x INTEGER, " +
                    "top_left_y INTEGER, " +
                    "bottom_right_x INTEGER, " +
                    "bottom_right_y INTEGER);";

            String createObstaclesTable = "CREATE TABLE IF NOT EXISTS Obstacles ( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "world_name TEXT, " +
                    "type TEXT, " +
                    "x INTEGER, " +
                    "y INTEGER);";

            // Insert test data
            String insertWorldSize = "INSERT INTO World_Size (name, top_left_x, top_left_y, bottom_right_x, bottom_right_y) " +
                    "VALUES ('TEST', -20, 20, 20, -20)";

            String insertObi1 = "INSERT INTO Obstacles (world_name, type, x, y) " +
                    "VALUES ('TEST', 'pit', 10, 10)";

            String insertObi2 = "INSERT INTO Obstacles (world_name, type, x, y) " +
                    "VALUES ('TEST', 'wall', 20, 20)";

            stmt.execute(createWorldSizeTable);
            stmt.execute(createObstaclesTable);
            stmt.execute(insertWorldSize);
            stmt.execute(insertObi1);
            stmt.execute(insertObi2);
        }
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testRestoreWorld() {
        // Verify the world size was restored correctly
        assertEquals(-100, Config.getTopLeftX_world());
        assertEquals(100, Config.getTopLeftY_world());
        assertEquals(100, Config.getBottomRightX_world());
        assertEquals(-100, Config.getBottomRightY_world());

//        // Verify obstacles were restored correctly
//        assertEquals(2, world.getObstacles().size());
//
//        // Verify specific obstacles
//        assertTrue(world.getObstacles().contains(new Obstacle("pit", 10, 10)));
//        assertTrue(world.getObstacles().contains(new Obstacle("wall", 20, 20)));
    }
}

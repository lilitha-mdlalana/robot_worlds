package Acceptance;//package Acceptance;
//
//import org.communication.server.serverHelpers.robot.Position;
//import org.communication.server.serverHelpers.world.DatabaseHelper;
//import org.communication.server.serverHelpers.world.SaveWorld;
//import org.communication.server.serverHelpers.world.World;
//import org.junit.jupiter.api.*;
//import java.sql.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class SaveWorldTests {
//
//    private static final String DATABASE_URL =  "jdbc:sqlite:robot_worlds.db";
//    private SaveWorld saveWorld;
//
//    @BeforeEach
//    public void setup() {
//
//        // Set up database and SaveWorld instance
//        DatabaseHelper.CreateDatabase();
//        saveWorld = SaveWorld.getInstance();
//
//        // Clear database tables before each test
//        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
//            Statement stmt = conn.createStatement();
//            stmt.execute("DELETE FROM World_Size;");
//            stmt.execute("DELETE FROM Obstacles;");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Test
//    @Order(1)
//    public void testSaveWorldBoundaries() {
//
//        // Create a world with specific boundaries
//        World world = new World(new Position(0,0), new Position(100,100));
//
//        // Save the World
//        saveWorld.saveWorld(world);
//
//        // Verify world boundaries saved in the World_Size table
//        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM World_size");
//
//
//            assertEquals(0, rs.getInt("top_left_x"));
//            assertEquals(0, rs.getInt("top_left_y"));
//            assertEquals(0, rs.getInt("bottom_left_x"));
//            assertEquals(0, rs.getInt("bottom_left_y"));
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
//
//

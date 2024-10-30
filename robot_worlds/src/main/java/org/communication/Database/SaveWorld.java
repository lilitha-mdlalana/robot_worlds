package org.communication.Database;
import org.communication.Infrastructure.obstacles.*;
import org.communication.Infrastructure.world.World;
import java.sql.*;

public class SaveWorld {

    private static SaveWorld instance;
    private static String DATABASE_URL = "jdbc:sqlite:robot_worlds/robot_worlds.db";


    public static SaveWorld getInstance(){

        if (instance == null) {
            synchronized (SaveWorld.class) {
                if (instance == null) {
                    instance = new SaveWorld();
                }
            }
        }
        return instance;
    }
    public static void saveWorld(World world, String worldName,Boolean Testing) {
        if (Testing == true){
            DATABASE_URL="jdbc:sqlite:robot_worlds.db";
        }
        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            conn.setAutoCommit(false);

            // Check if a world with the same name already exists
            String checkWorldSQL = "SELECT id FROM World_Size WHERE name = ?";
            try (PreparedStatement pstmtCheck = conn.prepareStatement(checkWorldSQL)) {
                pstmtCheck.setString(1, worldName);
                ResultSet rs = pstmtCheck.executeQuery();
                if (rs.next()) {
                    // Handle the case where the world name already exists
                    // For example, overwrite the existing world:
                    deleteWorld(conn, worldName); // Custom method to delete the existing world before saving the new one
                }
            }

            // Insert or update the world size data
            String insertWorldSQL = "INSERT INTO World_Size (name, top_left_x, top_left_y, bottom_right_x, bottom_right_y) " +
                    "VALUES (?, ?, ?, ?, ?);";

            try (PreparedStatement pstmt = conn.prepareStatement(insertWorldSQL)) {
                pstmt.setString(1, worldName);
                pstmt.setInt(2, world.getTopLeft().getX());
                pstmt.setInt(3, world.getTopLeft().getY());
                pstmt.setInt(4, world.getBottomRight().getX());
                pstmt.setInt(5, world.getBottomRight().getY());
                pstmt.executeUpdate();
            }

            // Insert obstacle data
            String insertObstacleSQL = "INSERT INTO Obstacles (world_name, type, x, y) VALUES (?, ?, ?, ?);";

            try (PreparedStatement pstmt = conn.prepareStatement(insertObstacleSQL)) {
                for (Object obs : world.obstacles) {
                    pstmt.setString(1, worldName);
                    pstmt.setString(2, world.getObstacleType(obs)); // Set the obstacle type
                    pstmt.setInt(3, getX(obs)); // Set the X coordinate of the obstacle
                    pstmt.setInt(4, getY(obs)); // Set the Y coordinate of the obstacle
                    pstmt.addBatch(); // Add this insertion to the batch for execution
                }
                pstmt.executeBatch(); // Execute the batch of obstacle insertions
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Helper method to get the X coordinate from different types of obstacles
    private static int getX(Object obstacle) {
        if (obstacle instanceof Obstacle) return ((Obstacle) obstacle).getX(); // Generic Obstacle
        if (obstacle instanceof Mountain) return ((Mountain) obstacle).getX(); // Mountain type obstacle
        if (obstacle instanceof Lake) return ((Lake) obstacle).getX(); // Lake type obstacle
        if (obstacle instanceof BottomLessPit) return ((BottomLessPit) obstacle).getX(); // BottomLessPit type obstacle
        return -1; // Return -1 if the obstacle type is unrecognized
    }

    // Helper method to get the Y coordinate from different types of obstacles
    private static int getY(Object obstacle) {
        if (obstacle instanceof Obstacle) return ((Obstacle) obstacle).getY(); // Generic Obstacle
        if (obstacle instanceof Mountain) return ((Mountain) obstacle).getY(); // Mountain type obstacle
        if (obstacle instanceof Lake) return ((Lake) obstacle).getY(); // Lake type obstacle
        if (obstacle instanceof BottomLessPit) return ((BottomLessPit) obstacle).getY(); // BottomLessPit type obstacle
        return -1; // Return -1 if the obstacle type is unrecognized
    }


    private static void deleteWorld(Connection conn, String worldName) throws SQLException {
        try (PreparedStatement pstmtDeleteWorld = conn.prepareStatement("DELETE FROM World_Size WHERE name = ?")) {
            pstmtDeleteWorld.setString(1, worldName);
            pstmtDeleteWorld.executeUpdate();
        }
        try (PreparedStatement pstmtDeleteObstacles = conn.prepareStatement("DELETE FROM Obstacles WHERE world_name = ?")) {
            pstmtDeleteObstacles.setString(1, worldName);
            pstmtDeleteObstacles.executeUpdate();
        }
    }


}

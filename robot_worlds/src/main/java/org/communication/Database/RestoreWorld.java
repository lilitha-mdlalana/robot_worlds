package org.communication.Database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.communication.Infrastructure.world.Config;
import org.communication.Infrastructure.world.World;

public class RestoreWorld {
    private static String DB_URL = "jdbc:sqlite:robot_worlds/robot_worlds.db"; // Update with your database path

    public static void restoreWorld(String worldName,boolean Testing) {
        if (Testing == true){
            DB_URL="jdbc:sqlite:robot_worlds.db";
        }
        try (Connection connection = DriverManager.getConnection(DB_URL)) {

            // Clear current world state
            World world = World.getInstance();
            world.clearObstacles();

            // Restore world size
            String queryWorldSize = "SELECT top_left_x, top_left_y, bottom_right_x, bottom_right_y FROM World_Size WHERE name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(queryWorldSize)) {
                pstmt.setString(1, worldName);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int topLeftX = rs.getInt("top_left_x");
                    int topLeftY = rs.getInt("top_left_y");
                    int bottomRightX = rs.getInt("bottom_right_x");
                    int bottomRightY = rs.getInt("bottom_right_y");

                    Config.setTopLeftX_world(topLeftX);
                    Config.setTopLeftY_world(topLeftY);
                    Config.setBottomRightX_world(bottomRightX);
                    Config.setBottomRightY_world(bottomRightY);
                }
            }

            // Restore obstacles
            String queryObstacles = "SELECT type, x, y FROM Obstacles WHERE world_name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(queryObstacles)) {
                pstmt.setString(1, worldName);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    String type = rs.getString("type");
                    int x = rs.getInt("x");
                    int y = rs.getInt("y");
                    world.addObstacle(type, x, y);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error restoring world: " + e.getMessage());
        }
    }

}
package org.communication.Database;

import java.sql.*;

public class DatabaseHelper {

    // url to tell java where to find SQLite database file, if file does not exist, SQLite creates it
    public static final String DATABASE_URL = "jdbc:sqlite:robot_worlds/robot_worlds.db";
    public String Database(){
        return this.DATABASE_URL;
    }
    //create database tables
    public static void CreateDatabase() {

        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            if (conn != null) {

                String createWorldSizeTable = " CREATE TABLE IF NOT EXISTS World_Size ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +// 'id' column, automatically increases for every new entry
                        "name TEXT UNIQUE NOT NULL, " + // 'name' column for unique world names
                        "top_left_x INTEGER, " +                                    // 'top_left_x' column , stores x-coord of top-left corner
                        "top_left_y INTEGER, " +                                    // 'top_left_y' column , stores y-coord of top-left corner
                        "bottom_right_x INTEGER, " +                                // 'top_right_x' column , stores x-coord of bottom-right corner
                        "bottom_right_y INTEGER);" ;                                // 'top_right_y' column , stores y-coord of bottom-right corner

                String createObstaclesTable = "CREATE TABLE IF NOT EXISTS Obstacles ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +// 'id' column, automatically increases for every new entry
                        "world_name TEXT, " +  // 'world_name' column to associate with the correct world
                        "type TEXT, " +                                               // 'type' column, stores the type of obstacle
                        "x INTEGER, " +                                               // 'x' column, stores X-coord of the obstacle's position
                        "y INTEGER);" ;                                               // 'y' column, stores Y-coord of the obstacle's position

                //create statement object to execute SQL statements
                Statement stmt = conn.createStatement();
                stmt.execute(createWorldSizeTable);
                stmt.execute(createObstaclesTable);

                //Debug print to console
                System.out.println("Tables created successfully!");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        CreateDatabase();
    }
}

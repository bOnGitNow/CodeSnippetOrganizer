package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // The file snippets.db will be created in your project root folder
    private static final String URL = "jdbc:sqlite:snippets.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
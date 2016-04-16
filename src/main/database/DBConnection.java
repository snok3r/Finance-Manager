package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private final String dbName;
    private final String dbType;

    /**
     * Sets up database name and database type
     * for future connection
     *
     * @param dbName database file name (eg. "music.db")
     * @param dbType database type (eg. "sqlite", "oracle")
     */
    public DBConnection(String dbName, String dbType) {
        this.dbName = dbName;
        this.dbType = dbType;
    }

    /**
     * Method to get database name
     *
     * @return returns set database name
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Method to get database type
     *
     * @return returns set database type
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * Method to get set connection
     *
     * @return connection to database with set <tt>dbName</tt> and <tt>dbType</tt>
     * @throws SQLException if couldn't connect to <tt>dbName</tt> with <tt>dbType</tt>
     */
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org." + dbType + ".JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection("jdbc:" + dbType + ":src/db/" + dbName);
    }
}

package main.database;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import main.java.Account;
import main.java.Record;
import main.java.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DBHelper implements DataStore {

    private Connection con;
    private PreparedStatement prStmt;
    private Statement stmt;
    private ResultSet rs;

    private static final DBHelper instance = new DBHelper().init();

    private DBHelper() {
    }

    /**
     * Initializes database creating (if not exists) tables
     *
     * @return this
     */
    private DBHelper init() {
        connect();
        try {
            executeSQL("src/sql/create_tables.sql");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();

        return this;
    }

    /**
     * @return instance of the class
     */
    public static DBHelper getInstance() {
        return instance;
    }

    /**
     * Connects to database
     */
    public void connect() {
        try {
            con = new DBConnection("test_finance.db", "sqlite").getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes ResultSet, Statements and Connection
     */
    public void close() {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if connection is opened. Returns false if
     * connection is null.
     *
     * @return true if connection isn't closed, false if connection
     * is closed, null or SQLException was thrown.
     */
    private boolean isConnectionOpened() {
        if (con == null)
            return false;

        try {
            return !con.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Executes sql query from the <tt>path</tt>
     *
     * @param path path to the file with sql query
     * @return true if execution went well, false if it isn't
     * @throws IOException
     * @throws SQLException
     */
    public boolean executeSQL(String path) throws IOException, SQLException {
        if (!isConnectionOpened())
            return false;

        boolean success = false;
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = in.readLine()) != null) {
                sb.append(str + "\n ");
            }
            in.close();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sb.toString());
            stmt.close();
            success = true;
        } catch (Exception e) {
            System.err.println("Failed to Execute " + path + ". The error is " + e.getMessage());
        }
        return success;
    }

    @Nullable
    @Override
    public User getUser(String name) {
        if (!isConnectionOpened())
            return null;

        return null;
    }

    @NotNull
    @Override
    public Set<String> getUserNames() {
        if (!isConnectionOpened())
            return new HashSet<>();

        Set<String> result = new HashSet<>();
        return result;
    }

    @NotNull
    @Override
    public Set<Account> getAccounts(User owner) {
        if (!isConnectionOpened())
            return new HashSet<>();

        Set<Account> result = new HashSet<>();
        return result;
    }

    @NotNull
    @Override
    public Set<Record> getRecords(Account account) {
        if (!isConnectionOpened())
            return new HashSet<>();

        Set<Record> result = new HashSet<>();
        return result;
    }

    @Override
    public void addUser(User user) {
        if (!isConnectionOpened())
            return;
    }

    @Override
    public void addAccount(User user, Account account) {
        if (!isConnectionOpened())
            return;
    }

    @Override
    public void addRecord(Account account, Record record) {
        if (!isConnectionOpened())
            return;
    }

    @Nullable
    @Override
    public User removeUser(String name) {
        if (!isConnectionOpened())
            return null;

        return null;
    }

    @Nullable
    @Override
    public Account removeAccount(User owner, Account account) {
        if (!isConnectionOpened())
            return null;

        return null;
    }

    @Nullable
    @Override
    public Record removeRecord(Account from, Record record) {
        if (!isConnectionOpened())
            return null;

        return null;
    }
}

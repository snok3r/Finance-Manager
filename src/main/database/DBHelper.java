package main.database;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import main.java.Account;
import main.java.Record;
import main.java.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DBHelper implements DataStore {

    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;

    private static final DBHelper instance = new DBHelper();

    private DBHelper() {
    }

    public DBHelper getInstance() {
        return instance;
    }

    public void connect() {
        try {
            con = new DBConnection("test_finance.db", "sqlite").getConnection();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void close() {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean isConnectionOpened() {
        if (con == null)
            return false;

        try {
            return con.isClosed();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Nullable
    @Override
    public User getUser(String name) {
        return null;
    }

    @NotNull
    @Override
    public Set<String> getUserNames() {
        Set<String> result = new HashSet<>();
        return result;
    }

    @NotNull
    @Override
    public Set<Account> getAccounts(User owner) {
        Set<Account> result = new HashSet<>();
        return result;
    }

    @NotNull
    @Override
    public Set<Record> getRecords(Account account) {
        Set<Record> result = new HashSet<>();
        return result;
    }

    @Override
    public void addUser(User user) {
    }

    @Override
    public void addAccount(User user, Account account) {
    }

    @Override
    public void addRecord(Account account, Record record) {
    }

    @Nullable
    @Override
    public User removeUser(String name) {
        return null;
    }

    @Nullable
    @Override
    public Account removeAccount(User owner, Account account) {
        return null;
    }

    @Nullable
    @Override
    public Record removeRecord(Account from, Record record) {
        return null;
    }
}

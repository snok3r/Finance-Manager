package main.database;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import main.java.Account;
import main.java.Record;
import main.java.User;
import main.util.Category;
import main.util.RecordType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
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
     * Connects to database.
     * Returns if connection is opened.
     */
    public void connect() {
        if (isConnectionOpened())
            return;

        try {
            con = new DBConnection("test_finance.db", "sqlite").getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes ResultSet, Statements and Connection.
     * Returns if connection is closed.
     */
    public void close() {
        if (isConnectionClosed())
            return;

        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (prStmt != null) prStmt.close();
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

    private boolean isConnectionClosed() {
        return !isConnectionOpened();
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
        if (isConnectionClosed())
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

    /**
     * Finding USER_ID for given <tt>user</tt>.
     *
     * @param user user to look USER_ID for
     * @return USER_ID of <tt>user</tt> or -1, if not found.
     */
    private int findUserId(User user) {
        int userId = -1;
        if (user == null)
            return userId;

        try {
            prStmt = con.prepareStatement("SELECT USER_ID, PASSWORD FROM users WHERE LOGIN = ?;");
            prStmt.setString(1, user.getLogin());
            rs = prStmt.executeQuery();

            String userPass;
            if (rs.next()) { // if user found, otherwise rs is empty
                userId = rs.getInt(1);
                userPass = rs.getString(2);
                if (!user.checkStringPassword(userPass)) // checking passwords
                    return -1;
            } else
                return -1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }

    /**
     * Finding ACCOUNT_ID for given <tt>account</tt>
     *
     * @param account account to look ACCOUNT_ID for
     * @return ACCOUNT_ID of <tt>account</tt> or null, if not found
     */
    @Nullable
    private String findAccountId(Account account) {
        if (account == null)
            return null;

        int userId = findUserId(account.getOwner());
        if (userId == -1)
            return null;

        String accountId = String.format("%s%s", userId, account.getDescription());
        return accountId;
    }

    /**
     * Finding RECORD_ID for given <tt>record</tt>
     * associated with <tt>account</tt>
     *
     * @param account account associated with the <tt>record</tt>
     * @param record  record to look RECORD_ID for
     * @return RECORD_ID of <tt>record</tt> or null, if not found
     */
    @Nullable
    private String findRecordId(Account account, Record record) {
        if (account == null || record == null)
            return null;

        String accountId = findAccountId(account);
        if (accountId == null)
            return null;

        String recordID = String.format("%s%d", accountId, record.getLongDate());
        return recordID;
    }

    /**
     * Method to get User object with given <tt>name</tt>
     *
     * @param name name of User to get
     * @return User object with <tt>name</tt> or null,
     * if not found
     */
    @Nullable
    @Override
    public User getUser(String name) {
        User user = null;
        if (isConnectionClosed())
            return user;
        if (name == null || !getUserNames().contains(name))
            return user;

        try {
            // reconstruct user with <tt>name</tt> and corresponding password
            prStmt = con.prepareStatement("SELECT PASSWORD FROM users WHERE LOGIN = ?;");
            prStmt.setString(1, name);
            rs = prStmt.executeQuery();

            if (rs.next()) {
                user = new User(name, "123456");

                Class c = user.getClass();
                Field pass = c.getDeclaredField("password");
                pass.setAccessible(true);
                pass.set(user, rs.getString(1));
                pass.setAccessible(false);
            }

            getAccounts(user)
                    .forEach(user::addAccount);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Method to get all the user's name
     *
     * @return Set of user's names (empty Set if didn't find any)
     */
    @NotNull
    @Override
    public Set<String> getUserNames() {
        Set<String> result = new HashSet<>(0);
        if (isConnectionClosed())
            return result;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT LOGIN FROM users;");

            while (rs.next())
                result.add(rs.getString(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Method to get accounts for <tt>owner</tt>.
     *
     * @param owner User for which you need his/her accounts
     * @return Set of accounts (empty set if no accounts found)
     */
    @NotNull
    @Override
    public Set<Account> getAccounts(User owner) {
        Set<Account> result = new HashSet<>(0);
        if (isConnectionClosed())
            return result;
        if (owner == null)
            return result;

        try {
            int userId = findUserId(owner);
            if (userId == -1)
                return result;

            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT DESCRIPTION FROM accounts WHERE USER_ID = '" + userId + "';");

            while (rs.next()) {
                String desc = rs.getString(1);
                Account account = new Account(owner, desc);

                getRecords(account)
                        .forEach(account::addRecord);

                result.add(account);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Method to get records for the <tt>account</tt>
     *
     * @param account account for which you need its records
     * @return Set of records (empty Set if no records found)
     */
    @NotNull
    @Override
    public Set<Record> getRecords(Account account) {
        Set<Record> result = new HashSet<>(0);
        if (isConnectionClosed())
            return result;
        if (account == null)
            return result;

        try {
            int userId = findUserId(account.getOwner());
            if (userId == -1)
                return result;

            String accountId = findAccountId(account);
            prStmt = con.prepareStatement("SELECT * FROM records WHERE ACCOUNT_ID = ?");
            prStmt.setString(1, accountId);
            rs = prStmt.executeQuery();

            while (rs.next()) {
                long date = rs.getLong(2);
                float amount = rs.getFloat(3);
                RecordType type = RecordType.valueOf(rs.getString(4));
                Category category = Category.valueOf(rs.getString(5));
                String desc = rs.getString(6);
                result.add(new Record(date, amount, type, category, desc));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Adds <tt>user</tt> to database if login is unique
     *
     * @param user user to add
     */
    @Override
    public void addUser(User user) {
        if (isConnectionClosed())
            return;
        if (user == null)
            return;

        try {
            int userId = findUserId(user);
            if (userId != -1) // user exists
                return;

            prStmt = con.prepareStatement("INSERT INTO users (LOGIN, PASSWORD) VALUES (?, ?);");
            prStmt.setString(1, user.getLogin());
            prStmt.setString(2, user.getPassword());
            prStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds <tt>account</tt> to <tt>user</tt> if
     * user exists and user doesn't yet have the <tt>account</tt>
     *
     * @param user    user to add the <tt>account</tt> to
     * @param account account to add
     */
    @Override
    public void addAccount(User user, Account account) {
        if (isConnectionClosed())
            return;
        if (user == null || account == null)
            return;

        try {
            int userId = findUserId(user);
            if (userId == -1)
                return;

            // inserting account
            String accountId = findAccountId(account);
            prStmt = con.prepareStatement("INSERT INTO accounts (ACCOUNT_ID, BALANCE, DESCRIPTION, USER_ID) " +
                    "VALUES (?, ?, ?, ?);");
            prStmt.setString(1, accountId);
            prStmt.setFloat(2, account.getBalance());
            prStmt.setString(3, account.getDescription());
            prStmt.setInt(4, userId);
            prStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds <tt>record</tt> to <tt>account</tt> if the
     * <tt>account</tt> exists and record is unique.
     *
     * @param account account to add <tt>record</tt> to
     * @param record  record to add
     */
    @Override
    public void addRecord(Account account, Record record) {
        if (isConnectionClosed())
            return;

        if (account == null || record == null)
            return;

        try {
            int userId = findUserId(account.getOwner());
            if (userId == -1)
                return;

            // if we found the account, then adding record to it
            String accountId = findAccountId(account);
            String recordID = findRecordId(account, record);
            prStmt = con.prepareStatement("INSERT INTO records (RECORD_ID, RECORD_DATE, AMOUNT, RECORD_TYPE, CATEGORY, DESCRIPTION, ACCOUNT_ID)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);");
            prStmt.setString(1, recordID);
            prStmt.setDate(2, record.getDate());
            prStmt.setFloat(3, record.getAmount());
            prStmt.setString(4, record.getType().name());
            prStmt.setString(5, record.getCategory().name());
            prStmt.setString(6, record.getDescription());
            prStmt.setString(7, accountId);
            prStmt.executeUpdate();

            float amount = 0;
            if (record.getType() == RecordType.WITHDRAW)
                amount = -record.getAmount();
            else if (record.getType() == RecordType.DEPOSIT)
                amount = record.getAmount();

            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE accounts SET BALANCE = BALANCE + " + amount + " WHERE ACCOUNT_ID = '" + accountId + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes user by <tt>name</tt>. Returns removed
     * user with <tt>name</tt> or null if no such user exists.
     *
     * @param name name of user to remove
     * @return removed user or null if no such user exists
     */
    @Nullable
    @Override
    public User removeUser(String name) {
        if (isConnectionClosed())
            return null;
        if (name == null || !getUserNames().contains(name))
            return null;

        User toReturn = getUser(name);
        if (toReturn == null)
            return null;

        try {
            // removing all accounts (and recursively records)
            Set<Account> accounts = getAccounts(toReturn);
            accounts.forEach(account -> removeAccount(toReturn, account));

            // finding USER_ID and delete the account
            int userID = findUserId(toReturn);
            stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM users WHERE USER_ID = '" + userID + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    /**
     * Removes <tt>account</tt> from <tt>user</tt> if it
     * really belongs to <tt>user</tt>. Returns removed
     * <tt>account</tt> or null if no such account or <tt>user</tt> exists.
     *
     * @param owner   owner of the <tt>account</tt> to delete
     * @param account account to delete
     * @return removed account or null if no such <tt>account</tt> or <tt>user</tt> exists
     */
    @Nullable
    @Override
    public Account removeAccount(User owner, Account account) {
        if (isConnectionClosed())
            return null;
        if (owner == null || account == null)
            return null;
        if (findUserId(owner) == -1 || findAccountId(account) == null)
            return null;

        Optional<Account> optAcc = getAccounts(owner)
                .parallelStream()
                .filter(account1 -> account1.equals(account))
                .findFirst();

        // if no account found return null
        if (!optAcc.isPresent())
            return null;

        Account toReturn = optAcc.get();
        try {
            // removing all related records
            Set<Record> records = getRecords(toReturn);
            records.forEach(record -> removeRecord(toReturn, record));

            // finding ACCOUNT_ID and delete the account
            String accountId = findAccountId(toReturn);
            stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM accounts WHERE ACCOUNT_ID = '" + accountId + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    /**
     * Removes <tt>record</tt> from <tt>account</tt> if it
     * really belongs to <tt>account</tt>. Returns removed
     * <tt>record</tt> or null if no such record or <tt>account</tt> exists.
     *
     * @param from   account to delete the <tt>record</tt> from
     * @param record record to delete
     * @return removed record or null if no such <tt>record</tt> or <tt>account</tt> exists
     */
    @Nullable
    @Override
    public Record removeRecord(Account from, Record record) {
        if (isConnectionClosed())
            return null;
        if (from == null || record == null)
            return null;
        if (findUserId(from.getOwner()) == -1 || findAccountId(from) == null)
            return null;

        Optional<Record> optRec = getRecords(from)
                .parallelStream()
                .filter(record1 -> record1.equals(record))
                .findFirst();

        // if no record found return null
        if (!optRec.isPresent())
            return null;

        Record toReturn = optRec.get();
        try {
            String recordId = findRecordId(from, toReturn);
            if (recordId == null) // making sure, that record exists and belongs to account
                return null;

            float amount = toReturn.getAmount();
            if (toReturn.getType() == RecordType.DEPOSIT)
                amount = -amount;

            // adding transtaction amount of removing record to account it belongs
            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE accounts SET BALANCE = BALANCE + " + amount + " WHERE ACCOUNT_ID = '" + findAccountId(from) + "';");

            // deleting record from records
            stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM records WHERE RECORD_ID = '" + recordId + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toReturn;
    }
}

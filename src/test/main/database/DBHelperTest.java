package main.database;

import main.java.Account;
import main.java.Record;
import main.java.User;
import main.util.Category;
import main.util.RecordType;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class DBHelperTest {

    private DBHelper db;
    private User user, malUserPass, malUserName;
    private Account account, malAccount;
    private Record record;

    @Before
    public void setUp() throws Exception {
        db = DBHelper.getInstance();
        user = new User("admin", "admin");
        malUserPass = new User("admin", "dasda");
        malUserName = new User("adla", "dasda");
        account = new Account(user, 5000, "RUB");
        malAccount = new Account(malUserPass, 5000, "RUB");

        record = new Record(1460817310779l, 500, RecordType.WITHDRAW, Category.Books, "The Sound and the Fury");
    }

    @Test
    public void addUser() throws Exception {
        db.connect();
        db.addUser(user);
        db.close();
    }

    @Test
    public void addAccount() throws Exception {
        addUser();

        db.connect();
        db.addAccount(user, account);
        db.addAccount(malUserName, account);
        db.addAccount(malUserName, malAccount);
        db.addAccount(malUserPass, account);
        db.close();
    }

    @Test
    public void addRecord() throws Exception {
        addAccount();

        db.connect();
        db.addRecord(account, record);
        db.addRecord(account, record);
        db.close();
    }

    @Test
    public void getAccounts() throws Exception {
        Set<Account> accounts;
        db.connect();
        accounts = db.getAccounts(user);
        db.close();
    }

    @Test
    public void getRecords() throws Exception {
        Set<Record> records;
        db.connect();
        records = db.getRecords(account);
        db.close();
    }

    @Test
    public void getUser() throws Exception {
        User user;
        db.connect();
        user = db.getUser("aloha");
        assertNull(user);

        user = db.getUser("admin");
        assertNotNull(user);
        assertEquals(this.user, user);
        db.close();
    }
}
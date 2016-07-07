package com.finance.java;

import com.finance.model.Account;
import com.finance.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private User user;
    private Account account1, account2;
    private float delta = 0.1f;

    @Before
    public void setUp() throws Exception {
        user = new User("kos", "kostya");

        account1 = new Account(user, "account with initial balance");
        account2 = new Account(user, "account with zero balance");
    }

    @Test
    public void addAccount() throws Exception {
        assertEquals(user.getNumOfAccounts(), 0);
        assertEquals(user.getUserBalance(), 0, delta);

        // adding account
        user.addAccount(account1);
        // checking number of accounts
        assertEquals(user.getNumOfAccounts(), 1);
        // checking user's balance
        assertEquals(user.getUserBalance(), account1.getBalance(), delta);

        // adding account that already in the Set
        user.addAccount(account1);
        // checking number of accounts (shouldn't change)
        assertEquals(user.getNumOfAccounts(), 1);
        // checking user's balance (shouldn't change)
        assertEquals(user.getUserBalance(), account1.getBalance(), delta);

        // adding account
        user.addAccount(account2);
        // checking number of accounts
        assertEquals(user.getNumOfAccounts(), 2);
        // checking user's balance
        assertEquals(user.getUserBalance(), account1.getBalance() + account2.getBalance(), delta);
    }

    @Test
    public void removeAccount() throws Exception {

        // removing not existing account (expect null)
        assertNull(user.removeAccount(account1));

        // adding account
        user.addAccount(account1);
        // removing not existing account (expect null)
        assertNull(user.removeAccount(account2));
        //checking number of accounts
        assertEquals(user.getNumOfAccounts(), 1);
        // removing existing account
        assertNotNull(user.removeAccount(account1));
        //checking number of accounts
        assertEquals(user.getNumOfAccounts(), 0);
        // checking that user has zero balance
        assertEquals(user.getUserBalance(), 0, delta);

        // adding account
        user.addAccount(account1);
        // adding account
        user.addAccount(account2);
        //checking number of accounts
        assertEquals(user.getNumOfAccounts(), 2);
        // removing existing account
        assertNotNull(user.removeAccount(account1));
        //checking number of accounts
        assertEquals(user.getNumOfAccounts(), 1);
        // checking that user's balance
        assertEquals(user.getUserBalance(), account2.getBalance(), delta);
        // removing existing account
        assertNotNull(user.removeAccount(account2));
        //checking number of accounts
        assertEquals(user.getNumOfAccounts(), 0);
        // checking that user has zero balance
        assertEquals(user.getUserBalance(), 0, delta);
    }

    @Test
    public void equals() throws Exception {
        User userEq1 = new User("kostya", "kostya");
        User userEq2 = new User("kostya", "kostya");
        User userDif = new User("KOSTYA", "kostya");

        assertTrue(userEq1.equals(userEq2));
        assertTrue(userEq2.equals(userEq1));

        assertFalse(userEq1.equals(userDif));
        assertFalse(userDif.equals(userEq1));

        assertFalse(userEq2.equals(userDif));
        assertFalse(userDif.equals(userEq2));
    }

    @Test
    public void getAccount() throws Exception {
        user.addAccount(account1);
        user.addAccount(account2);

        assertNotNull(user.getAccount("account with initial balance"));
        assertNotNull(user.getAccount("account with zero balance"));
        assertNull(user.getAccount("test"));
    }
}
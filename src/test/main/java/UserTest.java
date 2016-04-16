package main.java;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private User user;
    private Account account1, account2;
    private float initialBalance;
    private float delta = 0.1f;

    @Before
    public void setUp() throws Exception {
        user = new User("kos", "kostya");

        initialBalance = 1000;

        account1 = new Account(user, initialBalance, "account with initial balance");
        account2 = new Account(user, "account with zero balance");
    }

    @Test
    public void changePassword() throws Exception {
        final String fail = "FAIL\n";
        final String ok = "OK\n";
        String res = "";

        System.out.println("Trying change password with:");
        // trying change password with wrong old password
        try {
            user.changePassword("kosya", "fa-da*f2");
            res = fail;
        } catch (IllegalArgumentException e) {
            res = ok;
        }
        System.out.printf("wrong old password: %s", res);

        // trying change password with wrong new password length (<= 5)
        try {
            user.changePassword("kostya", "f");
            res = fail;
        } catch (IllegalArgumentException e) {
            res = ok;
        }
        System.out.printf("wrong new password length (<= 5): %s", res);

        // trying change password with wrong new password length (> 15)
        try {
            user.changePassword("kostya", "fa-da*f2dasdhg2j3gjhgsfjhsa");
            res = fail;
        } catch (IllegalArgumentException e) {
            res = ok;
        }
        System.out.printf("wrong new password length (> 15): %s", res);

        // trying change password with valid old and new passwords
        try {
            user.changePassword("kostya", "e12D&Ay");
            res = ok;
        } catch (IllegalArgumentException e) {
            res = fail;
        }
        System.out.printf("valid old and valid new passwords: %s", res);
    }

    @Test
    public void checkPassword() throws Exception {
        assertTrue(user.checkMD5Password("kostya"));

        assertFalse(user.checkMD5Password("kostyA")); // wrong
        assertFalse(user.checkMD5Password("kos")); // less than 5
        assertFalse(user.checkMD5Password("kostyakostyakostyakostya")); // longer than 15
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

}
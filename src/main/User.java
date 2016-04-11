package main;

import main.util.MD5;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User implements Serializable {

    private final String login;
    private String password;
    private final Set<Account> accounts;
    private int hash;

    /**
     * Initialize user with login and password
     *
     * @param login    any set of characters
     * @param password password, must be >= 5 and < 15 character long
     */
    public User(String login, String password) {
        this.login = login;
        setPassword(password);
        accounts = new HashSet<>();
    }

    /**
     * Method to change password
     *
     * @param oldPassword old password
     * @param newPassword new password, must be >= 5 and < 15 character long
     * @throws IllegalArgumentException if passwords don't match or new password is < 5 or >= 15 character long
     */
    public void changePassword(String oldPassword, String newPassword) {
        if (!checkPassword(oldPassword))
            throw new IllegalArgumentException("passwords don't match or new password is < 5 or >= 15 character long");

        try {
            setPassword(newPassword);
        } catch (IllegalArgumentException e) { // not letting client know, that oldPassword is right
            throw new IllegalArgumentException("passwords don't match or new password is < 5 or >= 15 character long");
        }
    }

    /**
     * Private method to set password, accepts passwords >= 5 and < 15 character long
     *
     * @param password password to set
     * @throws IllegalArgumentException if password is < 5 or >= 15 character long
     */
    private void setPassword(String password) {
        if (password.length() >= 5 && password.length() < 15)
            this.password = MD5.getHash(password);
        else
            throw new IllegalArgumentException("password must be at least 5 character long (and less than 15 characters)");
    }

    /**
     * Private method to check whether given password is THE password
     *
     * @param password password to check
     */
    private boolean checkPassword(String password) {
        if (password.length() != this.password.length())
            return false;

        return MD5.isEquals(this.password, password);
    }

    /**
     * Adds <tt>account</tt> to Set of accounts,
     *
     * @param account account to add
     */
    public void addAccount(Account account) {
        synchronized (accounts) {
            accounts.add(account);
        }
    }

    /**
     * Deletes <tt>account</tt> from Set of accounts
     *
     * @param account to delete
     * @return deleted <tt>account</tt> (or null if not found)
     */
    public Account removeAccount(Account account) {
        Account toRet = null;

        synchronized (accounts) {
            if (accounts.contains(account)) {
                for (Account acc : accounts) {
                    if (acc.equals(account)) {
                        toRet = acc;
                        accounts.remove(acc);
                        break;
                    }
                }
            }
        }

        return toRet;
    }

    /**
     * Method to get number of accounts
     *
     * @return number of accounts user has
     */
    public int getNumOfAccounts() {
        return accounts.size();
    }

    /**
     * Method to get the balance of all user's accounts
     *
     * @return user balance (cumulative balance of all accounts)
     */
    public float getUserBalance() {
        float balance = 0;
        synchronized (accounts) {
            for (Account account : accounts)
                balance += account.getBalance();
        }
        return balance;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof User)) return false;

        User user = (User) obj;
        return login.equals(user.login);
    }

    /**
     * Making hash with <tt>login</tt> String
     */
    @Override
    public int hashCode() {
        if (hash == 0) {
            UUID key = UUID.nameUUIDFromBytes(login.getBytes());
            hash = key.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return login;
    }
}

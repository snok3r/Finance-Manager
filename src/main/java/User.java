package main.java;

import main.util.MD5;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
     * @return User login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return User password hash
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method to change password
     *
     * @param oldPassword old password
     * @param newPassword new password, must be >= 5 and < 15 character long
     * @throws IllegalArgumentException if passwords don't match or new password is < 5 or >= 15 character long
     */
    public void changePassword(String oldPassword, String newPassword) {
        if (!checkMD5Password(oldPassword))
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
     * Method to check whether given hash is THE password
     *
     * @param password password to check
     */
    public boolean checkMD5Password(String password) {
        return MD5.isEquals(this.password, password);
    }

    /**
     * Method to check whether given password is THE password
     *
     * @param password password to check
     */
    public boolean checkStringPassword(String password) {
        return this.password.equals(password);
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
     * @return first account
     */
    public Account getDefaultAccount() {
        Account toRet = null;
        for (Account acc : accounts) {
            toRet = acc;
            break;
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
        if (hash == 0)
            hash = MD5.getHash(login).hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return login;
    }
}

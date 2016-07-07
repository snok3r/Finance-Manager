package com.finance.model;

import com.finance.util.MD5;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {

    private final String login;
    private final String password;
    private final Set<Account> accounts;
    private int hash;

    /**
     * Initialize user with login and password
     *
     * @param login    any set of characters
     * @param password password, must be greater or equal than 5 and less than 15 character long
     */
    public User(String login, String password) {
        this.login = login;
        this.password = setPassword(password);
        this.accounts = new HashSet<>();
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
     * Private method to generate hashed password, accepts passwords greater or equal than 5 and less than 15 character long
     *
     * @param password password to set
     * @return hashed <tt>password</tt>
     * @throws IllegalArgumentException if password is less than 5 or greater or equal than 15 character long
     */
    private String setPassword(String password) {
        if (password.length() >= 5 && password.length() < 15)
            return MD5.getHash(password);
        else
            throw new IllegalArgumentException("password must be at least 5 character long (and less than 15 characters)");
    }

    /**
     * Method to check whether given password is THE password
     *
     * @param password password to check
     * @return true if <tt>password</tt> matches THE password, false otherwise
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
        accounts.add(account);
    }

    /**
     * Deletes <tt>account</tt> from Set of accounts
     *
     * @param account to delete
     * @return deleted <tt>account</tt> (or null if not found)
     */
    public Account removeAccount(Account account) {
        Account toRet = null;

        if (accounts.contains(account)) {
            for (Account acc : accounts) {
                if (acc.equals(account)) {
                    toRet = acc;
                    accounts.remove(acc);
                    break;
                }
            }
        }

        return toRet;
    }

    /**
     * Method to get account by its description
     *
     * @param desc description of the account you need
     * @return account with this <tt>desc</tt> or null,
     * if not found any.
     */
    public Account getAccount(String desc) {
        return accounts
                .parallelStream()
                .filter(a -> a.getDescription().equals(desc))
                .findFirst()
                .orElse(null);
    }

    /**
     * Method to get user accounts
     *
     * @return accounts of this user
     */
    public Set<Account> getAccounts() {
        return accounts;
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
        return ((float) accounts
                .parallelStream()
                .mapToDouble(a -> a.getBalance())
                .sum());
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

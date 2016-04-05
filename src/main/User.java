package main;

import main.util.Salt;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {

    private final String login;
    private String password;
    private Set<Account> accounts;
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
            this.password = Salt.salt(password);
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

        return Salt.isEquals(this.password, password);
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof User)) return false;

        User user = (User) obj;
        return hashCode() == user.hashCode();
    }

    /**
     * Making hash with login String (main.User login must be uniq)
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

    public static void main(String[] args) {
        User user = new User("kos", "kostya");

        System.out.println(user.checkPassword("kostya"));
        System.out.println(user.checkPassword(Salt.salt(user.password)));
        System.out.println(user.checkPassword("koasas"));
        System.out.println(user.checkPassword("kos"));

        user.changePassword("kostya", "kostya123");
        System.out.println(Salt.salt(user.password));
    }
}

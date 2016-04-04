import util.Salt;

import java.util.HashSet;
import java.util.Set;

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

        return Salt.isSaltedStringEqualsToSecondString(this.password, password);
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
     * Making hash with login String (User login must be uniq)
     */
    @Override
    public int hashCode() {
        if (hash == 0)
            hash = login.hashCode();
        return hash;
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

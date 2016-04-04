
public class User {

    private final String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        setPassword(password);
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (!checkPassword(oldPassword))
            throw new IllegalArgumentException("passwords don't match or new password is < 5 or >= 15 characters long");

        try {
            setPassword(newPassword);
        } catch (IllegalArgumentException e) { // not letting client know, that oldPassword is right
            throw new IllegalArgumentException("passwords don't match or new password is < 5 or >= 15 characters long");
        }
    }

    private void setPassword(String password) {
        if (password.length() >= 5 && password.length() < 15)
            this.password = saltingPassword(password);
        else
            throw new IllegalArgumentException("password should be at least 5 characters long (and less than 15 characters)");
    }

    private boolean checkPassword(String password) {
        if (password.length() != this.password.length())
            return false;

        return this.password.equals(saltingPassword(password));
    }

    private String saltingPassword(String password) {
        int len = password.getBytes().length;

        byte[] salt = new byte[len];
        for (int i = 0; i < len; i++) {
            if (i % 2 == 0)
                salt[i] = 4;
            else
                salt[i] = 2;
        }

        byte[] pass = password.getBytes();
        for (int i = 0; i < len; i++)
            pass[i] ^= salt[i];

        return new String(pass);
    }

    public static void main(String[] args) {
        User user = new User("kos", "kostya");

        System.out.println(user.checkPassword("kostya"));
        System.out.println(user.checkPassword(user.saltingPassword(user.password)));
        System.out.println(user.checkPassword("koasas"));
        System.out.println(user.checkPassword("kos"));

        user.changePassword("kostya", "kostya123");
        System.out.println(user.saltingPassword(user.password));
    }
}

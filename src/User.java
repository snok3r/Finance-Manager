
public class User {

    private final String login;
    private final String password;

    public User(String login, String password) {
        this.login = login;
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
    }
}

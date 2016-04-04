package util;

public class Salt {
    /**
     * Adds salt to given str String
     *
     * @param str string to salt
     * @return String of salted str
     */
    static public String salt(String str) {
        int len = str.getBytes().length;

        byte[] salt = new byte[len];
        for (int i = 0; i < len; i++) {
            if (i % 2 == 0)
                salt[i] = 4;
            else
                salt[i] = 2;
        }

        byte[] pass = str.getBytes();
        for (int i = 0; i < len; i++)
            pass[i] ^= salt[i];

        return new String(pass);
    }

    /**
     * Checks if salted str2 equals to str1
     *
     * @param str1 already salted string
     * @param str2 string to check
     */
    static public boolean isSaltedStringEqualsToSecondString(String str1, String str2) {
        return str1.equals(salt(str2));
    }
}

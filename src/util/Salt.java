package util;

public class Salt {
    private static final byte[] salt = new byte[]{
            4, 2, 4, 2,
            4, 2, 4, 2,
            4, 2, 4, 2,
            4, 2, 4};

    /**
     * Adds salt to given str String
     *
     * @param str string to salt
     * @return salted str
     */
    static public String salt(String str) {
        int len = str.getBytes().length;

        byte[] pass = str.getBytes();
        for (int i = 0; i < len; i++)
            pass[i] ^= salt[i];

        return new String(pass);
    }

    /**
     * Checks if salted str2 equals to str1
     *
     * @param saltedStr already salted string
     * @param str2 string to: first salt and then check
     */
    static public boolean isEquals(String saltedStr, String str2) {
        return saltedStr.equals(salt(str2));
    }
}

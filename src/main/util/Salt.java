package main.util;

public class Salt {
    private static final byte[] salt = new byte[]{
            4, 2, 4, 2,
            4, 2, 4, 2,
            4, 2, 4, 2,
            4, 2, 4};

    /**
     * Adds salt to <tt>str</tt>
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
     * Checks if <tt>saltedStr</tt> equals to salted <tt>str</tt>
     *
     * @param saltedStr already salted string
     * @param str       string to: first salt and then check
     */
    static public boolean isEquals(String saltedStr, String str) {
        return saltedStr.equals(salt(str));
    }
}

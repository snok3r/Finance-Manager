package com.finance.util;

import java.security.MessageDigest;

public class MD5 {

    /**
     * Making MD5 hash out of <tt>str</tt>
     *
     * @param str String to hash
     * @return MD5 hashed String
     */
    static public String getHash(String str) {
        MessageDigest md5;
        StringBuffer hexString = new StringBuffer();

        try {
            md5 = MessageDigest.getInstance("md5");

            md5.reset();
            md5.update(str.getBytes());

            byte messageDigest[] = md5.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }

        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hexString.toString();
    }

    /**
     * Checks if <tt>hashedStr</tt> equals to hashed <tt>str</tt>
     *
     * @param hashedStr already hashed string
     * @param str       string to: first getHash and then check
     */
    static public boolean isEquals(String hashedStr, String str) {
        return hashedStr.equals(getHash(str));
    }
}

package com.mycompany.loginregistrationunifiedza.passwords;

import java.security.MessageDigest;
import java.util.Base64;

/**
 *
 * @author chuma
 */
public class passwords {

    public static String hash(String plainText) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(plainText.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }
}


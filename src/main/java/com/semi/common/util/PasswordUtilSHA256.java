// src/com/semi/common/util/PasswordUtilSHA256.java

// import com.semi.common.util/PasswordUtilSHA256;

package com.semi.common.util;

import java.security.MessageDigest;

public class PasswordUtilSHA256 {

    // 평문 → SHA-256 해시(HEX)
    public static String hash(String plain) {
        if (plain == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(plain.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 hashing failed", e);
        }
    }

    // 입력 평문 vs 저장 해시 비교
    public static boolean matches(String plain, String hashed) {
        if (plain == null || hashed == null) return false;
        return hash(plain).equalsIgnoreCase(hashed);
    }
}

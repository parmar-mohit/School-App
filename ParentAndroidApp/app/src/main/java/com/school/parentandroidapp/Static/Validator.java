package com.school.parentandroidapp.Static;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class Validator {

    //Validation Regex
    public static final String MOBILE_PATTERN = "[0-9]{10}";
    public static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static boolean isValidPassword(String password) {
        /* This Function returns a boolean value to indicate whether a password is Valid or not
        for a password to be valid it must contain one uppercase letter, one lowercase letter and one number,
         */
        boolean hasUpperCase = false, hasLowerCase = false, hasNumber = false;
        int i = 0;

        while (!(hasUpperCase && hasLowerCase && hasNumber) && i < password.length()) {
            if (Character.isUpperCase(password.charAt(i))) {
                hasUpperCase = true;
            }

            if (Character.isLowerCase(password.charAt(i))) {
                hasLowerCase = true;
            }

            if (Character.isDigit(password.charAt(i))) {
                hasNumber = true;
            }

            i++;
        }

        return (hasUpperCase && hasLowerCase && hasNumber);
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
            byte[] hashedByte = digest.digest(passwordBytes);
            return Base64.getEncoder().encodeToString(hashedByte);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isValidPhone(String phone){
        return phone.matches(MOBILE_PATTERN);
    }
}

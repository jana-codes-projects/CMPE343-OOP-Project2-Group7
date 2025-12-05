package com.emirfurqan.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher
{

    // Hash a plain-text password using SHA-256
    public static String hash(String password)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes)
            {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Verify a plain password against a hashed password
    public static boolean verify(String plain, String hashed)
    {
        return hash(plain).equals(hashed);
    }
}

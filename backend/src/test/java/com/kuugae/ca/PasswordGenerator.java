package com.kuugae.ca;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordGenerator {
    public static void main(String[] args) {
        String password = "admin123";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Password: " + password);
        System.out.println("Hashed: " + hashed);

        // Verify
        boolean matches = BCrypt.checkpw(password, hashed);
        System.out.println("Verify: " + matches);
    }
}

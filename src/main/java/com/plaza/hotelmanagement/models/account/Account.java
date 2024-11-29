package com.plaza.hotelmanagement.models.account;

import java.security.MessageDigest;

public class Account {
    private int accountId;
    private String fullName;
    private String email;
    private String passwordHash;
    private String role;

    // Constructor
    public Account(String fullName, String email, String password, String role) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = hashPassword(password);
        this.role = (role != null && !role.isEmpty()) ? role : "user";
    }

    // Getters and setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPasswordHash() {
        return passwordHash;
    }

    // Hash passwords using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

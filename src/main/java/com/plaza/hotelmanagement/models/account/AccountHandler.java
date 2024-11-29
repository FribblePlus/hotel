package com.plaza.hotelmanagement.models.account;

import com.plaza.hotelmanagement.database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountHandler {
    private final DatabaseManager dbManager;

    public AccountHandler(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // Save user to database
    public boolean saveAccount(Account account) {
        String sql = "INSERT INTO account (Role, FullName, Email, Password) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, account.getRole());
            pstmt.setString(2, account.getFullName());
            pstmt.setString(3, account.getEmail());
            pstmt.setString(4, account.hashPassword(account.getEmail()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        account.setAccountId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Edit account
    public Account editAccount(Account account, String fullName, String email, String password){
        if(Objects.equals(password, "")){
            String sql = "UPDATE account SET FullName = ?, Email = ? WHERE AccountID = ?";
            try (Connection conn = dbManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, fullName);
                pstmt.setString(2, email);
                pstmt.setString(3, String.valueOf(account.getAccountId()));
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            account.setAccountId(generatedKeys.getInt(1));
                        }
                    }
                    return authenticate(email, account.getPasswordHash());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }else{
            String sql = "UPDATE account SET FullName = ?, Email = ?, Password = ? WHERE AccountID = ?";
            try (Connection conn = dbManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, fullName);
                pstmt.setString(2, email);
                pstmt.setString(3, Account.hashPassword(password));
                pstmt.setString(4, String.valueOf(account.getAccountId()));

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            account.setAccountId(generatedKeys.getInt(1));
                        }
                    }
                    return authenticate(email, password);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    // Authenticate user
    public Account authenticate(String email, String password) {
        String sql = "SELECT * FROM account WHERE Email = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("Password");
                    if (storedHash.equals(Account.hashPassword(password))) {
                        Account user = new Account(
                                rs.getString("FullName"),
                                rs.getString("Email"),
                                "", // We don't store plain password
                                rs.getString("Role")
                        );
                        user.setAccountId(rs.getInt("AccountID"));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all users
    public List<Account> getAllUsers() {
        List<Account> users = new ArrayList<>();
        String sql = "SELECT * FROM account";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Account user = new Account(
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        "", // Don't expose plain passwords
                        rs.getString("Role")
                );
                user.setAccountId(rs.getInt("AccountID"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}

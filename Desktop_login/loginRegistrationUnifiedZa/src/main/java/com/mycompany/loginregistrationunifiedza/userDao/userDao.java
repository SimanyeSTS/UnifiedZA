package com.mycompany.loginregistrationunifiedza.userDao;

import com.mycompany.loginregistrationunifiedza.DBConnection.DBConnection;
import com.mycompany.loginregistrationunifiedza.passwords.passwords;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author chuma
 */


public class userDao {

    
    public boolean registerUser(String fullName, String idNumber, String email,
                                 String phoneNumber, String plainPassword,
                                 int provinceId, String userType) {

        String sql = "INSERT INTO Users (FullName, IdNumber, Email, PhoneNumber, Passwords, ProvinceID, UserType) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String hashedPassword = passwords.hash(plainPassword);

            stmt.setString(1, fullName);
            stmt.setString(2, idNumber);
            stmt.setString(3, email);
            stmt.setString(4, phoneNumber);
            stmt.setString(5, hashedPassword);
            stmt.setInt(6, provinceId);
            stmt.setString(7, userType);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    
    public int validateLogin(String email, String plainPassword) {
        String sql = "SELECT UserID, Passwords FROM Users WHERE Email = ?";
        int foundUserId = -1;
        boolean passwordMatches = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                foundUserId = rs.getInt("UserID");
                String storedHash = rs.getString("Passwords");
                String enteredHash = passwords.hash(plainPassword);
                passwordMatches = storedHash.equals(enteredHash);
            }

           
            if (foundUserId != -1) {
                logLoginAttempt(conn, foundUserId, passwordMatches ? "Approved" : "Failed");
            }

        } catch (SQLException e) {
            System.out.println("Login check failed: " + e.getMessage());
        }

        return passwordMatches ? foundUserId : -1;
    }

    private void logLoginAttempt(Connection conn, int userId, String status) throws SQLException {
        String sql = "INSERT INTO LoginSession (UserID, LoginTime, LoginDevice, LoginStatus) "
                   + "VALUES (?, NOW(), ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, "Desktop App (Java)");
            stmt.setString(3, status);
            stmt.executeUpdate();
        }
    }
}
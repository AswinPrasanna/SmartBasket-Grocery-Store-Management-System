package project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

public class CustomerDAO {

    public boolean register(String name, String email, String phone, String passwordPlain, String address, String location) {
        String sql = "INSERT INTO customers (name, email, phone, password, address, location) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, phone);
            pst.setString(4, (passwordPlain));//sha256(passwordPlain));
            pst.setString(5, address);
            pst.setString(6, location);
            return pst.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException dup) {
            System.out.println("Email already registered.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Customer> login(String email, String passwordPlain) {
        String sql = "SELECT customer_id, name, email, phone FROM customers WHERE email=? AND password=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, email);
            pst.setString(2,  (passwordPlain));//sha256(passwordPlain));
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
//
//    private static String sha256(String str) throws Exception {
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] hash = md.digest(str.getBytes("UTF-8"));
//        StringBuilder sb = new StringBuilder();
//        for (byte b : hash) sb.append(String.format("%02x", b));
//        return sb.toString();
//    }
}


/*
 * © 2025 Aswin Prasanna | SmartBasket - Grocery Store Management System
 * All Rights Reserved.
 */


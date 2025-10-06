package project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO {

    public boolean addProduct(String name, String category, double price, int stock) {
        String sql = "INSERT INTO products (name, category, price, stock) VALUES (?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, category);
            pst.setDouble(3, price);
            pst.setInt(4, stock);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(int productId, String name, String category, double price, int stock) {
        String sql = "UPDATE products SET name=?, category=?, price=?, stock=? WHERE product_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, category);
            pst.setDouble(3, price);
            pst.setInt(4, stock);
            pst.setInt(5, productId);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, productId);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Product> listAll() {
        String sql = "SELECT * FROM products ORDER BY name";
        List<Product> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Product> search(String keyword) {
        String sql = "SELECT * FROM products WHERE name LIKE ? OR category LIKE ? ORDER BY name";
        List<Product> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            pst.setString(1, like);
            pst.setString(2, like);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("stock")));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public Optional<Product> getById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, productId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("stock")));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return Optional.empty();
    }
}
/*
 * © 2025 Aswin Prasanna | SmartBasket - Grocery Store Management System
 * All Rights Reserved.
 */

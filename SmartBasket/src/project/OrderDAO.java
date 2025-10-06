package project;

import java.sql.*;
import java.util.List;

public class OrderDAO {

    public boolean placeOrder(int customerId, List<CartItem> cart, String paymentMode) {
        if (cart == null || cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return false;
        }

        String insertOrder = "INSERT INTO orders (customer_id, total_amount, status, payment_mode) VALUES (?,?, 'PLACED', ?)";
        String insertItem  = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?,?,?,?)";
        String insertPay   = "INSERT INTO payments (order_id, payment_method, payment_status) VALUES (?,?,?)";

        double total = 0.0;
        for (CartItem ci : cart) total += ci.getLineTotal();

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            // Insert into orders
            int orderId;
            try (PreparedStatement pst = con.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                pst.setInt(1, customerId);
                pst.setDouble(2, total);
                pst.setString(3, paymentMode);
                pst.executeUpdate();
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) orderId = rs.getInt(1);
                    else throw new SQLException("Failed to obtain order_id");
                }
            }

            // For each item: reduce stock and insert item
            for (CartItem ci : cart) {
                // Reduce stock atomically and ensure enough stock
                try (PreparedStatement pst = con.prepareStatement(
                        "UPDATE products SET stock = stock - ? WHERE product_id=? AND stock >= ?")) {
                    pst.setInt(1, ci.getQuantity());
                    pst.setInt(2, ci.getProductId());
                    pst.setInt(3, ci.getQuantity());
                    int updated = pst.executeUpdate();
                    if (updated == 0) {
                        con.rollback();
                        System.out.println("Insufficient stock for product ID " + ci.getProductId());
                        return false;
                    }
                }
                try (PreparedStatement pst = con.prepareStatement(insertItem)) {
                    pst.setInt(1, orderId);
                    pst.setInt(2, ci.getProductId());
                    pst.setInt(3, ci.getQuantity());
                    pst.setDouble(4, ci.getPrice());
                    pst.executeUpdate();
                }
            }

            // Payment row
            String payStatus = paymentMode.equalsIgnoreCase("ONLINE") ? "PAID" : "PENDING";
            try (PreparedStatement pst = con.prepareStatement(insertPay)) {
                pst.setInt(1, orderId);
                pst.setString(2, paymentMode.toUpperCase());
                pst.setString(3, payStatus);
                pst.executeUpdate();
            }

            con.commit();
            System.out.println("Order placed successfully. Order ID: " + orderId + ", Total: " + total);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET status=? WHERE order_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, newStatus);
            pst.setInt(2, orderId);
            int n = pst.executeUpdate();
            System.out.println(n > 0 ? "Order status updated." : "Order not found.");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void viewOrderHistory(int customerId) {
        String sql = "SELECT o.order_id, o.order_date, o.total_amount, o.status, o.payment_mode, p.payment_status " +
                     "FROM orders o JOIN payments p ON o.order_id = p.order_id WHERE o.customer_id=? ORDER BY o.order_date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, customerId);
            try (ResultSet rs = pst.executeQuery()) {
                System.out.println("Order History:");
                System.out.println("ID\tDateTime\t\tTotal\tStatus\tMode\tPayment");
                while (rs.next()) {
                    System.out.printf("%d\t%s\t%.2f\t%s\t%s\t%s%n",
                        rs.getInt("order_id"),
                        rs.getTimestamp("order_date").toString(),
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        rs.getString("payment_mode"),
                        rs.getString("payment_status"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void salesReportDaily() {
        String sql = "SELECT DATE(order_date) AS day, SUM(total_amount) AS revenue, COUNT(*) AS orders " +
                     "FROM orders WHERE status <> 'CANCELLED' GROUP BY DATE(order_date) ORDER BY day DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("Day\t\tRevenue\tOrders");
            while (rs.next()) {
                System.out.printf("%s\t%.2f\t%d%n",
                    rs.getDate("day").toString(),
                    rs.getDouble("revenue"),
                    rs.getInt("orders"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void listAllOrders() {
        String sql = "SELECT o.order_id, c.name AS customer, o.order_date, o.total_amount, o.status, o.payment_mode " +
                     "FROM orders o JOIN customers c ON o.customer_id = c.customer_id ORDER BY o.order_date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("ID\tCustomer\tDateTime\t\tTotal\tStatus\tMode");
            while (rs.next()) {
                System.out.printf("%d\t%s\t%s\t%.2f\t%s\t%s%n",
                    rs.getInt("order_id"),
                    rs.getString("customer"),
                    rs.getTimestamp("order_date").toString(),
                    rs.getDouble("total_amount"),
                    rs.getString("status"),
                    rs.getString("payment_mode"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
/*
 * Project: SmartBasket - Grocery Store Management System
 * Author: Aswin Prasanna
 * © 2025 Aswin Prasanna. All Rights Reserved.
 *
 * Description:
 * This software is developed for educational and personal use.
 * Unauthorized copying, modification, or distribution of this code,
 * via any medium, is strictly prohibited.
 */

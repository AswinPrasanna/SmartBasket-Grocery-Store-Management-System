//DB connection
package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL  = "jdbc:mysql://localhost:3306/SmartBasket";
    private static final String USER = "root";
    private static final String PASS = "aswin@2003";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found. Add mysql-connector-j to classpath.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
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

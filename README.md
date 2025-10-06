# SmartBasket-Grocery-Store-Management-System
  SmartBasket is a Java-based Grocery Store Management System designed to simplify 
the online grocery shopping process for customers while providing administrators 
with full control over product inventory, orders, and payments.

**The project offers:**
1) Customer-Friendly Interface: Easy browsing, searching, and
ordering of grocery products.
 2) Admin Dashboard: Manage inventory, process orders, and
track sales efficiently.
 3) MySQL Database: All data, including users, orders, and
payments, is securely stored and managed in a centralized MySQL
database.
The system is console-based but can be extended to a graphical or web
application in the future.

**Operations:**
1)Customer Operations:
   Register / Login: Create an account or log in with existing
    credentials.
   Profile Management: View and update customer details.
   Browse Products: View a categorized list of products
    (Grains, Vegetables, Fruits, Oils, etc.).
   Search Products: Quickly find items by name or category.
   Add to Cart & Place Orders: Select items, set quantities, and
    confirm purchases.
   Choose Payment Method: Pay online or opt for Cash on
    Delivery (COD).
   View Order History: See previous orders with date and time stamps.
  
2)Admin Operations:
   Admin Login: Access with a predefined username and password.
   Product Management:
    1) Add new products.
    2) Edit product details.
    3) Delete products.
   Order Management:
    1) View pending orders.
    2) Update order delivery status.
   Sales Reports: Generate summaries of revenue and product sales.
   Delivery Tracking: Mark orders as "Shipped," "Delivered," etc.

3)Database Operations:
   The system uses MySQL to store all business data.
   It handles CRUD (Create, Read, Update, Delete) operations for
    customers, products, and orders.
   Relationships between tables ensure proper mapping of customers,
    orders, and payments.

   Database Tables:
    Note: PK → Primary Key (unique identifier for each record).
          FK → Foreign Key (used to link tables together).
          The design follows database normalization principles for improved efficiency and reduced redundancy.

| 🧾 **Table Name** | 📘 **Description**                  | 🔑 **Key Fields**                                                                          |
| ----------------- | ----------------------------------- | ------------------------------------------------------------------------------------------- |
| **CUSTOMERS**     | Stores customer account details.    | `customer_id (PK)`, `name`, `email`, `password`, `phone`                                    |
| **PRODUCTS**      | Stores grocery product information. | `product_id (PK)`, `name`, `category`, `price`, `stock`                                     |
| **ORDERS**        | Records all customer orders.        | `order_id (PK)`, `customer_id (FK)`, `order_date`, `total_amount`, `status`, `payment_mode` |
| **ORDER_ITEMS**   | Links products with orders.         | `order_item_id (PK)`, `order_id (FK)`, `product_id (FK)`, `quantity`, `price`               |
| **PAYMENTS**      | Stores payment details.             | `payment_id (PK)`, `order_id (FK)`, `payment_method`, `payment_status`                      |
| **ADMINS**        | Stores admin credentials.           | `admin_id (PK)`, `username`, `password`                                                     |

**Future Enhancements:**
  Add delivery and inventory tracking tables.
  Implement user activity logs for better analytics.
  Add timestamp fields (created_at, updated_at) to all tables.

 **Tech Stack:**
| Component    | Technology                               |
| ------------ | ---------------------------------------- |
| **Frontend** | Console (Java-based)                     |
| **Backend**  | Core Java, JDBC                          |
| **Database** | MySQL                                    |
| **Tools**    | IntelliJ IDEA / Eclipse, MySQL Workbench |

**Developed By,**
  Aswin Prasanna
  © 2025 SmartBasket – Grocery Store Project
  All Rights Reserved

© 2025 Aswin Prasanna | Grocery Store Project | All Rights Reserved.


stamps.

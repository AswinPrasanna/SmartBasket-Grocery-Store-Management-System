package project;

import java.util.*;
import java.sql.SQLException;

public class SmartBasketApp {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    private static final Scanner sc = new Scanner(System.in);
    private static final ProductDAO productDAO = new ProductDAO();
    private static final CustomerDAO customerDAO = new CustomerDAO();
    private static final OrderDAO orderDAO = new OrderDAO();

    private static Customer currentUser = null;
    private static final List<CartItem> cart = new ArrayList<>();

    public static void main(String[] args) throws SQLException {
        while (true) {
            System.out.println("_____SmartBasket_____");
            System.out.println("1) Admin");
            System.out.println("2) Customer");
            System.out.println("3) Exit");
            System.out.print("Choose: ");
            int ch = nextInt();
            switch (ch) {
                case 1 -> adminLogin();
                case 2 -> customerMenu();
                case 3 -> { System.out.println("Thank you,Bye!"); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ---------- Admin ----------
    private static void adminLogin() {
    	System.out.println("\n===== Admin Login =====");
        System.out.print("Name: ");
        String u = sc.next();
        System.out.print("Password: ");
        String p = sc.next();
        if (ADMIN_USER.equals(u) && ADMIN_PASS.equals(p)) {
            adminPanel();
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private static void adminPanel() {
        while (true) {
            System.out.println("\n-- Admin Panel --");
            System.out.println("1) Add Product");
            System.out.println("2) Edit Product");
            System.out.println("3) Delete Product");
            System.out.println("4) List Products");
            System.out.println("5) Update Order Status");
            System.out.println("6) View All Orders");
            System.out.println("7) Sales Report (Daily)");
            System.out.println("8) Back");
            System.out.print("Choose: ");
            int c = nextInt();
            switch (c) {
                case 1 -> addProduct();
                case 2 -> editProduct();
                case 3 -> deleteProduct();
                case 4 -> listProducts();
                case 5 -> updateOrderStatus();
                case 6 -> orderDAO.listAllOrders();
                case 7 -> orderDAO.salesReportDaily();
                case 8 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void addProduct() {
        System.out.println("\n===== Add product =====");
        sc.nextLine(); // Clear leftover newline
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Category: ");
        String category = sc.nextLine();
        System.out.print("Price: ");
        double price = nextDouble();
        System.out.print("Stock: ");
        int stock = nextInt();
        boolean ok = productDAO.addProduct(name, category, price, stock);
        System.out.println(ok ? "Product added." : "Failed to add product.");
    }

    private static void editProduct() {
    	System.out.println("\n===== Edit Product =====");
        System.out.print("Product ID: ");
        int pid = nextInt();
        sc.nextLine();
        System.out.print("New Name: ");
        String name = sc.nextLine();
        System.out.print("New Category: ");
        String category = sc.nextLine();
        System.out.print("New Price: ");
        double price = nextDouble();
        System.out.print("New Stock details: ");
        int stock = nextInt();
        boolean ok = productDAO.updateProduct(pid, name, category, price, stock);
        System.out.println(ok ? "Updated." : "Update failed.");
    }

    private static void deleteProduct() {
    	System.out.println("\n===== Delete Product =====");
        System.out.print("Product ID: ");
        int pid = nextInt();
        boolean ok = productDAO.deleteProduct(pid);
        System.out.println(ok ? "Deleted." : "Delete failed.");
    }

    private static void updateOrderStatus() {
        System.out.print("Order ID: ");
        int oid = nextInt();
        System.out.print("New Status [PLACED|PACKED|SHIPPED|DELIVERED|CANCELLED]: ");
        String st = sc.next();
        orderDAO.updateOrderStatus(oid, st.toUpperCase());
    }

    // ---------- Customer ----------
    private static void customerMenu() {
        while (true) {
            System.out.println("\n-- Customer --");
            System.out.println("1) New Register");
            System.out.println("2) Login");
            System.out.println("3) Back");
            System.out.print("Choose: ");
            int c = nextInt();
            switch (c) {
                case 1 -> register();
                case 2 -> {
                    if (login()) customerPanel();
                }
                case 3 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void register() {
    	System.out.println("\n===== New Register =====");
        System.out.print("Name: ");
        String name = sc.next();
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Phone: ");
        String phone = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();
        System.out.print("Address: ");
        sc.nextLine(); 
        String address = sc.nextLine();
        System.out.print("Location (e.g., Chennai): ");
        String location = sc.next();

        boolean ok = customerDAO.register(name, email, phone, pass, address, location);
        System.out.println(ok ? "Registration successfully!, Please login." : "Registration failed.");
    }

    private static boolean login() {
    	System.out.println("\n===== Login =====");
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();
        var user = customerDAO.login(email, pass);
        if (user.isPresent()) {
            currentUser = user.get();
            cart.clear();
            System.out.println("Welcome, " + currentUser.getName() + "!");
            return true;
        } else {
            System.out.println("Invalid login.");
            return false;
        }
    }

    private static void customerPanel() {
        while (true) {
            System.out.println("\n-- Customer Panel --");
            System.out.println("1) Browse Products");
            System.out.println("2) Search Products");
            System.out.println("3) Add To Cart");
            System.out.println("4) View Cart");
            System.out.println("5) Checkout");
            System.out.println("6) View Order History");
            System.out.println("7) Logout");
            System.out.print("Choose: ");
            int c = nextInt();
            switch (c) {
                case 1 -> listProducts();
                case 2 -> searchProducts();
                case 3 -> addToCart();
                case 4 -> viewCart();
                case 5 -> checkout();
                case 6 -> orderDAO.viewOrderHistory(currentUser.getCustomerId());
                case 7 -> { currentUser = null; cart.clear(); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void listProducts() {
    	System.out.println("\n===== List Products =====");
        var list = productDAO.listAll();
        if (list.isEmpty()) {
            System.out.println("No products.");
            return;
        }
        System.out.println("ID\tName\tCategory\tPrice\tStock");
        for (var p : list) {
            System.out.printf("%d\t%s\t%s\t%.2f\t%d%n",
                p.getProductId(), p.getName(), p.getCategory(), p.getPrice(), p.getStock());
        }
    }

    private static void searchProducts() {
    	System.out.println("\n===== Search Products =====");
    	System.out.println("(Grains, Oils, Pulses, Gourmet, Vegetables, Fruits)");
        System.out.print("Keyword: ");
        String k = sc.next();
        var list = productDAO.search(k);
        if (list.isEmpty()) {
            System.out.println("No matching products.");
            return;
        }
        System.out.println("ID\tName\tCategory\tPrice\tStock");
        for (var p : list) {
            System.out.printf("%d\t%s\t%s\t%.2f\t%d%n",
                p.getProductId(), p.getName(), p.getCategory(), p.getPrice(), p.getStock());
        }
    }

    private static void addToCart() {
    	System.out.println("\n===== Add To Cart =====");
        System.out.print("Product ID: ");
        int pid = nextInt();
        var opt = productDAO.getById(pid);
        if (opt.isEmpty()) {
            System.out.println("Product not found.");
            return;
        }
        var p = opt.get();
        System.out.print("Quantity: ");
        int qty = nextInt();
        if (qty <= 0 || qty > p.getStock()) {
            System.out.println("Invalid quantity. Available stock: " + p.getStock());
            return;
        }
        cart.add(new CartItem(p.getProductId(), p.getName(), p.getPrice(), qty));
        System.out.println("Added to cart.");
    }

    private static void viewCart() {
    	System.out.println("\n===== View Cart =====");
        if (cart.isEmpty()) { System.out.println("Cart is empty."); return; }
        double total = 0.0;
        System.out.println("PID\tName\tQty\tPrice\tLine Total");
        for (CartItem ci : cart) {
            System.out.printf("%d\t%s\t%d\t%.2f\t%.2f%n",
                ci.getProductId(), ci.getName(), ci.getQuantity(), ci.getPrice(), ci.getLineTotal());
            total += ci.getLineTotal();
        }
        System.out.printf("Total: %.2f%n", total);
    }

    private static void checkout() {
    	System.out.println("\n===== Checkout =====");
        if (cart.isEmpty()) { System.out.println("Cart is empty."); return; }
        System.out.print("Payment mode [ONLINE|COD]: ");
        String mode = sc.next();
        if (!mode.equalsIgnoreCase("ONLINE") && !mode.equalsIgnoreCase("COD")) {
            System.out.println("Invalid mode.");
            return;
        }
        boolean ok = orderDAO.placeOrder(currentUser.getCustomerId(), cart, mode.toUpperCase());
        if (ok) cart.clear();
    }

    private static int nextInt() {
        while (!sc.hasNextInt()) { System.out.print("Enter a number: "); sc.next(); }
        return sc.nextInt();
    }

    private static double nextDouble() {
        while (!sc.hasNextDouble()) { System.out.print("Enter a price one or double number: "); 
        sc.next(); 
        }
        return sc.nextDouble();
    }
}
/********************************************************************
 *  SmartBasket - Grocery Store Management System
 *  © 2025 Aswin Prasanna | All Rights Reserved
 *
 *  Description:
 *  A Java-based console application designed to manage grocery
 *  shopping operations such as product inventory, orders,
 *  payments, and customer details efficiently.
 ********************************************************************/


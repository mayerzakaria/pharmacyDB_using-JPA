package pharmacy.system;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

public class PharmacyDAO {

    private EntityManagerFactory emf;
    private EntityManager em;

    public PharmacyDAO() {
        emf = Persistence.createEntityManagerFactory("PharmacyPU");
        em = emf.createEntityManager();
    }

    public void close() {
        em.close();
        emf.close();
    }

    // ==================== INSERT ====================

    public void insertSupplier(Supplier s) {
        em.getTransaction().begin();
        em.persist(s);
        em.getTransaction().commit();
        System.out.println("✅ Supplier inserted: " + s.getName());
    }

    public void insertProduct(Product p) {
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        System.out.println("✅ Product inserted: " + p.getName());
    }

    public void insertEmployee(Employee e) {
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
        System.out.println("✅ Employee inserted: " + e.getName());
    }

    public void insertCustomer(Customer c) {
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
        System.out.println("✅ Customer inserted: " + c.getName());
    }

    public void insertSale(Sale s) {
        em.getTransaction().begin();
        em.persist(s);
        em.getTransaction().commit();
        System.out.println("✅ Sale inserted: " + s.getSaleID());
    }

    // ==================== UPDATE ====================

    // Update product price
    public void updateProductPrice(int productID, double newPrice) {
        em.getTransaction().begin();
        Product p = em.find(Product.class, productID);
        if (p != null) {
            p.setPrice(newPrice);
            em.merge(p);
            System.out.println("✅ Product price updated to: " + newPrice);
        } else {
            System.out.println("❌ Product not found!");
        }
        em.getTransaction().commit();
    }

    // Update product stock
    public void updateProductStock(int productID, int newStock) {
        em.getTransaction().begin();
        Product p = em.find(Product.class, productID);
        if (p != null) {
            p.setStockQuantity(newStock);
            em.merge(p);
            System.out.println("✅ Stock updated to: " + newStock);
        }
        em.getTransaction().commit();
    }

    // Update customer amount due
    public void updateCustomerAmountDue(int customerID, double amount) {
        em.getTransaction().begin();
        Customer c = em.find(Customer.class, customerID);
        if (c != null) {
            c.setAmountDue(amount);
            em.merge(c);
            System.out.println("✅ Customer amount due updated: " + amount);
        }
        em.getTransaction().commit();
    }

    // Update employee password
    public void updateEmployeePassword(String employeeID, String newPassword) {
        em.getTransaction().begin();
        Employee e = em.find(Employee.class, employeeID);
        if (e != null) {
            e.setPassword(newPassword);
            em.merge(e);
            System.out.println("✅ Password updated for: " + e.getName());
        }
        em.getTransaction().commit();
    }

    // ==================== DELETE ====================

    // Delete product by ID
    public void deleteProduct(int productID) {
        em.getTransaction().begin();
        Product p = em.find(Product.class, productID);
        if (p != null) {
            em.remove(p);
            System.out.println("✅ Product deleted: " + p.getName());
        } else {
            System.out.println("❌ Product not found!");
        }
        em.getTransaction().commit();
    }

    // Delete customer by ID
    public void deleteCustomer(int customerID) {
        em.getTransaction().begin();
        Customer c = em.find(Customer.class, customerID);
        if (c != null) {
            em.remove(c);
            System.out.println("✅ Customer deleted: " + c.getName());
        }
        em.getTransaction().commit();
    }

    // Delete employee by ID
    public void deleteEmployee(String employeeID) {
        em.getTransaction().begin();
        Employee e = em.find(Employee.class, employeeID);
        if (e != null) {
            em.remove(e);
            System.out.println("✅ Employee deleted: " + e.getName());
        }
        em.getTransaction().commit();
    }

    // ==================== QUERIES (JPQL) ====================

    // Query 1: Get all products with their supplier name
    // Joins: products + suppliers
    public void query1_ProductsWithSupplier() {
        System.out.println("\n=== Query 1: Products with Supplier ===");
        List<Object[]> results = em.createQuery(
            "SELECT p.name, p.price, p.stockQuantity, s.name " +
            "FROM Product p JOIN p.supplier s", Object[].class)
            .getResultList();
        for (Object[] row : results) {
            System.out.println("Product: " + row[0] +
                " | Price: " + row[1] +
                " | Stock: " + row[2] +
                " | Supplier: " + row[3]);
        }
    }

    // Query 2: Get all sales with employee name and customer name
    // Joins: sales + employees + customers
    public void query2_SalesWithEmployeeAndCustomer() {
        System.out.println("\n=== Query 2: Sales with Employee & Customer ===");
        List<Object[]> results = em.createQuery(
            "SELECT s.saleID, s.saleDate, s.totalAmount, " +
            "e.name, c.name " +
            "FROM Sale s JOIN s.employee e JOIN s.customer c",
            Object[].class)
            .getResultList();
        for (Object[] row : results) {
            System.out.println("SaleID: " + row[0] +
                " | Date: " + row[1] +
                " | Total: " + row[2] +
                " | Employee: " + row[3] +
                " | Customer: " + row[4]);
        }
    }

    // Query 3: Get all products sold in each sale (sale items details)
    // Joins: sale_items + products + sales
    public void query3_SaleItemsWithProducts() {
        System.out.println("\n=== Query 3: Sale Items with Products ===");
        List<Object[]> results = em.createQuery(
            "SELECT si.sale.saleID, p.name, si.quantity, si.lineTotal " +
            "FROM SaleItem si JOIN si.product p",
            Object[].class)
            .getResultList();
        for (Object[] row : results) {
            System.out.println("SaleID: " + row[0] +
                " | Product: " + row[1] +
                " | Qty: " + row[2] +
                " | LineTotal: " + row[3]);
        }
    }

    // Query 4: Get total sales amount per employee
    // Joins: sales + employees
    public void query4_TotalSalesPerEmployee() {
        System.out.println("\n=== Query 4: Total Sales per Employee ===");
        List<Object[]> results = em.createQuery(
            "SELECT e.name, SUM(s.totalAmount) " +
            "FROM Sale s JOIN s.employee e " +
            "GROUP BY e.name",
            Object[].class)
            .getResultList();
        for (Object[] row : results) {
            System.out.println("Employee: " + row[0] +
                " | Total Sales: " + row[1]);
        }
    }

    // Query 5: Get all customers who have amount due > 0
    // Joins: customers + sales
    public void query5_CustomersWithDebt() {
        System.out.println("\n=== Query 5: Customers with Debt ===");
        List<Object[]> results = em.createQuery(
            "SELECT c.name, c.phone, c.amountDue, s.saleDate " +
            "FROM Customer c JOIN c.sales s " +
            "WHERE c.amountDue > 0",
            Object[].class)
            .getResultList();
        for (Object[] row : results) {
            System.out.println("Customer: " + row[0] +
                " | Phone: " + row[1] +
                " | Due: " + row[2] +
                " | Last Sale: " + row[3]);
        }
    }

    // Query 6: Get low stock products with supplier info
    // Joins: products + suppliers
    public void query6_LowStockWithSupplier() {
        System.out.println("\n=== Query 6: Low Stock Products & Supplier ===");
        List<Object[]> results = em.createQuery(
            "SELECT p.name, p.stockQuantity, p.category, s.name, s.email " +
            "FROM Product p JOIN p.supplier s " +
            "WHERE p.stockQuantity < 50",
            Object[].class)
            .getResultList();
        for (Object[] row : results) {
            System.out.println("Product: " + row[0] +
                " | Stock: " + row[1] +
                " | Category: " + row[2] +
                " | Supplier: " + row[3] +
                " | Email: " + row[4]);
        }
    }
}
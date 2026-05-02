package pharmacy.system;

import jakarta.persistence.*;
import java.util.List;

public class PharmacyDAO {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public PharmacyDAO() {
        emf = Persistence.createEntityManagerFactory("PharmacyPU");
        em = emf.createEntityManager();
    }
    
    public Employee login(String username, String password) {
    try {
        return em.createQuery(
            "SELECT e FROM Employee e WHERE e.username = :u AND e.password = :p",
            Employee.class)
            .setParameter("u", username)
            .setParameter("p", password)
            .getSingleResult();
    } catch (NoResultException e) {
        return null;
    }
}

    // ==================== CLOSE ====================
    public void close() {
        if (em.isOpen()) em.close();
        if (emf.isOpen()) emf.close();
    }

    // ==================== TRANSACTION WRAPPER ====================
    private void executeInsideTransaction(Runnable action) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.run();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }
    }
        public Supplier getSupplierById(String id) {
            return em.find(Supplier.class, id);
        }
    // ==================== INSERT ====================
 public Supplier insertSupplier(Supplier s) 
 {

    if (em.find(Supplier.class, s.getSupplierID()) != null)
        return em.find(Supplier.class, s.getSupplierID());

    executeInsideTransaction(() -> em.persist(s));
    return s;
}
    public Product insertProduct(Product p) {
        executeInsideTransaction(() -> em.persist(p));
        return p;
    }

 public Employee insertEmployee(Employee e) {
    executeInsideTransaction(() -> em.persist(e));
    return e;
}

    public Customer insertCustomer(Customer c) {
        executeInsideTransaction(() -> em.persist(c));
        return c;
    }

  public Sale insertSale(Sale s) {
    executeInsideTransaction(() -> {
        // decrease stock for each item
        for (SaleItem item : s.getItems()) {
            Product p = em.find(Product.class, 
                         item.getProduct().getProductID());
            if (p != null) {
                int newStock = p.getStockQuantity() - item.getQuantity();
                if (newStock < 0) throw new RuntimeException(
                    "Not enough stock for: " + p.getName());
                p.setStockQuantity(newStock);
            }
        }
        // update employee total sales
        Employee emp = em.find(Employee.class, 
                       s.getEmployee().getEmployeeID());
        if (emp != null) {
            emp.setTotalSales(emp.getTotalSales() + s.getTotalAmount());
        }
        em.persist(s);
    });
    return s;
}
    // ==================== GET ====================
        public List<Customer> getAllCustomers() {
        return em.createQuery("SELECT c FROM Customer c", Customer.class)
                .getResultList();
    }

    public List<Product> getAllProducts() {
        return em.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }

    public List<Sale> getAllSales() {
        return em.createQuery("SELECT s FROM Sale s", Sale.class)
                .getResultList();
    }
    public List<Employee> getAllEmployees() {
        return em.createQuery("SELECT e FROM Employee e", Employee.class)
            .getResultList();
    }
    public Product getProductById(Long id) {
    return em.find(Product.class, id);
}

    // ==================== FIND HELPER ====================
    private <T> T find(Class<T> clazz, Object id) {
        return em.find(clazz, id);
    }

    // ==================== UPDATE ====================
    public void updateProductPrice(Long  productID, double newPrice) {
        executeInsideTransaction(() -> {
            Product p = find(Product.class, productID);
            if (p != null) p.setPrice(newPrice);
        });
    }

    public void updateProductStock(Long  productID, int newStock) {
        executeInsideTransaction(() -> {
            Product p = find(Product.class, productID);
            if (p != null) p.setStockQuantity(newStock);
        });
    }

    public void updateCustomerAmountDue(long customerID, double amount) {
        executeInsideTransaction(() -> {
            Customer c = find(Customer.class, customerID);
            if (c != null) c.setAmountDue(amount);
        });
    }

    public void updateEmployeePassword(String employeeID, String newPassword) {
        executeInsideTransaction(() -> {
            Employee e = find(Employee.class, employeeID);
            if (e != null) e.setPassword(newPassword);
        });
    }

    // ==================== DELETE ====================
    public boolean deleteProduct(Long  id) {
        final boolean[] deleted = {false};

        executeInsideTransaction(() -> {
            Product p = em.find(Product.class, id);
            if (p != null) {
                em.remove(p);
                deleted[0] = true;
            }
        });

        return deleted[0];
    }

    public boolean deleteCustomer(Long  id) {
        final boolean[] deleted = {false};

        executeInsideTransaction(() -> {
            Customer c = em.find(Customer.class, id);
            if (c != null) {
                em.remove(c);
                deleted[0] = true;
            }
        });

        return deleted[0];
    }

 public boolean deleteEmployee(Long id) {

    final boolean[] deleted = {false};

    executeInsideTransaction(() -> {
        Employee e = em.find(Employee.class, id);
        if (e != null) {
            em.remove(e);
            deleted[0] = true;
        }
    });

    return deleted[0];
}
 
 
 
 
 
 //====================== Supplier ======================
 public List<Supplier> getAllSuppliers() {
    return em.createQuery("SELECT s FROM Supplier s", Supplier.class)
            .getResultList();
}

public void deleteSupplier(Supplier s) {
    executeInsideTransaction(() -> {
        Supplier managed = em.find(Supplier.class, s.getSupplierID());
        if (managed != null) {
            em.remove(managed);
        }
    });
}
 






    // ==================== QUERIES ====================

public List<Object[]> getQuery1() {
    return em.createQuery(
        "SELECT p.name, p.category, p.price, p.stockQuantity, s.name, s.email " +
        "FROM Product p JOIN p.supplier s " +
        "ORDER BY p.category",
        Object[].class).getResultList();
}


public List<Object[]> getQuery2() {
    return em.createQuery(
        "SELECT s.saleID, s.saleDate, s.totalAmount, s.payment, e.name, c.name " +
        "FROM Sale s JOIN s.employee e JOIN s.customer c " +
        "ORDER BY s.saleDate DESC",
        Object[].class).getResultList();
}


public List<Object[]> getQuery3() {
    return em.createQuery(
        "SELECT si.sale.saleID, c.name, p.name, si.quantity, si.lineTotal " +
        "FROM SaleItem si JOIN si.product p JOIN si.sale s JOIN s.customer c " +
        "ORDER BY si.sale.saleID",
        Object[].class).getResultList();
}


public List<Object[]> getQuery4() {
    return em.createQuery(
        "SELECT e.name, e.role, COUNT(s.saleID), SUM(s.totalAmount) " +
        "FROM Sale s JOIN s.employee e " +
        "GROUP BY e.name, e.role " +
        "ORDER BY SUM(s.totalAmount) DESC",
        Object[].class).getResultList();
}


public List<Object[]> getQuery5() {
    return em.createQuery(
        "SELECT c.name, c.phone, c.amountDue, MAX(s.saleDate) " +
        "FROM Sale s JOIN s.customer c " +
        "WHERE c.amountDue > 0 " +
        "GROUP BY c.name, c.phone, c.amountDue " +
        "ORDER BY c.amountDue DESC",
        Object[].class).getResultList();
}


public List<Object[]> getQuery6() {
    return em.createQuery(
        "SELECT p.name, p.stockQuantity, p.category, sup.name, sup.email " +
        "FROM Product p JOIN p.supplier sup " +
        "WHERE p.stockQuantity <= 10 " +        // ← كل المنتجات اللي stock <= 10
        "ORDER BY p.stockQuantity ASC",
        Object[].class).getResultList();
}

public List<Object[]> getQuery7() {
    return em.createQuery(
        "SELECT p.name, p.category, sup.name, e.name, SUM(si.quantity) " +
        "FROM SaleItem si JOIN si.product p JOIN p.supplier sup JOIN si.sale s JOIN s.employee e " +
        "GROUP BY p.name, p.category, sup.name, e.name " +
        "ORDER BY SUM(si.quantity) DESC",
        Object[].class).getResultList();
}
}
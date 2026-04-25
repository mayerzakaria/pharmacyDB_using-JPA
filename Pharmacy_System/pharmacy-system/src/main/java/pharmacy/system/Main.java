package pharmacy.system;

import jakarta.persistence.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        PharmacyDAO dao = new PharmacyDAO();

        // ── INSERT ──
        Supplier supplier = new Supplier(
            "S002", "HealthyMed Co",
            "contact@healthymed.com", "Giza"
        );
        dao.insertSupplier(supplier);

        Product product = new Product(
            "Ibuprofen", "Medicine", 25.0,
            40, LocalDate.now().plusMonths(18),
            false, "400mg"
        );
        product.setSupplier(supplier);
        dao.insertProduct(product);

        Employee employee = new Employee(
            "E002", "Ali",
            "AliUser", "5678", "Pharmacist"
        );
        dao.insertEmployee(employee);

        Customer customer = new Customer(
            "Sara", "01098765432",
            "Alexandria", 200.0, "2025-03-01"
        );
        dao.insertCustomer(customer);

        // ── UPDATE ──
        dao.updateProductPrice(1, 35.0);
        dao.updateProductStock(1, 150);
        dao.updateCustomerAmountDue(1, 300.0);
        dao.updateEmployeePassword("E001", "newpass123");

        // ── QUERIES ──
        dao.query1_ProductsWithSupplier();
        dao.query2_SalesWithEmployeeAndCustomer();
        dao.query3_SaleItemsWithProducts();
        dao.query4_TotalSalesPerEmployee();
        dao.query5_CustomersWithDebt();
        dao.query6_LowStockWithSupplier();

        dao.close();
    }
}
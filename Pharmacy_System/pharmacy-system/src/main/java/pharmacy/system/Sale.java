package pharmacy.system;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleID;

    private LocalDate saleDate;

    private double totalAmount;

    private String payment;

    // 🔥 Employee relation (many sales → one employee)
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    // 🔥 Customer relation (optional but recommended)
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // 🔥 FIX: replace HashMap with SaleItem list
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleItem> items = new ArrayList<>();

    // ---------------- Constructor ----------------

    public Sale() {
        this.saleDate = LocalDate.now();
        this.totalAmount = 0.0;
        this.payment = "Cash";
    }

    // ---------------- Getters/Setters ----------------

    public Long getSaleID() {
        return saleID;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }
    public void setEmployee(Employee employee) {
    this.employee = employee;
}

public void setCustomer(Customer customer) {
    this.customer = customer;
}

    // ---------------- Business Logic ----------------

    public void calculateTotal() {
        double total = 0;

        for (SaleItem item : items) {
            total += item.getLineTotal();
        }

        this.totalAmount = applyDiscount(total);
    }

    public double applyDiscount(double total) {
        double rate = 0;

        if (total >= 1000) rate = 0.10;
        else if (total >= 500) rate = 0.05;

        return total - (total * rate);
    }
}
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
@Column(name = "sale_id")
private Long saleID;
    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "payment_method")
    private String payment;

    // ================= Employee =================
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // ================= Customer =================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // ================= Sale Items =================
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items = new ArrayList<>();

    // ================= Constructor =================
    public Sale() {
        this.saleDate = LocalDate.now();
        this.payment = "Cash";
        this.totalAmount = 0.0;
    }

    // ================= Getters =================
    public Long getSaleID() {
        return saleID;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPayment() {
        return payment;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    // ================= Setters =================
    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }

    public void setEmployee(Employee employee) {
        if (employee == null)
            throw new IllegalArgumentException("Employee cannot be null");
        this.employee = employee;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // ================= Helper Methods =================
    public void addItem(SaleItem item) {
        items.add(item);
        item.setSale(this);
    }

    public void removeItem(SaleItem item) {
        items.remove(item);
        item.setSale(null);
    }

    // ================= Business Logic =================
    public void calculateTotal() {
        double sum = items.stream()
                .mapToDouble(SaleItem::getLineTotal)
                .sum();

        this.totalAmount = applyDiscount(sum);
    }

    private double applyDiscount(double total) {
        double rate = 0.0;

        if (total >= 1000) rate = 0.10;
        else if (total >= 500) rate = 0.05;

        return total - (total * rate);
    }
    
    

    // ================= Lifecycle Safety =================
    @PrePersist
    public void prePersist() {
        if (saleDate == null) {
            saleDate = LocalDate.now();
        }
    }

    // ================= Display =================
    @Override
    public String toString() {
        return "Sale{" +
                "saleID=" + saleID +
                ", saleDate=" + saleDate +
                ", totalAmount=" + totalAmount +
                ", payment='" + payment + '\'' +
                '}';
    }
}
package pharmacy.system;

import jakarta.persistence.*;

@Entity
@Table(name = "sale_items")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================= Sale Relation =================
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    // ================= Product Relation =================
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "line_total", nullable = false)
    private double lineTotal;

    // ---------------- Constructors ----------------
    public SaleItem() {}

    public SaleItem(Sale sale, Product product, int quantity) {
        this.sale = sale;
        this.product = product;
        this.quantity = quantity;
        calculateLineTotal();
    }

    // ---------------- Getters & Setters ----------------
    public Long getId() {
        return id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        calculateLineTotal();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateLineTotal();
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }

    // ---------------- Business Logic ----------------
    public void calculateLineTotal() {
        if (product != null) {
            this.lineTotal = this.product.getPrice() * this.quantity;
        } else {
            this.lineTotal = 0;
        }
    }

    // ---------------- Debug ----------------
    @Override
    public String toString() {
        return "SaleItem{" +
                "id=" + id +
                ", product=" + (product != null ? product.getName() : null) +
                ", quantity=" + quantity +
                ", lineTotal=" + lineTotal +
                '}';
    }
}
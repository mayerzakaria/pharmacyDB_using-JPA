package pharmacy.system;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productID")
    private int productID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "stockQuantity", nullable = false)
    private int stockQuantity;

    @Column(name = "expiryDate", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "isPrescriptionRequired")
    private boolean isPrescriptionRequired;

    // ← merged from Drug class
    @Column(name = "dosage")
    private String dosage;

    // ← will link to Supplier later
    @ManyToOne
    @JoinColumn(name = "supplierID")
    private Supplier supplier;

    //----------------------- Constructors -----------------------
    public Product() {}

    public Product(String name, String category, double price,
                   int stockQuantity, LocalDate expiryDate,
                   boolean isPrescriptionRequired, String dosage) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.expiryDate = expiryDate;
        this.isPrescriptionRequired = isPrescriptionRequired;
        this.dosage = dosage;
    }

    //----------------------- Getters & Setters -----------------------
    public int getProductID() { return productID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public boolean isIsPrescriptionRequired() { return isPrescriptionRequired; }
    public void setIsPrescriptionRequired(boolean isPrescriptionRequired) {
        this.isPrescriptionRequired = isPrescriptionRequired;
    }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }
}
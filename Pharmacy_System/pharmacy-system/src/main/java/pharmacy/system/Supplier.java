/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacy.system;

/**
 *
 * @author Mayer
 */
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @Column(name = "supplierID")
    private String supplierID;

    @Column(nullable = false)
    private String name;

    private String email;
    private String location;

    // One supplier → many products
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    //----------------------- Constructors -----------------------
    public Supplier() {}

    public Supplier(String supplierID, String name,
                    String email, String location) {
        this.supplierID = supplierID;
        this.name = name;
        this.email = email;
        this.location = location;
    }

    //----------------------- Getters & Setters -----------------------
    public String getSupplierID() { return supplierID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<Product> getProducts() { return products; }
}

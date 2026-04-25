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
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerID")
    private int customerID;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String address;

    // merged from CustomerRecords
    @Column(name = "amountDue")
    private double amountDue;

    @Column(name = "lastPaymentDate")
    private String lastPaymentDate;

    // One customer → many sales
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Sale> sales = new ArrayList<>();

    //----------------------- Constructors -----------------------
    public Customer() {}

    public Customer(String name, String phone, String address,
                    double amountDue, String lastPaymentDate) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.amountDue = amountDue;
        this.lastPaymentDate = lastPaymentDate;
    }

    //----------------------- Getters & Setters -----------------------
    public int getCustomerID() { return customerID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getAmountDue() { return amountDue; }
    public void setAmountDue(double amountDue) { this.amountDue = amountDue; }

    public String getLastPaymentDate() { return lastPaymentDate; }
    public void setLastPaymentDate(String lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    //----------------------- Business Logic -----------------------
    public void payDueAmount(double amount) {
        if (amount > 0 && amount <= amountDue) {
            amountDue -= amount;
        }
    }
}

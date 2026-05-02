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
import java.time.LocalDate;
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
    private LocalDate  lastPaymentDate;

    // One customer → many sales
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Sale> sales = new ArrayList<>() ;

    //----------------------- Constructors -----------------------
    public Customer() {}

    public Customer(String name, String phone, String address,
                    double amountDue,   LocalDate lastPaymentDate) {
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
    public void setAmountDue(double amountDue){
        if(amountDue >= 0)
        this.amountDue = amountDue;
    }

    public LocalDate getLastPaymentDate() { return lastPaymentDate; }
    public void setLastPaymentDate(LocalDate lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    //----------------------- Business Logic -----------------------
    
        public void addSale(Sale sale) {
            sales.add(sale);
            sale.setCustomer(this);
        }
        public boolean payDueAmount(double amount) {
        if (amount > 0 && amount <= amountDue) {
            amountDue -= amount;
            return true;
        }
        return false;
    }
    public String getCustomerInfo() {
        return name + " - " + phone + " - Due: " + amountDue;
    }
    @Override
    public String toString() {
        return getCustomerInfo();
    }
}
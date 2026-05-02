package pharmacy.system;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(name = "totalSales")
    private double totalSales;

    // One employee → many sales
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Sale> salesHistory = new ArrayList<>();

    //----------------------- Constructors -----------------------
    public Employee() {}

    public Employee(String name, String username, String password, String role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.totalSales = 0.0;
    }

    //----------------------- Getters & Setters -----------------------
    public Long getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Long employeeID) {
        this.employeeID = employeeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public List<Sale> getSalesHistory() {
        return salesHistory;
    }

    //----------------------- Business Logic -----------------------
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public double calculateBonus(double bonusPercentage) {
        return totalSales * (bonusPercentage / 100);
    }

    public void addSale(double amount) {
        this.totalSales += amount;
    }

    public void addSale(Sale sale) {
        salesHistory.add(sale);
        sale.setEmployee(this);
    }

    @Override
    public String toString() {
        return name + " (" + role + ") - Sales: " + totalSales;
    }
}
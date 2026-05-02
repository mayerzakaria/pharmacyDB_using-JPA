package pharmacy.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MainGUI extends JFrame {

    private PharmacyDAO dao;

    private DefaultTableModel employeeModel;
    private DefaultTableModel customerModel;
    private DefaultTableModel productModel;
    private DefaultTableModel saleModel;
    private DefaultTableModel supplierModel;

    private Employee loggedInEmployee;

public MainGUI(Employee emp) {
    this.loggedInEmployee = emp;
    dao = new PharmacyDAO();
        setTitle("Pharmacy System Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Employees", buildEmployeePanel());
        tabs.add("Customers", buildCustomerPanel());
        tabs.add("Products", buildProductPanel());
        tabs.add("Sales", buildSalesPanel());
        tabs.add("Suppliers", buildSupplierPanel());
        tabs.add("Queries", buildQueriesPanel());

                // ── Logout Button ──
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBackground(new Color(200, 50, 50));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to logout?",
                "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame();
            }
        });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setBackground(new Color(33, 90, 160));
        topBar.add(logoutBtn);

        add(topBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        loadEmployees();
        loadCustomers();
        loadProducts();
        loadSales();
         loadSuppliers();

        setVisible(true);
    }

    // ================= EMPLOYEES =================

    private JPanel buildEmployeePanel() {

        JPanel p = new JPanel(new BorderLayout());

        employeeModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Role"}, 0
        );

        JTable table = new JTable(employeeModel);

        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");

        add.addActionListener(e -> addEmployee());
        delete.addActionListener(e -> deleteEmployee(table));
        refresh.addActionListener(e -> loadEmployees());

        JPanel top = new JPanel();
        top.add(add);
        top.add(delete);
        top.add(refresh);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        return p;
    }

   private void addEmployee() {

    String name = JOptionPane.showInputDialog("Name:");
    String role = JOptionPane.showInputDialog("Role:");
    String username = JOptionPane.showInputDialog("Username:");
    String password = JOptionPane.showInputDialog("Password:");

    if (name == null || role == null) return;

    Employee e = new Employee();
    e.setName(name);
    e.setRole(role);
    e.setUsername(username);
    e.setPassword(password);

    dao.insertEmployee(e);

    loadEmployees();
}

private void deleteEmployee(JTable table) {

    int row = table.getSelectedRow();
    if (row == -1) return;

    Long id = ((Number) table.getValueAt(row, 0)).longValue();

    dao.deleteEmployee(id);

    loadEmployees();
}
private void loadEmployees() {

    employeeModel.setRowCount(0);

    List<Employee> list = dao.getAllEmployees();

    for (Employee e : list) {
        employeeModel.addRow(new Object[]{
                e.getEmployeeID(),
                e.getName(),
                e.getRole()
        });
    }
}
    // ================= Supplier =================
private void deleteSupplier(JTable table) {

    int row = table.getSelectedRow();
    if (row == -1) return;

    String id = table.getValueAt(row, 0).toString();

    Supplier s = dao.getSupplierById(id);

    if (s != null) {
        dao.deleteSupplier(s);
    }

    loadSuppliers();
}
private void loadSuppliers() {

    supplierModel.setRowCount(0);

    List<Supplier> list = dao.getAllSuppliers();

    for (Supplier s : list) {
        supplierModel.addRow(new Object[]{
                s.getSupplierID(),
                s.getName(),
                s.getEmail(),
                s.getLocation()
        });
    }
}

private void addSupplier() {

    String id = JOptionPane.showInputDialog("Supplier ID:");
    String name = JOptionPane.showInputDialog("Name:");
    String email = JOptionPane.showInputDialog("Email:");
    String address = JOptionPane.showInputDialog("Address:");

    Supplier s = new Supplier(id, name, email, address);

    dao.insertSupplier(s);

    loadSuppliers();
}

     private JPanel buildSupplierPanel() 
    {
    JPanel p = new JPanel(new BorderLayout());

    supplierModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Email", "Address"}, 0
    );

    JTable table = new JTable(supplierModel);

    JButton add = new JButton("Add Supplier");
    JButton delete = new JButton("Delete Supplier"); // ← make sure this exists
    JButton refresh = new JButton("Refresh");

    add.addActionListener(e -> addSupplier());
    delete.addActionListener(e -> deleteSupplier(table)); // ← wired correctly
    refresh.addActionListener(e -> loadSuppliers());

    JPanel top = new JPanel();
    top.add(add);
    top.add(delete);  // ← make sure this is added
    top.add(refresh);

    p.add(top, BorderLayout.NORTH);
    p.add(new JScrollPane(table), BorderLayout.CENTER);

    return p;
}
        

    // ================= CUSTOMERS =================
private void deleteCustomer(JTable table) {

    int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select a customer first");
        return;
    }

    Long id = ((Number) table.getValueAt(row, 0)).longValue();

    boolean deleted = dao.deleteCustomer(id);

    if (deleted) {
        JOptionPane.showMessageDialog(this, "Customer deleted");
        loadCustomers();
    } else {
        JOptionPane.showMessageDialog(this, "Customer not found");
    }
}
    private JPanel buildCustomerPanel() {

    JPanel p = new JPanel(new BorderLayout());

    customerModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Phone", "Address", "Debt"}, 0
    );

    JTable table = new JTable(customerModel);

    JButton add = new JButton("Add");
    JButton delete = new JButton("Delete");
    JButton refresh = new JButton("Refresh");

    add.addActionListener(e -> addCustomer());
    delete.addActionListener(e -> deleteCustomer(table));
    refresh.addActionListener(e -> loadCustomers());

    JPanel top = new JPanel();
    top.add(add);
    top.add(delete);
    top.add(refresh);

    p.add(top, BorderLayout.NORTH);
    p.add(new JScrollPane(table), BorderLayout.CENTER);

    return p;
}

  private void addCustomer() {
    String name = JOptionPane.showInputDialog("Name:");
    String phone = JOptionPane.showInputDialog("Phone:");
    String address = JOptionPane.showInputDialog("Address:");
    String debtStr = JOptionPane.showInputDialog("Amount Due (0 if none):");
    
    double amountDue = 0.0;
    if (debtStr != null && !debtStr.isEmpty()) {
        try {
            amountDue = Double.parseDouble(debtStr);
        } catch (NumberFormatException e) {
            amountDue = 0.0;
        }
    }
    Customer c = new Customer(name, phone, address, 
                              amountDue, LocalDate.now());
    dao.insertCustomer(c);
    loadCustomers();
}

    private void loadCustomers() 
    {

        customerModel.setRowCount(0);

        List<Customer> list = dao.getAllCustomers();

        for (Customer c : list) {
            customerModel.addRow(new Object[]{
                    c.getCustomerID(),
                    c.getName(),
                    c.getPhone(),
                    c.getAddress(),
                    c.getAmountDue()
            });
        }
    }

    // ================= PRODUCTS =================

   private JPanel buildProductPanel() 
   {

    JPanel p = new JPanel(new BorderLayout());

          productModel = new DefaultTableModel(
              new String[]{"ID", "Name", "Category", "Price", 
                           "Stock", "Dosage", "Prescription", "Expiry"}, 0
          );

    JTable table = new JTable(productModel);

    // ── Buttons ──
    JButton add = new JButton("Add");
    JButton delete = new JButton("Delete");
    JButton updatePrice = new JButton("Update Price");
    JButton updateStock = new JButton("Update Stock");
    JButton refresh = new JButton("Refresh");

    // ── Actions ──
    add.addActionListener(e -> addProduct());

    delete.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product first");
            return;
        }
        Long id = ((Number) table.getValueAt(row, 0)).longValue();
        boolean deleted = dao.deleteProduct(id);
        if (deleted) {
            JOptionPane.showMessageDialog(this, "Product deleted ✅");
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this, "Product not found ❌");
        }
    });

    updatePrice.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product first");
            return;
        }
        Long id = ((Number) table.getValueAt(row, 0)).longValue();
        String input = JOptionPane.showInputDialog("Enter New Price:");
        if (input == null) return;
        try {
            double newPrice = Double.parseDouble(input);
            dao.updateProductPrice(id, newPrice);
            JOptionPane.showMessageDialog(this, "Price updated ✅");
            loadProducts();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price!");
        }
    });

    updateStock.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product first");
            return;
        }
        Long id = ((Number) table.getValueAt(row, 0)).longValue();
        String input = JOptionPane.showInputDialog("Enter New Stock:");
        if (input == null) return;
        try {
            int newStock = Integer.parseInt(input);
            dao.updateProductStock(id, newStock);
            JOptionPane.showMessageDialog(this, "Stock updated ✅");
            loadProducts();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid stock number!");
        }
    });

    refresh.addActionListener(e -> loadProducts());

    // ── Top Panel ──
    JPanel top = new JPanel();
    top.add(add);
    top.add(delete);
    top.add(updatePrice);
    top.add(updateStock);
    top.add(refresh);

    p.add(top, BorderLayout.NORTH);
    p.add(new JScrollPane(table), BorderLayout.CENTER);

    return p;
}

   private void addProduct() {
    try {
        String name = JOptionPane.showInputDialog("Name:");
        String category = JOptionPane.showInputDialog(
            "Category (Medicine/Hair Care/Oral Care):");
        double price = Double.parseDouble(
            JOptionPane.showInputDialog("Price:"));
        int stock = Integer.parseInt(
            JOptionPane.showInputDialog("Stock Quantity:"));
        String dosage = JOptionPane.showInputDialog(
            "Dosage (e.g. 500mg, or N/A if not medicine):");
        String prescStr = JOptionPane.showInputDialog(
            "Prescription Required? (yes/no):");
        boolean prescription = prescStr != null && 
                               prescStr.equalsIgnoreCase("yes");

        // expiry date = 1 year from now by default
        LocalDate expiry = LocalDate.now().plusYears(1);

        // get supplier
        List<Supplier> suppliers = dao.getAllSuppliers();
        if (suppliers.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Add a supplier first!");
            return;
        }

        // show supplier options
        String[] supplierNames = suppliers.stream()
            .map(s -> s.getSupplierID() + " - " + s.getName())
            .toArray(String[]::new);
        String chosen = (String) JOptionPane.showInputDialog(
            this, "Select Supplier:",
            "Supplier", JOptionPane.QUESTION_MESSAGE,
            null, supplierNames, supplierNames[0]);
        if (chosen == null) return;

        String supplierID = chosen.split(" - ")[0];
        Supplier supplier = dao.getSupplierById(supplierID);

        Product p = new Product(name, category, price, stock,
                                expiry, prescription, dosage, supplier);
        dao.insertProduct(p);
        
        JOptionPane.showMessageDialog(this, "Product added ✅");
        loadProducts();

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, 
            "Invalid input: " + ex.getMessage());
    }
}
    private void loadProducts() {
    productModel.setRowCount(0);
    List<Product> list = dao.getAllProducts();
    for (Product p : list) {
        productModel.addRow(new Object[]{
            p.getProductID(),
            p.getName(),
            p.getCategory(),
            p.getPrice(),
            p.getStockQuantity(),
            p.getDosage(),
            p.isPrescriptionRequired() ? "Yes" : "No",
            p.getExpiryDate()
        });
    }
}

    // ================= SALES (FULL LOGIC) =================
private void payCustomerDebt() {
    List<Customer> allCustomers = dao.getAllCustomers();
    List<Customer> debtors = allCustomers.stream()
        .filter(c -> c.getAmountDue() > 0)
        .collect(java.util.stream.Collectors.toList());

    if (debtors.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No customers with outstanding debt.");
        return;
    }

    String[] options = debtors.stream()
        .map(c -> c.getCustomerID() + " - " + c.getName() +
                  " (Owes: " + String.format(java.util.Locale.ENGLISH, "%.2f", c.getAmountDue()) + ")")
        .toArray(String[]::new);

    String chosen = (String) JOptionPane.showInputDialog(
        this,
        "Select customer to pay debt:",
        "Pay Debt",
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]
    );

    if (chosen == null) return;

    int chosenId = Integer.parseInt(chosen.split(" - ")[0]);
    Customer customer = debtors.stream()
        .filter(c -> c.getCustomerID() == chosenId)
        .findFirst().orElse(null);

    if (customer == null) return;

    String input = JOptionPane.showInputDialog(
        this,
        "Customer: " + customer.getName() +
        "\nTotal Debt: " + String.format(java.util.Locale.ENGLISH, "%.2f", customer.getAmountDue()) +
        "\n\nEnter amount to pay:"
    );

    if (input == null || input.trim().isEmpty()) return;

    try {
        double payment = Double.parseDouble(input.trim());

        if (payment <= 0) {
            JOptionPane.showMessageDialog(this, "Amount must be greater than 0.");
            return;
        }

        if (payment > customer.getAmountDue()) {
            JOptionPane.showMessageDialog(this,
                "Amount exceeds debt!\nMax allowed: " +
                String.format(java.util.Locale.ENGLISH, "%.2f", customer.getAmountDue()));
            return;
        }

        double newDebt = customer.getAmountDue() - payment;
        dao.updateCustomerAmountDue(customer.getCustomerID(), newDebt);

        String msg = "Payment recorded!\n" +
                     "Customer: " + customer.getName() + "\n" +
                     "Paid: " + String.format(java.util.Locale.ENGLISH, "%.2f", payment) + "\n" +
                     "Remaining Debt: " + String.format(java.util.Locale.ENGLISH, "%.2f", newDebt);

        if (newDebt == 0) {
            msg += "\n\nCustomer is fully paid!";
        }

        JOptionPane.showMessageDialog(this, msg);
        loadCustomers();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid amount entered.");
    }
}

private JPanel buildSalesPanel() {
    JPanel p = new JPanel(new BorderLayout());

    saleModel = new DefaultTableModel(
            new String[]{"ID", "Date", "Total", "Payment"}, 0
    );

    JTable table = new JTable(saleModel);

    JButton add = new JButton("Add Sale");
    JButton payDebt = new JButton("Pay Debt");
    JButton refresh = new JButton("Refresh");

    add.addActionListener(e -> addSale());
    payDebt.addActionListener(e -> payCustomerDebt());
    refresh.addActionListener(e -> loadSales());

    JPanel top = new JPanel();
    top.add(add);
    top.add(payDebt);
    top.add(refresh);

    p.add(top, BorderLayout.NORTH);
    p.add(new JScrollPane(table), BorderLayout.CENTER);

    return p;
}
   private void addSale() {
    try {
        // ── اختار المنتج ──
        Long productId = Long.parseLong(
            JOptionPane.showInputDialog("Product ID:"));
        int qty = Integer.parseInt(
            JOptionPane.showInputDialog("Quantity:"));

        // ── نوع الدفع: نقدي أو آجل ──
        String[] paymentOptions = {"Cash", "Credit Card", "Deferred (آجل)"};
        String payment = (String) JOptionPane.showInputDialog(
            this, "Select Payment Method:",
            "Payment", JOptionPane.QUESTION_MESSAGE,
            null, paymentOptions, paymentOptions[0]);
        if (payment == null) return;

        // ── اختار الكاستومر ──
        List<Customer> customers = dao.getAllCustomers();
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add a customer first!");
            return;
        }
        String[] customerNames = customers.stream()
            .map(c -> c.getCustomerID() + " - " + c.getName())
            .toArray(String[]::new);
        String chosenCustomer = (String) JOptionPane.showInputDialog(
            this, "Select Customer:",
            "Customer", JOptionPane.QUESTION_MESSAGE,
            null, customerNames, customerNames[0]);
        if (chosenCustomer == null) return;
        Long customerId = Long.parseLong(chosenCustomer.split(" - ")[0]);
        Customer selectedCustomer = customers.stream()
            .filter(c -> c.getCustomerID() == customerId.intValue())
            .findFirst().orElse(null);

        // ── اختار الموظف ──
        List<Employee> employees = dao.getAllEmployees();
        String[] empNames = employees.stream()
            .map(e -> e.getEmployeeID() + " - " + e.getName())
            .toArray(String[]::new);
        String chosenEmp = (String) JOptionPane.showInputDialog(
            this, "Select Employee:",
            "Employee", JOptionPane.QUESTION_MESSAGE,
            null, empNames, empNames[0]);
        if (chosenEmp == null) return;
        Long empId = Long.parseLong(chosenEmp.split(" - ")[0]);
        Employee selectedEmployee = employees.stream()
            .filter(e -> e.getEmployeeID() == empId.longValue())
            .findFirst().orElse(null);

        // ── جيب المنتج ──
        Product product = dao.getProductById(productId);
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Product not found");
            return;
        }

        // ── اعمل الـ Sale ──
        Sale sale = new Sale();
        sale.setEmployee(selectedEmployee);
        sale.setCustomer(selectedCustomer);
        sale.setPayment(payment);

        SaleItem item = new SaleItem(sale, product, qty);
        sale.addItem(item);
        sale.calculateTotal();

        dao.insertSale(sale);

        // ── لو آجل: ضيف التوتال على amountDue ──
        if (payment.startsWith("Deferred")) {
            double currentDue = selectedCustomer.getAmountDue();
            dao.updateCustomerAmountDue(
                selectedCustomer.getCustomerID(),
                currentDue + sale.getTotalAmount());
            JOptionPane.showMessageDialog(this,
                "Sale recorded as DEFERRED.\n" +
                "Total added to customer's debt: " + sale.getTotalAmount() +
                "\nNew Amount Due: " + (currentDue + sale.getTotalAmount()));
        } else {
            JOptionPane.showMessageDialog(this,
                "Sale complete!\nTotal = " + sale.getTotalAmount());
        }

        loadSales();
        loadCustomers(); // علشان يحدث الـ Debt في جدول الكاستومرز

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Sale error: " + e.getMessage());
    }
}

    private void loadSales() {

        saleModel.setRowCount(0);

        List<Sale> list = dao.getAllSales();

        for (Sale s : list) {
            saleModel.addRow(new Object[]{
                    s.getSaleID(),
                    s.getSaleDate(),
                    s.getTotalAmount(),
                    s.getPayment()
            });
        }
    }
    
    // ======== queries=======
private JPanel buildQueriesPanel() {

    JPanel p = new JPanel(new BorderLayout());

    DefaultTableModel queryModel = new DefaultTableModel();
    JTable resultTable = new JTable(queryModel);
    resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    JButton q1 = new JButton("Q1: Products & Supplier");
    JButton q2 = new JButton("Q2: Sales + Employee + Customer");
    JButton q3 = new JButton("Q3: Items + Customer + Product");
    JButton q4 = new JButton("Q4: Employee Performance");
    JButton q5 = new JButton("Q5: Debt + Last Purchase");
    JButton q6 = new JButton("Q6: Low Stock + Supplier");
    JButton q7 = new JButton("Q7: Top Products + Who Sold");

    q1.addActionListener(e -> {
        queryModel.setRowCount(0); queryModel.setColumnCount(0);
        queryModel.addColumn("Product");
        queryModel.addColumn("Category");
        queryModel.addColumn("Price");
        queryModel.addColumn("Stock");
        queryModel.addColumn("Supplier");
        queryModel.addColumn("Email");
        for (Object[] row : dao.getQuery1()) queryModel.addRow(row);
    });

    q2.addActionListener(e -> {
        queryModel.setRowCount(0); queryModel.setColumnCount(0);
        queryModel.addColumn("Sale ID");
        queryModel.addColumn("Date");
        queryModel.addColumn("Total");
        queryModel.addColumn("Payment");
        queryModel.addColumn("Employee");
        queryModel.addColumn("Customer");
        for (Object[] row : dao.getQuery2()) queryModel.addRow(row);
    });

    q3.addActionListener(e -> {
        queryModel.setRowCount(0); queryModel.setColumnCount(0);
        queryModel.addColumn("Sale ID");
        queryModel.addColumn("Customer");
        queryModel.addColumn("Product");
        queryModel.addColumn("Qty");
        queryModel.addColumn("Line Total");
        for (Object[] row : dao.getQuery3()) queryModel.addRow(row);
    });

    q4.addActionListener(e -> {
        queryModel.setRowCount(0); queryModel.setColumnCount(0);
        queryModel.addColumn("Employee");
        queryModel.addColumn("Role");
        queryModel.addColumn("# Sales");
        queryModel.addColumn("Total Revenue");
        for (Object[] row : dao.getQuery4()) queryModel.addRow(row);
    });

    q5.addActionListener(e -> {
        queryModel.setRowCount(0); queryModel.setColumnCount(0);
        queryModel.addColumn("Customer");
        queryModel.addColumn("Phone");
        queryModel.addColumn("Amount Due");
        queryModel.addColumn("Last Purchase");
        for (Object[] row : dao.getQuery5()) queryModel.addRow(row);
    });

    q6.addActionListener(e -> {
        queryModel.setRowCount(0); queryModel.setColumnCount(0);
        queryModel.addColumn("Product");
        queryModel.addColumn("Stock");
        queryModel.addColumn("Category");
        queryModel.addColumn("Supplier");
        queryModel.addColumn("Email");
        for (Object[] row : dao.getQuery6()) queryModel.addRow(row);
    });

    q7.addActionListener(e -> {
        queryModel.setRowCount(0); queryModel.setColumnCount(0);
        queryModel.addColumn("Product");
        queryModel.addColumn("Category");
        queryModel.addColumn("Supplier");
        queryModel.addColumn("Sold By");
        queryModel.addColumn("Total Qty Sold");
        for (Object[] row : dao.getQuery7()) queryModel.addRow(row);
    });

    JPanel buttons = new JPanel(new GridLayout(2, 4, 5, 5));
    buttons.add(q1);
    buttons.add(q2);
    buttons.add(q3);
    buttons.add(q4);
    buttons.add(q5);
    buttons.add(q6);
    buttons.add(q7);

    p.add(buttons, BorderLayout.NORTH);
    p.add(new JScrollPane(resultTable), BorderLayout.CENTER);

    return p;
}    
    
}
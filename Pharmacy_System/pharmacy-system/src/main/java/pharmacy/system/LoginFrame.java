package pharmacy.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private PharmacyDAO dao = new PharmacyDAO();

    public LoginFrame() {
        setTitle("Pharmacy Login");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // ── Main Panel ──
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(245, 248, 255));

        // ── Header ──
        JLabel title = new JLabel("💊 Pharmacy System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(33, 90, 160));
        title.setBorder(new EmptyBorder(25, 0, 10, 0));
        main.add(title, BorderLayout.NORTH);

        // ── Form Panel ──
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 248, 255));
        form.setBorder(new EmptyBorder(10, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTextField userField = new JTextField(18);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 90, 160), 1, true),
            new EmptyBorder(5, 8, 5, 8)
        ));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPasswordField passField = new JPasswordField(18);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 90, 160), 1, true),
            new EmptyBorder(5, 8, 5, 8)
        ));

        // ── Row 0: Username ──
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        form.add(userLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        form.add(userField, gbc);

        // ── Row 1: Password ──
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        form.add(passLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        form.add(passField, gbc);

        main.add(form, BorderLayout.CENTER);

        // ── Button Panel ──
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginBtn.setBackground(new Color(33, 90, 160));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.setPreferredSize(new Dimension(120, 38));
        loginBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245, 248, 255));
        btnPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        btnPanel.add(loginBtn);
        main.add(btnPanel, BorderLayout.SOUTH);

        // ── Login Action ──
        loginBtn.addActionListener(e -> {
            String u = userField.getText();
            String p = new String(passField.getPassword());
            Employee emp = dao.login(u, p);
            if (emp != null) {
                dispose();
                new MainGUI(emp);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Wrong Username or Password ❌",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Enter key triggers login
        passField.addActionListener(e -> loginBtn.doClick());

        add(main);
        setVisible(true);
    }
}
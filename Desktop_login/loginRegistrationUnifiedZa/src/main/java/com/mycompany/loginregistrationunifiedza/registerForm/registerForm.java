package com.mycompany.loginregistrationunifiedza.registerForm;

import com.mycompany.loginregistrationunifiedza.loginForm.loginForm;
import com.mycompany.loginregistrationunifiedza.provinceDao.provinceDao;
import com.mycompany.loginregistrationunifiedza.userDao.userDao;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
/**
 *
 * @author chuma
 */

public class registerForm extends JFrame {

    private JTextField fullNameField, idNumberField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<ProvinceItem> provinceDropdown;
    private JRadioButton studentRadio, adminRadio;

    public registerForm() {
        setTitle("Create Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 540);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel typePanel = new JPanel();
        studentRadio = new JRadioButton("Student", true);
        adminRadio = new JRadioButton("University Admin");
        ButtonGroup group = new ButtonGroup();
        group.add(studentRadio);
        group.add(adminRadio);
        typePanel.add(studentRadio);
        typePanel.add(adminRadio);
        add(typePanel, gbc);
        row++;

        fullNameField = addField("Full Name", row++, gbc);
        idNumberField = addField("ID Number", row++, gbc);
        emailField = addField("Email Address", row++, gbc);
        phoneField = addField("Phone Number", row++, gbc);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        add(new JLabel("Password"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Confirm Password"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(15);
        add(confirmPasswordField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Province"), gbc);
        gbc.gridx = 1;
        provinceDropdown = new JComboBox<>();
        loadProvinces();
        add(provinceDropdown, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JButton createBtn = new JButton("CREATE ACCOUNT");
        createBtn.addActionListener(e -> handleRegister());
        add(createBtn, gbc);
        row++;

        gbc.gridy = row;
        JButton loginLinkBtn = new JButton("Already have an account? Log in");
        loginLinkBtn.setBorderPainted(false);
        loginLinkBtn.setContentAreaFilled(false);
        loginLinkBtn.setForeground(Color.BLUE);
        loginLinkBtn.addActionListener(e -> {
            new loginForm().setVisible(true);
            dispose();
        });
        add(loginLinkBtn, gbc);
    }

    private JTextField addField(String label, int row, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        JTextField field = new JTextField(15);
        add(field, gbc);
        return field;
    }

    private void loadProvinces() {
        Map<Integer, String> provinces = new provinceDao().getAllProvinces();
        for (Map.Entry<Integer, String> entry : provinces.entrySet()) {
            provinceDropdown.addItem(new ProvinceItem(entry.getKey(), entry.getValue()));
        }
    }

    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String idNumber = idNumberField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        ProvinceItem selectedProvince = (ProvinceItem) provinceDropdown.getSelectedItem();
        String userType = studentRadio.isSelected() ? "Student" : "UniversityAdmin";

       
        if (fullName.isEmpty() || idNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (idNumber.length() != 13 || !idNumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "ID Number must be exactly 13 digits.",
                    "Invalid ID Number", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.",
                    "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                    "Password Mismatch", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedProvince == null) {
            JOptionPane.showMessageDialog(this, "Please select a province.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        userDao userDAO = new userDao();
        boolean success = userDAO.registerUser(fullName, idNumber, email, phone,
                password, selectedProvince.getId(), userType);

        if (success) {
            JOptionPane.showMessageDialog(this, "Account created successfully! Please log in.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            new loginForm().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registration failed. That email or ID number might already be registered.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class ProvinceItem {
        private final int id;
        private final String name;

        public ProvinceItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }

        @Override
        public String toString() { return name; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new registerForm().setVisible(true));
    }
}
package com.mycompany.loginregistrationunifiedza.loginForm;

import com.mycompany.loginregistrationunifiedza.registerForm.registerForm;
import com.mycompany.loginregistrationunifiedza.userDao.userDao;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author chuma
 */
public class loginForm extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public loginForm() {
        setTitle("Welcome Back");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(360, 280);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(new JLabel("Log in to continue your application", SwingConstants.CENTER), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        add(new JLabel("Email Address"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        add(emailField, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        add(new JLabel("Password"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        JButton loginBtn = new JButton("LOG IN");
        loginBtn.addActionListener(e -> handleLogin());
        add(loginBtn, gbc);

        gbc.gridy = 4;
        JButton registerLinkBtn = new JButton("Don't have an account? Register");
        registerLinkBtn.setBorderPainted(false);
        registerLinkBtn.setContentAreaFilled(false);
        registerLinkBtn.setForeground(Color.BLUE);
        registerLinkBtn.addActionListener(e -> {
            new registerForm().setVisible(true);
            dispose();
        });
        add(registerLinkBtn, gbc);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        userDao userDAO = new userDao();
        int userId = userDAO.validateLogin(email, password);

        if (userId != -1) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome back.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
            JOptionPane.showMessageDialog(this, "Incorrect email or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new loginForm().setVisible(true));
    }
}
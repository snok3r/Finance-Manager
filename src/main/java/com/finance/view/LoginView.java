package com.finance.view;

import com.finance.database.DBHelper;
import com.finance.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginView extends JFrame {
    private JButton buttonLogin;
    private JButton buttonRegister;
    private JTextField textFieldUsername;
    private JPasswordField textFieldPassword;

    private static LoginView loginView;
    private static DBHelper db = DBHelper.getInstance();

    private LoginView() throws HeadlessException {
        super("Welcome to Finance Manager!");

        setSize(310, 120);
        setMinimumSize(new Dimension(310, 120));
        setMaximumSize(new Dimension(310, 120));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents();
        pack();

        buttonLogin.addActionListener(e -> performAction(LoginAction.LOGIN));
        buttonRegister.addActionListener(e -> performAction(LoginAction.REGISTER));
        textFieldUsername.addActionListener(e -> performAction(LoginAction.LOGIN));
        textFieldPassword.addActionListener(e -> performAction(LoginAction.LOGIN));
    }

    /**
     * @return login window
     */
    public static LoginView getLoginWindow() {
        if (loginView == null)
            loginView = new LoginView();
        return loginView;
    }

    /**
     * Hides loginView window
     */
    public void hideLoginWindow() {
        setVisible(false);
        textFieldPassword.setText("");
    }

    /**
     * Shows loginView window
     */
    public void showLoginWindow() {
        setVisible(true);
    }

    /**
     * Composing components in window
     */
    private void addComponents() {
        // main panel
        JPanel panel = new JPanel(new GridBagLayout());

        // labels and textfields
        textFieldUsername = new JTextField();
        addLabelAndTextField("Username", 0, textFieldUsername, panel);

        textFieldPassword = new JPasswordField();
        addLabelAndTextField("Password", 1, textFieldPassword, panel);

        // buttons
        buttonRegister = new JButton("Register");
        addButton(buttonRegister, 0, 0.0, panel);

        buttonLogin = new JButton("Login");
        addButton(buttonLogin, 1, 1.0, panel);

        add(panel);
    }

    private void addLabelAndTextField(String name, int row, JTextField textField, JPanel panel) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10;

        c.gridx = 0;
        c.gridy = row;
        panel.add(new JLabel(name, SwingConstants.CENTER), c);


        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = row;
        panel.add(textField, c);
    }

    private void addButton(JButton button, int col, double weightx, JPanel panel) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.ipady = 10;
        c.weightx = weightx;
        c.gridx = col;
        c.gridy = 2;
        panel.add(button, c);
    }

    /**
     * Method to loginView into existing account or register a new one.
     * Includes checking inputs and username existence.
     * Opens database when called and closes when done.
     *
     * @param action LOGIN or REGISTER
     */
    private void performAction(LoginAction action) {
        String username = textFieldUsername.getText();
        String password = String.copyValueOf(textFieldPassword.getPassword());

        if (!checkInputs(username, password))
            showError(LoginError.INVALID_INPUTS);
        else {
            try {
                db.connect();
                switch (action) {
                    case LOGIN:
                        User user = db.acquireUser(username, password);
                        if (user == null)
                            showError(LoginError.NO_SUCH_USER);
                        else {
                            hideLoginWindow();
                            MainView.open(user);
                        }
                        break;
                    case REGISTER:
                        if (db.getUserNames().contains(username)) // means user exists
                            showError(LoginError.USERNAME_TAKEN);
                        else {
                            db.addUser(new User(username, password));
                            JOptionPane.showConfirmDialog(this,
                                    "Registration successful!",
                                    "Registration completed",
                                    JOptionPane.CLOSED_OPTION,
                                    JOptionPane.PLAIN_MESSAGE);
                        }
                        break;
                }
            } catch (SQLException e) {
                showError(LoginError.DATABASE_ERROR);
            } finally {
                db.close();
            }
        }
    }

    /**
     * Shows modal dialog with <tt>error</tt>
     *
     * @param error type of error occurred
     */
    private void showError(LoginError error) {
        String strError = "unknown error";
        switch (error) {
            case INVALID_INPUTS:
                strError = "Username and password must be longer than 5 and less than 16 character.";
                break;
            case NO_SUCH_USER:
                strError = "Username or password is invalid.";
                break;
            case USERNAME_TAKEN:
                strError = "Username already exists.";
                break;
            case DATABASE_ERROR:
                strError = "Couldn't connect to database";
                break;
        }
        JOptionPane.showConfirmDialog(this,
                strError,
                "LoginView Failed",
                JOptionPane.CLOSED_OPTION,
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Checks inputs whether they long enough etc.
     *
     * @param username username to check
     * @param password password to check
     * @return true if both inputs are valid, false otherwise
     */
    private boolean checkInputs(String username, String password) {
        if (username == null || password == null)
            return false;

        if (username.length() < 5 || username.length() > 16)
            return false;
        else if (password.length() < 5 || password.length() > 16)
            return false;
        else
            return true;
    }

    private enum LoginError {
        INVALID_INPUTS, NO_SUCH_USER, USERNAME_TAKEN, DATABASE_ERROR
    }

    private enum LoginAction {
        LOGIN, REGISTER
    }
}

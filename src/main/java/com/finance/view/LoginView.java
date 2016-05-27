package com.finance.view;

import com.finance.database.DBHelper;
import com.finance.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginView {
    private JPanel panel;
    private JButton buttonLogin;
    private JButton buttonRegister;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private static JFrame frame;
    private static LoginView loginView;
    private static DBHelper db = DBHelper.getInstance();

    /**
     * Adds listeners etc.
     */
    private LoginView() {
        buttonLogin.addActionListener(e -> performAction(LoginAction.LOGIN));
        buttonRegister.addActionListener(e -> performAction(LoginAction.REGISTER));
        usernameField.addActionListener(e -> performAction(LoginAction.LOGIN));
        passwordField.addActionListener(e -> performAction(LoginAction.LOGIN));
    }

    /**
     * @return login window
     */
    public static LoginView getLoginView() {
        if (loginView == null)
            loginView = new LoginView();
        return loginView;
    }

    /**
     * Method to login into existing account or register a new one.
     * Includes checking inputs and username existence.
     * Opens com.finance.database when called and closes when done.
     *
     * @param action LOGIN or REGISTER
     */
    private void performAction(LoginAction action) {
        String username = usernameField.getText();
        String password = String.copyValueOf(passwordField.getPassword());

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
                            hideLoginView();
                            MainView.open(user);
                        }
                        break;
                    case REGISTER:
                        if (db.getUserNames().contains(username)) // means user exists
                            showError(LoginError.USERNAME_TAKEN);
                        else {
                            db.addUser(new User(username, password));
                            JOptionPane.showConfirmDialog(frame,
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
                strError = "Couldn't connect to com.finance.database";
                break;
        }
        JOptionPane.showConfirmDialog(frame,
                strError,
                "Login Failed",
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

    /**
     * Hides login window
     */
    public static void hideLoginView() {
        frame.setVisible(false);
    }

    /**
     * Shows login window
     */
    public static void showLoginView() {
        frame.setVisible(true);
    }

    /**
     * Initializes components and listeners
     */
    public static void main(String[] args) {
        frame = new JFrame("Login");
        loginView = getLoginView();

        frame.setContentPane(loginView.panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250, 130);
        frame.setMinimumSize(new Dimension(250, 130));
        frame.setMaximumSize(new Dimension(350, 150));
        frame.setLocationRelativeTo(null);
        showLoginView();
    }

    enum LoginError {
        INVALID_INPUTS, NO_SUCH_USER, USERNAME_TAKEN, DATABASE_ERROR
    }

    enum LoginAction {
        LOGIN, REGISTER
    }
}

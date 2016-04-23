package main.view;

import main.database.DBHelper;
import main.java.*;
import main.util.Category;
import main.util.RecordType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;

public class MainView {
    private JPanel panel;
    private JLabel labelUserInfo;
    private JTable tableRecords;
    private JPanel panelCenter;
    private JScrollPane scrollPane;
    private JButton buttonAddRecord;
    private JButton buttonAddAccount;

    private static JFrame frame;
    private static MainView mainView;
    private static DBHelper db = DBHelper.getInstance();
    private static User user;
    private static Account account;

    /**
     * Adds listeners etc.
     */
    private MainView() {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                user = null;
                LoginView.showLoginView();
            }
        });

        buttonAddRecord.addActionListener(e -> {
            addRecord();
            updateView(); // refreshing
        });

        buttonAddAccount.addActionListener(e -> {
            addAccount();
            updateView(); // refreshing
        });
    }

    /**
     * @return MainView window
     */
    public static MainView getMainView() {
        if (mainView == null)
            mainView = new MainView();
        return mainView;
    }

    /**
     * Hides MainView window
     */
    public static void hideMainView() {
        frame.setVisible(false);
    }

    /**
     * Shows MainView window
     */
    public static void showMainView() {
        frame.setVisible(true);
    }

    /**
     * Adds <tt>user</tt> to that window
     * and opens it
     *
     * @param user User of that window
     */
    public static void open(User user) {
        MainView.user = user;
        if (user.getNumOfAccounts() != 0)
            MainView.account = user.getDefaultAccount();
        main(null);
        getMainView().setView();
    }

    /**
     * Call after open(User user) and main(String[] args) are done
     */
    private void setView() {
        updateView();
        showMainView();
    }

    /**
     * Updates view (loads tables and updates balance)
     */
    private void updateView() {
        labelUserInfo.setText(String.format(
                "%s (%.2f)",
                user.getLogin(), user.getUserBalance())
        );
        loadTable();
    }

    /**
     * Loads records from user account into the
     * table
     */
    private void loadTable() {
        Object[] columnNames = {"Date", "Category", "Sum", "Description"};
        Object[][] data = null;

        if (user.getNumOfAccounts() != 0) {
            int numOfRecords = account.getNumOfRecords();
            data = new Object[numOfRecords][columnNames.length];

            Object[] recordsArray = account.getRecords().toArray();

            for (int j = 0; j < numOfRecords; j++) {
                Record record = ((Record) recordsArray[j]);
                data[j][0] = record.getDate().toString();
                data[j][1] = record.getCategory().toString();

                float amount = record.getAmount();
                if (record.getType() == RecordType.WITHDRAW)
                    amount = -amount;
                data[j][2] = amount;

                data[j][3] = record.getDescription();
            }
        }

        tableRecords.setModel(new DefaultTableModel(data, columnNames));
        tableRecords.setDefaultEditor(Object.class, null);
    }

    /**
     * Adds record to database and current user.
     * If something went wrong with <tt>db</tt>, does nothing.
     */
    private void addRecord() {
        RecordAdd dialog = RecordAdd.getDialog();
        if (dialog.getDescription() == null)
            return; // user decided not to add anything

        // getting values
        float amount = dialog.getAmount();
        RecordType recordType = dialog.getRecordType();
        Category category = dialog.getCategory();
        String desc = dialog.getDescription();

        Record recordToAdd = new Record(amount, recordType, category, desc);
        try {
            db.connect();
            db.addRecord(account, recordToAdd);
            db.close();
            account.addRecord(recordToAdd);
            showModal("Record has been added!", "Add Record", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException e) {
            showModal("Record has not been added!", "Add Record", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
        } catch (NullPointerException e) {
            showModal("Record has not been added!\nTry add account first.", "Add Record", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Adds account to database and current user.
     * If something went wrong with <tt>db</tt>, does nothing.
     */
    private void addAccount() {
        AccountAdd dialog = AccountAdd.getDialog();
        if (dialog.getDescription() == null)
            return;

        String desc = dialog.getDescription();

        Account account = new Account(user, desc);
        try {
            db.connect();
            db.addAccount(user, account);
            db.close();
            user.addAccount(account);
            if (user.getNumOfAccounts() == 1)
                MainView.account = user.getDefaultAccount();
            showModal("Account has been added!", "Add Account", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException e) {
            showModal("Account has not been added!", "Add Account", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showModal(String msg, String title, int option, int type) {
        JOptionPane.showConfirmDialog(frame, msg, title, option, type);
    }

    /**
     * Initializes components and listeners
     */
    public static void main(String[] args) {
        frame = new JFrame("Finance Manager > " + user.getLogin());
        mainView = new MainView();

        frame.setContentPane(mainView.panel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setMinimumSize(new Dimension(500, 400));
        frame.setMaximumSize(new Dimension(600, 500));
        frame.setLocationRelativeTo(null);
        frame.pack();
    }
}

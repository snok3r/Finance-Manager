package com.finance.view;

import com.finance.database.DBHelper;

import com.finance.model.*;
import com.finance.util.Category;
import com.finance.util.RecordType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.Vector;

public class MainView {
    private JPanel panel;
    private JLabel labelUserInfo;
    private JTable tableRecords;
    private JPanel panelCenter;
    private JScrollPane scrollPane;
    private JButton buttonAddRecord;
    private JButton buttonAddAccount;
    private JComboBox comboBoxAccounts;

    private static JFrame frame;
    private static MainView mainView;
    private static DBHelper db = DBHelper.getInstance();
    private static User user;
    private static Account currentAccount;

    /**
     * Adds listeners etc.
     */
    private MainView() {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                user = null;
                currentAccount = null;
                LoginView.showLoginView();
            }
        });

        buttonAddRecord.addActionListener(e -> {
            addRecord();
            updateView();
        });

        buttonAddAccount.addActionListener(e -> {
            addAccount();
            updateView();
        });

        comboBoxAccounts.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (String.valueOf(e.getItem()).equals("-"))
                    return;

                String desc = String.valueOf(e.getItem()).split(" ")[0];
                currentAccount = user.getAccount(desc);
                loadTable();
            }
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
        main(null);
        getMainView().loadAccounts();
        String currentAcc = getMainView().comboBoxAccounts.getSelectedItem().toString().split(" ")[0];
        currentAccount = user.getAccount(currentAcc);
        getMainView().loadTable();
        showMainView();
    }

    /**
     * Updates view (loads tables and account list)
     */
    private void updateView() {
        loadAccounts();
        loadTable();
    }

    /**
     * Loads records from user currentAccount into the
     * table
     */
    private void loadTable() {
        Object[] columnNames = {"Date", "Category", "Sum", "Description"};
        Object[][] data = null;

        if (currentAccount != null) {
            int numOfRecords = currentAccount.getNumOfRecords();
            data = new Object[numOfRecords][columnNames.length];

            Object[] recordsArray = currentAccount.getRecords().toArray();

            for (int j = 0; j < numOfRecords; j++) {
                Record record = ((Record) recordsArray[j]);
                data[j][0] = record.getDate().toString();
                data[j][1] = record.getCategory().toString();

                float amount = record.getAmount();
                if (record.getType() == RecordType.WITHDRAW)
                    amount = -amount;
                data[j][2] = String.format("%.2f", amount);

                data[j][3] = record.getDescription();
            }
        }

        tableRecords.setModel(new DefaultTableModel(data, columnNames));
        tableRecords.setDefaultEditor(Object.class, null);
    }

    /**
     * Adds all of user's accounts into combo box
     */
    private void loadAccounts() {
        Vector<String> accounts = new Vector<>();

        if (user.getNumOfAccounts() != 0) {
            for (Account a : user.getAccounts())
                accounts.add(String.format(
                        "%s (%.2f)",
                        a.getDescription(),
                        a.getBalance())
                );
        } else
            accounts.add("-");

        comboBoxAccounts.setModel(new DefaultComboBoxModel<>(accounts));
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
            db.addRecord(currentAccount, recordToAdd);
            db.close();
            currentAccount.addRecord(recordToAdd);
            showModal("Record has been added!", "Add Record", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException e) {
            showModal("Record has not been added!", "Add Record", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
        } catch (NullPointerException e) {
            showModal("Record has not been added!\nTry add account first.", "Add Record", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Adds currentAccount to database and current user.
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
            currentAccount = account;
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

        mainView.labelUserInfo.setText(user.getLogin());

        frame.setContentPane(mainView.panel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setMinimumSize(new Dimension(500, 400));
        frame.setMaximumSize(new Dimension(600, 500));
        frame.setLocationRelativeTo(null);
        frame.pack();
    }
}

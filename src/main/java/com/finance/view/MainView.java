package com.finance.view;

import com.finance.database.DBHelper;
import com.finance.model.Account;
import com.finance.model.Record;
import com.finance.model.User;
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

public class MainView extends JFrame {
    private JTable tableRecords;
    private JButton buttonAddRecord;
    private JButton buttonAddAccount;
    private JComboBox comboBoxAccounts;

    private static MainView mainView;
    private static DBHelper db = DBHelper.getInstance();
    private static User user;
    private static Account currentAccount;

    /**
     * Adds listeners etc.
     */
    private MainView() {
        super("Finance Manager");
        JPanel panel = new JPanel(new BorderLayout());

        setLocationRelativeTo(null);
        setSize(500, 500);
        setMinimumSize(new Dimension(500, 400));
        setMaximumSize(new Dimension(500, 500));
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        addComponent(panel);
        addActions();

        pack();
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
    public void hideMainView() {
        setVisible(false);
    }

    /**
     * Shows MainView window
     */
    public void showMainView() {
        setVisible(true);
    }

    private void addComponent(JPanel panel) {
        JPanel nPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.insets = new Insets(0, 20, 0, 0);
        nPanel.add(new JLabel("Current account:"), c);

        c.weightx = 1.0;
        c.gridwidth = 3;
        c.gridx = 1;
        c.insets = new Insets(0, 5, 0, 20);
        nPanel.add(comboBoxAccounts = new JComboBox(), c);
        panel.add(nPanel, BorderLayout.NORTH);

        JPanel cPanel = new JPanel();
        tableRecords = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableRecords);
        tableRecords.setFillsViewportHeight(true);
        tableRecords.setAutoCreateRowSorter(true);
        tableRecords.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        tableRecords.setShowHorizontalLines(true);
        tableRecords.setShowVerticalLines(true);
        cPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(cPanel, BorderLayout.CENTER);

        JPanel sPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.ipady = 10;

        c.insets = new Insets(1, 20, 1, 0);
        c.gridx = 0;
        sPanel.add(buttonAddAccount = new JButton("Add Account"), c);

        c.insets = new Insets(1, 10, 1, 20);
        c.gridx = 1;
        sPanel.add(buttonAddRecord = new JButton("Add Record"), c);
        panel.add(sPanel, BorderLayout.SOUTH);
    }

    private void addActions() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                user = null;
                currentAccount = null;
                LoginView.getLoginWindow().showLoginWindow();
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
     * Adds <tt>user</tt> to that window
     * and opens it
     *
     * @param user User of that window
     */
    public static void open(User user) {
        MainView.user = user;
        getMainView().loadAccounts();
        getMainView().setTitle("Finance Manager > " + user.getLogin());

        String currentAcc = getMainView().comboBoxAccounts.getSelectedItem().toString().split(" ")[0];
        currentAccount = user.getAccount(currentAcc);

        getMainView().loadTable();
        getMainView().showMainView();
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
        if (currentAccount != null)
            comboBoxAccounts.setSelectedItem(String.format(
                    "%s (%.2f)",
                    currentAccount.getDescription(),
                    currentAccount.getBalance()));
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
        JOptionPane.showConfirmDialog(this, msg, title, option, type);
    }
}

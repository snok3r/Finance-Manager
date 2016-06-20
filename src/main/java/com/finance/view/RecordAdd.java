package com.finance.view;

import com.finance.util.Category;
import com.finance.util.RecordType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RecordAdd extends JDialog {

    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldDescription;
    private JComboBox comboBoxCategory;
    private JCheckBox checkBoxWithdraw;
    private JFormattedTextField formattedTextFieldAmount;

    private float amount;
    private RecordType recordType;
    private Category category;
    private String description;

    public static RecordAdd getDialog() {
        return new RecordAdd();
    }

    public float getAmount() {
        return amount;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    private RecordAdd() {
        JPanel panel = new JPanel(new GridBagLayout());

        setLocationRelativeTo(getParent());
        setSize(400, 140);
        setMinimumSize(new Dimension(400, 150));
        setMaximumSize(new Dimension(600, 250));
        setContentPane(panel);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        addComponents(panel);
        addCategories();
        pack();

        setActions(panel);
        setVisible(true);
    }

    private void setActions(JPanel panel) {
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        panel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String desc = textFieldDescription.getText();
        String amount = formattedTextFieldAmount.getText();

        RecordAddCheck result = checkInputs(desc, amount);
        if (result == RecordAddCheck.OK) { // if inputs are valid
            if (showModal(RecordAddCheck.CONFIRM) == JOptionPane.YES_OPTION) {

                // setting those values
                this.amount = Float.valueOf(amount);
                category = (Category) comboBoxCategory.getSelectedItem();
                if (checkBoxWithdraw.isSelected())
                    recordType = RecordType.WITHDRAW;
                else
                    recordType = RecordType.DEPOSIT;
                this.description = desc;

                dispose();
            }
        } else
            showModal(result);
    }

    private void onCancel() {
        description = null;
        dispose();
    }

    /**
     * Shows modal dialog with <tt>result</tt>
     *
     * @param result result occurred
     */
    private int showModal(RecordAddCheck result) {
        String str = "unknown error";
        int type = JOptionPane.ERROR_MESSAGE;
        int option = JOptionPane.CLOSED_OPTION;

        switch (result) {
            case EMPTY_INPUT:
                str = "Description and amount must be filled in.";
                break;
            case BAD_INPUT:
                str = "Use floating point number in amount field (eg. 10.95)";
                break;
            case CONFIRM:
                str = "Are you sure?";
                type = JOptionPane.QUESTION_MESSAGE;
                option = JOptionPane.YES_NO_OPTION;
        }
        return JOptionPane.showConfirmDialog(this,
                str,
                "Add Record",
                option,
                type);
    }

    /**
     * Checks given inputs
     *
     * @param description description of the record
     * @param amount      amount of the record
     * @return EMPTY_INPUT if both field are empty,
     * BAD_INPUT if couldn't parse amount to float,
     * OK if all went ok.
     */
    private RecordAddCheck checkInputs(String description, String amount) {
        if (description.isEmpty() || amount.isEmpty())
            return RecordAddCheck.EMPTY_INPUT;

        try {
            Float.valueOf(amount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return RecordAddCheck.BAD_INPUT;
        }

        return RecordAddCheck.OK;
    }

    /**
     * Composing components in window
     */
    private void addComponents(JPanel panel) {
        buttonOK = new JButton("OK");
        buttonCancel = new JButton("Cancel");
        textFieldDescription = new JTextField();
        formattedTextFieldAmount = new JFormattedTextField();
        checkBoxWithdraw = new JCheckBox("Withdraw");
        comboBoxCategory = new JComboBox();

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridwidth = 2;
        c.gridx = 2;
        c.gridy = 0;
        panel.add(new JLabel("<html><b>Add new record</b></html>", SwingConstants.CENTER), c);


        addLabelAndTextField("Description", 1, textFieldDescription, panel);
        addLabelAndTextField("Amount", 2, formattedTextFieldAmount, panel);

        c.gridx = 4;
        c.gridy = 2;
        panel.add(checkBoxWithdraw, c);

        addLabelAndTextField("Category", 3, comboBoxCategory, panel);

        // buttons
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 2;
        c.gridy = 4;
        panel.add(buttonOK, c);

        c.gridwidth = 1;
        c.gridx = 4;
        c.gridy = 4;
        panel.add(buttonCancel, c);
    }

    private void addLabelAndTextField(String name, int row, JComponent component, JPanel panel) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 5, 0, 5);

        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = row;
        panel.add(new JLabel(name, SwingConstants.CENTER), c);

        c.weightx = 1.0;
        c.gridwidth = 2;
        c.gridx = 2;
        c.gridy = row;
        panel.add(component, c);
    }

    /**
     * Adds all categories to form
     */
    private void addCategories() {
        for (Category c : Category.values())
            comboBoxCategory.addItem(c);
    }

    enum RecordAddCheck {
        EMPTY_INPUT, BAD_INPUT, OK, CONFIRM
    }
}

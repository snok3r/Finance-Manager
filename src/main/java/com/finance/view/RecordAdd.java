package com.finance.view;

import com.finance.util.Category;
import com.finance.util.RecordType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RecordAdd extends JDialog {
    private JPanel contentPane;
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

    private static RecordAdd dialog;

    public static RecordAdd getDialog() {
        RecordAdd.main(null);
        return dialog;
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
        setLocationRelativeTo(getParent());
        setMinimumSize(new Dimension(400, 170));
        setMaximumSize(new Dimension(600, 250));
        setSize(600, 250);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        addCategories();

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
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
     * Adds all categories to form
     */
    private void addCategories() {
        for (Category c : Category.values())
            comboBoxCategory.addItem(c);
    }

    public static void main(String[] args) {
        dialog = new RecordAdd();
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0); // for debugging
    }

    enum RecordAddCheck {
        EMPTY_INPUT, BAD_INPUT, OK, CONFIRM
    }
}

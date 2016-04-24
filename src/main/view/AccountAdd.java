package main.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AccountAdd extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldDescription;

    private String description;

    private static AccountAdd dialog;

    public static AccountAdd getDialog() {
        AccountAdd.main(null);
        return dialog;
    }

    public String getDescription() {
        return description;
    }

    private AccountAdd() {
        setLocationRelativeTo(getParent());
        setMinimumSize(new Dimension(330, 120));
        setMaximumSize(new Dimension(330, 120));
        setSize(330, 120);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String desc = textFieldDescription.getText();
        if (!desc.isEmpty()) {
            description = desc;
            dispose();
        }
    }

    private void onCancel() {
        description = null;
        dispose();
    }

    public static void main(String[] args) {
        dialog = new AccountAdd();
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0); // for debbuging
    }
}

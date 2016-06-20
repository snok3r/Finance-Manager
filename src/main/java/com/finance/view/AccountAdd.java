package com.finance.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AccountAdd extends JDialog {
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldDescription;

    private String description;
    
    public static AccountAdd getDialog() {
        return new AccountAdd();
    }

    public String getDescription() {
        return description;
    }

    private AccountAdd() {
        JPanel panel = new JPanel(new GridBagLayout());

        setLocationRelativeTo(getParent());
        setSize(300, 80);
        setMinimumSize(new Dimension(300, 80));
        setMaximumSize(new Dimension(300, 80));
        setContentPane(panel);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        addComponents(panel);
        pack();

        setActions(panel);
        setVisible(true);
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

    private void setActions(JPanel panel) {
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
        panel.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    /**
     * Composing components in window
     */
    private void addComponents(JPanel panel) {
        buttonOK = new JButton("OK");
        buttonCancel = new JButton("Cancel");
        textFieldDescription = new JTextField();

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridwidth = 2;
        c.gridx = 2;
        c.gridy = 0;
        panel.add(new JLabel("<html><b>Add new account</b></html>", SwingConstants.CENTER), c);

        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(0, 5, 0, 5);
        panel.add(new JLabel("Description", SwingConstants.CENTER), c);

        c.weightx = 1.0;
        c.gridwidth = 2;
        c.gridx = 2;
        c.gridy = 1;
        c.insets = new Insets(0, 5, 0, 5);
        panel.add(textFieldDescription, c);

        c.gridwidth = 1;
        c.gridx = 3;
        c.gridy = 1;
        panel.add(Box.createHorizontalBox(), c);

        // buttons
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 2;
        c.gridy = 2;
        panel.add(buttonOK, c);

        c.gridx = 3;
        panel.add(buttonCancel, c);
    }
}

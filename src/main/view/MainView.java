package main.view;

import main.database.DBHelper;
import main.java.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainView {
    private JPanel panel;

    private static JFrame frame;
    private static MainView mainView;
    private static DBHelper db = DBHelper.getInstance();
    private static User user;

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
     * Sets <tt>user</tt> to that window
     *
     * @param user User of that window
     */
    public static void setUser(User user) {
        MainView.user = user;
    }

    /**
     * Initializes components and listeners
     */
    public static void main(String[] args) {
        frame = new JFrame("Finance Manager > " + user.getLogin());
        mainView = getMainView();

        frame.setContentPane(mainView.panel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setMinimumSize(new Dimension(500, 400));
        frame.setMaximumSize(new Dimension(600, 500));
        frame.setLocationRelativeTo(null);
    }
}

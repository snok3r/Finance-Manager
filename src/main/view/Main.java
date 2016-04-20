package main.view;

public class Main {
    private static LoginView loginView;

    public static void main(String[] args) {
        // load windows
        LoginView.main(null);

        loginView = LoginView.getLoginView();
    }
}

package financeapp;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // SwingUtilities.invokeLater osigurava da se GUI pokrene na ispravan način
        SwingUtilities.invokeLater(() -> {
            try {
                // Opcionalno: postavlja izgled aplikacije da liči na Windows prozor
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Ovdje pokrećemo LOGIN prozor.
                // To je sada jedina stvar koju Main radi.
                new LoginForm();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
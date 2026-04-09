package financeapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MainMenu extends JFrame {
    private JPanel panel1; // Dodaj ovu liniju
    private JButton btnFinance;
    private JButton btnSleep;
    private JButton btnStudy;
    private JButton btnHabit;
    private JButton btnSettings;
    private String trenutniKorisnik;

    public MainMenu(String username, String tema) {
        this.trenutniKorisnik = username;

        setTitle("Life Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Glavni panel sa marginama (Padding 30px)
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        // --- STILIZACIJA POZADINE PANELA ---
        if ("Zelena".equals(tema)) {
            mainPanel.setBackground(new Color(220, 240, 220)); // Svijetlo zelena
        } else if ("Tamna".equals(tema)) {
            mainPanel.setBackground(new Color(45, 45, 45)); // Tamno siva
        } else if ("Cyberpunk".equals(tema)) {
            mainPanel.setBackground(new Color(20, 0, 20)); // Tamno ljubičasta
        } else {
            mainPanel.setBackground(new Color(245, 245, 245)); // Standardna siva/bijela
        }

        // --- STILIZACIJA TRACKER DUGMADI ---
        btnFinance = createTrackerButton("Finance Tracker", tema);
        btnSleep = createTrackerButton("Sleep Tracker", tema);
        btnStudy = createTrackerButton("Study Planner", tema);
        btnHabit = createTrackerButton("Habit Tracker", tema);

        // --- RASPODJELA U 2x2 MREŽU ---
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.45; // Zauzimaju 90% visine (45% svaki red)
        gbc.insets = new Insets(15, 15, 15, 15); // Razmak između dugmadi

        // Red 0
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(btnFinance, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(btnSleep, gbc);

        // Red 1
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(btnStudy, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(btnHabit, gbc);

        // --- SETTINGS DUGME (Donji desni ugao) ---
        btnSettings = new JButton("⚙ Postavke Profila");
        btnSettings.setPreferredSize(new Dimension(150, 40));

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weighty = 0.1; // Zauzima preostalih 10% visine
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHEAST; // Desni donji ugao
        mainPanel.add(btnSettings, gbc);


        // AKCIJA ZA SLEEP TRACKER
        btnSleep.addActionListener(e -> {
            try {
                // Pozivamo SleepTrackerForm i šaljemo mu trenutnog korisnika
                new SleepTrackerForm(trenutniKorisnik);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Greška pri otvaranju Sleep Trackera: " + ex.getMessage());
            }
        });

        // AKCIJA ZA STUDY PLANNER
        btnStudy.addActionListener(e -> {
            try {
                new StudyPlannerForm(trenutniKorisnik);
                System.out.println("Study Planner pokrenut.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Greška: " + ex.getMessage());
            }
        });

        // AKCIJA ZA HABIT TRACKER
        btnHabit.addActionListener(e -> {
            try {
                new HabitTrackerForm(trenutniKorisnik);
                System.out.println("Habit Tracker (Water) pokrenut.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Greška: " + ex.getMessage());
            }
        });

        // LOGIKA ZA UPDATE (Tačka 5)
        btnSettings.addActionListener(e -> {
            new ProfileUpdateForm(trenutniKorisnik);
            this.dispose();
        });
        System.out.println("Inicijalizacija akcija...");
        // AKCIJA ZA FINANCE TRACKER
        btnFinance.addActionListener(e -> {
            try {
                FinanceTrackerForm forma = new FinanceTrackerForm();
                forma.setVisible(true);
                System.out.println("Forma bi trebala biti vidljiva.");
            } catch (Exception ex) {
                ex.printStackTrace(); // Ovo će nam reći tačnu liniju gdje puca
                JOptionPane.showMessageDialog(null, "Greška: " + ex.getMessage());
            }
        });

// "Habit Tracker dolazi uskoro!"));

        add(mainPanel); // Ovo je tvoja linija 75
        setVisible(true); // Ovo je tvoja linija 76
    }

    private JButton createTrackerButton(String text, String tema) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Provjera naziva teme (pazi na velika/mala slova kao u bazi!)
        if ("Zelena".equals(tema)) {
            btn.setBackground(new Color(40, 167, 69));
            btn.setForeground(Color.WHITE);
        } else if ("Tamna".equals(tema)) {
            btn.setBackground(new Color(70, 70, 70));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        } else if ("Cyberpunk".equals(tema)) {
            btn.setBackground(Color.BLACK);
            btn.setForeground(new Color(0, 255, 255)); // Neon plava slova
            btn.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255), 2)); // Roza okvir
        } else {
            // "Standard" ili bilo koja druga tema
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(50, 50, 50));
            btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        }

        return btn;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), 20, 20, true, true));
        panel1.setEnabled(true);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        btnFinance = new JButton();
        btnFinance.setHorizontalAlignment(0);
        btnFinance.setHorizontalTextPosition(0);
        btnFinance.setText("Finance Tracker");
        panel1.add(btnFinance, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(105, 30), null, 0, false));
        btnSleep = new JButton();
        btnSleep.setHorizontalAlignment(0);
        btnSleep.setHorizontalTextPosition(0);
        btnSleep.setText("Sleep Tracker");
        panel1.add(btnSleep, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(218, 30), null, 0, false));
        btnStudy = new JButton();
        btnStudy.setHorizontalAlignment(0);
        btnStudy.setHorizontalTextPosition(0);
        btnStudy.setText("Study Planner");
        panel1.add(btnStudy, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(105, 30), null, 0, false));
        btnHabit = new JButton();
        btnHabit.setHorizontalAlignment(0);
        btnHabit.setHorizontalTextPosition(0);
        btnHabit.setText("Habit Tracker");
        panel1.add(btnHabit, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(218, 30), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
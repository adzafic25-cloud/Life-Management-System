package financeapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class FinanceTrackerForm extends JFrame {
    private JPanel mainPanel;
    private JTextField descriptionField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JTable transactionTable;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel balanceLabel;

    private JComboBox<String> izvorPrihoda;
    private JTextField iznosPrihoda;
    private JComboBox<String> vrstaRashoda;
    private JTextField iznosRashoda;
    private JLabel rashod;
    private JButton updateButton;

    private TransactionManager manager;

    public FinanceTrackerForm() {
        setTitle("Finance Tracker - CRUD (No Description)");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            manager = new TransactionManager();
            osvjeziSve();
        } catch (Exception e) {
            System.err.println("Greška pri inicijalizaciji: " + e.getMessage());
        }

        transactionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && transactionTable.getSelectedRow() != -1) {
                int row = transactionTable.getSelectedRow();

                Object tipObj = transactionTable.getValueAt(row, 0);
                Object iznosObj = transactionTable.getValueAt(row, 1);

                String tip = (tipObj != null) ? tipObj.toString() : "";
                String iznos = (iznosObj != null) ? iznosObj.toString() : "0";

                String provera = tip.toLowerCase();
                if (provera.contains("plata") || provera.contains("stipendija") ||
                        provera.contains("poklon") || provera.contains("penzija") || provera.contains("prihod")) {
                    iznosPrihoda.setText(iznos);
                    iznosRashoda.setText("");
                    if (izvorPrihoda != null) izvorPrihoda.setSelectedItem(tip);
                } else {
                    iznosRashoda.setText(iznos);
                    iznosPrihoda.setText("");
                    if (vrstaRashoda != null) vrstaRashoda.setSelectedItem(tip);
                }
            }
        });

        addButton.addActionListener(e -> {
            try {
                String type = "";
                double amount = 0;

                if (!iznosPrihoda.getText().trim().isEmpty()) {
                    type = (izvorPrihoda.getSelectedItem() != null) ? izvorPrihoda.getSelectedItem().toString() : "Prihod";
                    amount = Double.parseDouble(iznosPrihoda.getText().trim());
                    iznosPrihoda.setText("");
                } else if (!iznosRashoda.getText().trim().isEmpty()) {
                    type = (vrstaRashoda.getSelectedItem() != null) ? vrstaRashoda.getSelectedItem().toString() : "Rashod";
                    amount = Double.parseDouble(iznosRashoda.getText().trim());
                    iznosRashoda.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Unesite iznos!");
                    return;
                }

                manager.addTransaction(new Transaction(type, amount, ""));
                osvjeziSve();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Greška pri unosu: " + ex.getMessage());
            }
        });

        if (updateButton != null) {
            updateButton.addActionListener(e -> {
                try {
                    int row = transactionTable.getSelectedRow();
                    if (row == -1) {
                        JOptionPane.showMessageDialog(null, "Prvo selektujte red u tabeli!");
                        return;
                    }

                    String stariTip = transactionTable.getValueAt(row, 0).toString();
                    String iznosIzTabele = transactionTable.getValueAt(row, 1).toString().replace(",", ".");
                    double stariIznos = Double.parseDouble(iznosIzTabele);

                    String noviTip = "";
                    double noviIznos = 0;

                    if (!iznosPrihoda.getText().trim().isEmpty()) {
                        noviTip = izvorPrihoda.getSelectedItem().toString();
                        noviIznos = Double.parseDouble(iznosPrihoda.getText().trim().replace(",", "."));
                    } else if (!iznosRashoda.getText().trim().isEmpty()) {
                        noviTip = vrstaRashoda.getSelectedItem().toString();
                        noviIznos = Double.parseDouble(iznosRashoda.getText().trim().replace(",", "."));
                    } else {
                        JOptionPane.showMessageDialog(null, "Unesite iznos!");
                        return;
                    }

                    manager.updateTransactionByValues(stariTip, stariIznos, new Transaction(noviTip, noviIznos, ""));

                    osvjeziSve();

                    iznosPrihoda.setText("");
                    iznosRashoda.setText("");

                    JOptionPane.showMessageDialog(null, "Uspješno ažurirano!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Greška: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }

        if (deleteButton != null) {
            deleteButton.addActionListener(e -> {
                int row = transactionTable.getSelectedRow();
                if (row != -1) {
                    String tip = transactionTable.getValueAt(row, 0).toString();
                    double iznos = Double.parseDouble(transactionTable.getValueAt(row, 1).toString());

                    manager.deleteTransactionByValues(tip, iznos);
                    osvjeziSve();
                } else {
                    JOptionPane.showMessageDialog(null, "Selektujte red!");
                }
            });
        }

        if (clearButton != null) {
            clearButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(null, "Obrisati sve podatke?", "Potvrda", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    manager.deleteAllTransactions();
                    osvjeziSve();
                }
            });
        }

        pack();
        setLocationRelativeTo(null);
    }

    private void osvjeziSve() {
        loadDataIntoTable();
        updateSummary();
    }

    private void loadDataIntoTable() {
        if (manager == null || transactionTable == null) return;
        ArrayList<Transaction> list = manager.getAllTransactions();

        String[] columns = {"Kategorija", "Iznos"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Transaction t : list) {
            model.addRow(new Object[]{t.getType(), t.getAmount()});
        }
        transactionTable.setModel(model);
    }

    private void updateSummary() {
        if (manager == null) return;
        double income = 0, expense = 0;

        for (Transaction t : manager.getAllTransactions()) {
            if (t.getType() == null) continue;
            String type = t.getType().toLowerCase();

            if (type.contains("plata") || type.contains("stipendija") ||
                    type.contains("poklon") || type.contains("penzija") || type.contains("prihod")) {
                income += t.getAmount();
            } else {
                expense += t.getAmount();
            }
        }

        double balance = income - expense;
        if (incomeLabel != null) incomeLabel.setText("Prihod: " + String.format("%.2f", income));
        if (expenseLabel != null) expenseLabel.setText("Rashod: " + String.format("%.2f", expense));
        if (balanceLabel != null) {
            balanceLabel.setText("Saldo: " + String.format("%.2f", balance));
            balanceLabel.setForeground(balance >= 0 ? new Color(0, 150, 0) : Color.RED);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(14, 5, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(10);
        label1.setText("PRAĆENJE LIČNIH FINANSIJA");
        mainPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Unesite iznos vašeg prihoda");
        mainPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Opišite ovaj izvor prihoda");
        mainPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addButton = new JButton();
        addButton.setText("Izračunaj");
        mainPanel.add(addButton, new com.intellij.uiDesigner.core.GridConstraints(11, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        incomeLabel = new JLabel();
        incomeLabel.setText("Prihod:0.0");
        mainPanel.add(incomeLabel, new com.intellij.uiDesigner.core.GridConstraints(12, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        expenseLabel = new JLabel();
        expenseLabel.setText("Rashod:0.0");
        mainPanel.add(expenseLabel, new com.intellij.uiDesigner.core.GridConstraints(12, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        balanceLabel = new JLabel();
        balanceLabel.setEnabled(false);
        balanceLabel.setText("Saldo:0.0");
        mainPanel.add(balanceLabel, new com.intellij.uiDesigner.core.GridConstraints(12, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(13, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        transactionTable = new JTable();
        scrollPane1.setViewportView(transactionTable);
        izvorPrihoda = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("plata");
        defaultComboBoxModel1.addElement("penzija");
        defaultComboBoxModel1.addElement("stipendija");
        defaultComboBoxModel1.addElement("poklon");
        izvorPrihoda.setModel(defaultComboBoxModel1);
        mainPanel.add(izvorPrihoda, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        iznosPrihoda = new JTextField();
        iznosPrihoda.setText("");
        mainPanel.add(iznosPrihoda, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rashod = new JLabel();
        rashod.setText("Unesite iznos rashoda");
        mainPanel.add(rashod, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        vrstaRashoda = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("racuni");
        defaultComboBoxModel2.addElement("hrana");
        defaultComboBoxModel2.addElement("zabava");
        defaultComboBoxModel2.addElement("prevoz");
        vrstaRashoda.setModel(defaultComboBoxModel2);
        mainPanel.add(vrstaRashoda, new com.intellij.uiDesigner.core.GridConstraints(10, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Opisite ovaj rashod");
        mainPanel.add(label4, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        iznosRashoda = new JTextField();
        iznosRashoda.setText("");
        mainPanel.add(iznosRashoda, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        clearButton = new JButton();
        clearButton.setText("Obrisi sve");
        mainPanel.add(clearButton, new com.intellij.uiDesigner.core.GridConstraints(11, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("Obrisi red");
        mainPanel.add(deleteButton, new com.intellij.uiDesigner.core.GridConstraints(11, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updateButton = new JButton();
        updateButton.setText("Azuriraj");
        mainPanel.add(updateButton, new com.intellij.uiDesigner.core.GridConstraints(11, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
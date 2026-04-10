package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import javax.swing.*;
import java.awt.*;

public class RegisterForm {
    private JPanel registerPanel;
    private JTextField txtImePrezime;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnRegister;
    private JButton btnBackToLogin;

    public RegisterForm() {
        JFrame frame = new JFrame("Registracija - Life Management System");
        frame.setContentPane(registerPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        btnRegister.addActionListener(e -> {
            String ime = txtImePrezime.getText();
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            String tema = "Standard";

            if (!isValidPassword(pass)) {
                JOptionPane.showMessageDialog(null,
                        "Lozinka mora imati više od 4 karaktera i barem jedan broj!");
                return;
            }

            if (ime.isEmpty() || user.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Sva polja su obavezna!");
                return;
            }

            try {
                MongoDatabase db = MongoDBConnection.getDatabase();
                MongoCollection<Document> collection = db.getCollection("users");


                if (collection.find(Filters.eq("username", user)).first() != null) {
                    JOptionPane.showMessageDialog(null, "Korisničko ime je zauzeto!");
                    return;
                }


                Document newUser = new Document("username", user)
                        .append("password", pass) 
                        .append("imePrezime", ime)
                        .append("theme", tema);

                collection.insertOne(newUser);

                JOptionPane.showMessageDialog(null, "Uspješna registracija, " + ime + "!");

                frame.dispose(); 
                new LoginForm();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Greška sa bazom: " + ex.getMessage());
            }
        });


        btnBackToLogin.addActionListener(e -> {
            frame.dispose(); 
            new LoginForm(); 
        });


        frame.pack();
        frame.setSize(450, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private boolean isValidPassword(String password) {

        if (password.length() <= 4) return false;


        return password.matches(".*\\d.*");
    }

    {

        
        $$$setupUI$$$();
    }

    
    private void $$$setupUI$$$() {
        registerPanel = new JPanel();
        registerPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(9, 1, new Insets(30, 30, 30, 30), -1, -1));
        btnRegister = new JButton();
        btnRegister.setText("Registruj se");
        registerPanel.add(btnRegister, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Ime i prezime");
        registerPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtImePrezime = new JTextField();
        registerPanel.add(txtImePrezime, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Username");
        registerPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtUsername = new JTextField();
        registerPanel.add(txtUsername, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Password");
        registerPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtPassword = new JPasswordField();
        registerPanel.add(txtPassword, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        registerPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnBackToLogin = new JButton();
        btnBackToLogin.setText("Nazad na Login");
        registerPanel.add(btnBackToLogin, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

  
    public JComponent $$$getRootComponent$$$() {
        return registerPanel;
    }

}

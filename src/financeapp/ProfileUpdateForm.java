package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import javax.swing.*;
import java.awt.*;

public class ProfileUpdateForm extends JFrame {
    private JTextField txtNewUser;
    private JPasswordField txtNewPass;
    private JComboBox<String> comboTheme;
    private JButton btnSave;

    public ProfileUpdateForm(String staroKorisnickoIme) {
        setTitle("Postavke Profila - Update");
        setSize(400, 300);
        setLayout(new GridLayout(4, 2, 10, 10));
        setLocationRelativeTo(null);

        add(new JLabel(" Novo korisničko ime:"));
        txtNewUser = new JTextField(staroKorisnickoIme);
        add(txtNewUser);

        add(new JLabel(" Nova lozinka:"));
        txtNewPass = new JPasswordField();
        add(txtNewPass);

        add(new JLabel(" Izaberi temu:"));
        comboTheme = new JComboBox<>(new String[]{"Standard", "Zelena", "Cyberpunk", "Tamna"});
        add(comboTheme);

        btnSave = new JButton("Spasi promjene");
        add(new JLabel("")); 
        add(btnSave);

        btnSave.addActionListener(e -> {
            String noviUser = txtNewUser.getText();
            String novaTema = (String) comboTheme.getSelectedItem();
            String novaLozinka = new String(txtNewPass.getPassword());

            try {
                MongoDatabase db = MongoDBConnection.getDatabase();
                MongoCollection<Document> collection = db.getCollection("users");

              
                collection.updateOne(
                        Filters.eq("username", staroKorisnickoIme),
                        new Document("$set", new Document("username", noviUser)
                                .append("theme", novaTema)
                                .append("password", novaLozinka))
                );

                JOptionPane.showMessageDialog(null, "Promjene su spašene!");


                new MainMenu(noviUser, novaTema);
                this.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Greška: " + ex.getMessage());
            }
        });
        
        setVisible(true);
    }
}

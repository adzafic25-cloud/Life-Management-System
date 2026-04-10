package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SleepTrackerForm extends JFrame {
    private JTextField txtSati, txtOpis, txtDatum;
    private JComboBox<Integer> comboOcjena;
    private JLabel lblTotalMonth;
    private JButton btnSave, btnUpdate;
    private String trenutniKorisnik;

    public SleepTrackerForm(String username) {
        this.trenutniKorisnik = username;
        setTitle("Sleep Tracker - " + username);
        setSize(450, 550);
        setLayout(new GridLayout(11, 1, 5, 5));
        setLocationRelativeTo(null);


        add(new JLabel(" Datum (YYYY-MM-DD):"));
        txtDatum = new JTextField(LocalDate.now().toString());
        add(txtDatum);


        add(new JLabel(" Koliko sati ste spavali? (npr. 7.5):"));
        txtSati = new JTextField();
        add(txtSati);


        add(new JLabel(" Ocjena sna (1-10):"));
        Integer[] ocjene = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        comboOcjena = new JComboBox<>(ocjene);
        comboOcjena.setSelectedItem(7); 
        add(comboOcjena);


        add(new JLabel(" Kratak opis/bilješka:"));
        txtOpis = new JTextField();
        add(txtOpis);


        btnSave = new JButton("Spasi novi unos");
        btnUpdate = new JButton("Ažuriraj ocjenu za uneseni datum");
        add(btnSave);
        add(btnUpdate);


        lblTotalMonth = new JLabel("Ukupno sati ovaj mjesec: Računam...");
        lblTotalMonth.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalMonth.setForeground(new Color(0, 102, 204));
        add(lblTotalMonth);


        btnSave.addActionListener(e -> saveSleepData());
        btnUpdate.addActionListener(e -> updateSleepData());


        calculateMonthlyTotal();

        setVisible(true);
    }

    private void saveSleepData() {
        try {
            double sati = Double.parseDouble(txtSati.getText());
            String datum = txtDatum.getText();

            MongoDatabase db = MongoDBConnection.getDatabase();
            MongoCollection<Document> collection = db.getCollection("sleep_logs");

            Document doc = new Document("username", trenutniKorisnik)
                    .append("datum", datum)
                    .append("satiSna", sati)
                    .append("ocjena", comboOcjena.getSelectedItem())
                    .append("opis", txtOpis.getText());

            collection.insertOne(doc);
            JOptionPane.showMessageDialog(this, "Uspješno spašeno!");
            calculateMonthlyTotal();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Greška: Provjerite unos sati!");
        }
    }

    private void updateSleepData() {
        try {
            String datum = txtDatum.getText();
            int novaOcjena = (int) comboOcjena.getSelectedItem();

            MongoDatabase db = MongoDBConnection.getDatabase();
            MongoCollection<Document> collection = db.getCollection("sleep_logs");


            collection.updateOne(
                    Filters.and(Filters.eq("username", trenutniKorisnik), Filters.eq("datum", datum)),
                    new Document("$set", new Document("ocjena", novaOcjena))
            );

            JOptionPane.showMessageDialog(this, "Ocjena ažurirana za datum " + datum);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Greška pri ažuriranju!");
        }
    }

    private void calculateMonthlyTotal() {
        try {
            MongoDatabase db = MongoDBConnection.getDatabase();
            MongoCollection<Document> collection = db.getCollection("sleep_logs");


            String trenutniMjesecPrefix = LocalDate.now().toString().substring(0, 7);

            double ukupno = 0;

            for (Document doc : collection.find(Filters.eq("username", trenutniKorisnik))) {
                String datumLoga = doc.getString("datum");


                if (datumLoga != null && datumLoga.startsWith(trenutniMjesecPrefix)) {
                    Object satiObj = doc.get("satiSna");
                    if (satiObj instanceof Number) {
                        ukupno += ((Number) satiObj).doubleValue();
                    }
                }
            }

            lblTotalMonth.setText("Ukupno sati ovaj mjesec (" + trenutniMjesecPrefix + "): " + ukupno + "h");

        } catch (Exception ex) {
            lblTotalMonth.setText("Greška pri računanju statistike.");
            ex.printStackTrace();
        }
    } 
} 

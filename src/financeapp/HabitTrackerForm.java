package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class HabitTrackerForm extends JFrame {
    private JProgressBar waterBar;
    private JLabel lblStatus, lblDatum;
    private JButton btnPlus, btnMinus, btnSave;
    private int trenutniBrojCasa = 0;
    private final int CILJ = 10;
    private String trenutniKorisnik;
    private String danasnjiDatum = LocalDate.now().toString();

    public HabitTrackerForm(String username) {
        this.trenutniKorisnik = username;
        setTitle("Habit Tracker - Water Intake");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));


        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        lblDatum = new JLabel("Datum: " + danasnjiDatum, SwingConstants.CENTER);
        lblDatum.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel title = new JLabel("Dnevni cilj: 10 čaša vode", SwingConstants.CENTER);
        headerPanel.add(lblDatum);
        headerPanel.add(title);
        add(headerPanel, BorderLayout.NORTH);


        JPanel centerPanel = new JPanel(new GridBagLayout());
        waterBar = new JProgressBar(JProgressBar.VERTICAL, 0, CILJ);
        waterBar.setPreferredSize(new Dimension(80, 250));
        waterBar.setStringPainted(true); 
        waterBar.setForeground(new Color(51, 153, 255)); 

        lblStatus = new JLabel("Popijeno: 0 / 10", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(10,0,10,0);
        centerPanel.add(waterBar, gbc);
        gbc.gridy = 1;
        centerPanel.add(lblStatus, gbc);

        add(centerPanel, BorderLayout.CENTER);


        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnMinus = new JButton(" - ");
        btnPlus = new JButton(" + ");
        btnSave = new JButton("Spasi u bazu");


        btnMinus.setFont(new Font("Arial", Font.BOLD, 25));
        btnPlus.setFont(new Font("Arial", Font.BOLD, 25));

        controlPanel.add(btnMinus);
        controlPanel.add(btnPlus);
        controlPanel.add(btnSave);
        add(controlPanel, BorderLayout.SOUTH);


        btnPlus.addActionListener(e -> updateWater(1));
        btnMinus.addActionListener(e -> updateWater(-1));
        btnSave.addActionListener(e -> saveToDatabase());

        loadTodayData(); 
        setVisible(true);
    }

    private void updateWater(int promjena) {
        int noviBroj = trenutniBrojCasa + promjena;


        if (noviBroj >= 0 && noviBroj <= 15) {
            trenutniBrojCasa = noviBroj;
            waterBar.setValue(trenutniBrojCasa);
            lblStatus.setText("Popijeno: " + trenutniBrojCasa + " / " + CILJ);


            if (trenutniBrojCasa >= CILJ) {
                waterBar.setForeground(Color.GREEN);
                lblStatus.setText("Cilj ostvaren! (" + trenutniBrojCasa + ")");
            } else {
                waterBar.setForeground(new Color(51, 153, 255));
            }
        }
    }

    private void saveToDatabase() {
        try {
            MongoDatabase db = MongoDBConnection.getDatabase();
            MongoCollection<Document> collection = db.getCollection("habit_water");


            Document query = new Document("username", trenutniKorisnik).append("datum", danasnjiDatum);
            Document update = new Document("$set", new Document("brojCasa", trenutniBrojCasa));

            collection.updateOne(query, update, new com.mongodb.client.model.UpdateOptions().upsert(true));

            JOptionPane.showMessageDialog(this, "Progres spašen!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Greška sa bazom: " + ex.getMessage());
        }
    }

    private void loadTodayData() {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("habit_water");
            Document doc = collection.find(Filters.and(
                    Filters.eq("username", trenutniKorisnik),
                    Filters.eq("datum", danasnjiDatum)
            )).first();

            if (doc != null) {
                trenutniBrojCasa = doc.getInteger("brojCasa");
                waterBar.setValue(trenutniBrojCasa);
                lblStatus.setText("Popijeno: " + trenutniBrojCasa + " / " + CILJ);
                if (trenutniBrojCasa >= CILJ) waterBar.setForeground(Color.GREEN);
            }
        } catch (Exception e) {
            System.out.println("Nema današnjih podataka.");
        }
    }
}

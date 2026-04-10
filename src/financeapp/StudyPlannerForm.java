package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StudyPlannerForm extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtPredmet, txtCilj, txtOstvareno;
    private JSpinner dateSpinner;
    private String trenutniKorisnik;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public StudyPlannerForm(String username) {
        this.trenutniKorisnik = username;
        setTitle("Study Planner - " + username);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));


        JPanel inputPanel = new JPanel(new GridLayout(2, 5, 5, 5));


        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        txtPredmet = new JTextField();
        txtCilj = new JTextField();
        txtOstvareno = new JTextField();
        JButton btnAdd = new JButton("Dodaj Plan");

        inputPanel.add(new JLabel("Datum:"));
        inputPanel.add(new JLabel("Predmet:"));
        inputPanel.add(new JLabel("Cilj (h):"));
        inputPanel.add(new JLabel("Učeno (h):"));
        inputPanel.add(new JLabel("")); 

        inputPanel.add(dateSpinner);
        inputPanel.add(txtPredmet);
        inputPanel.add(txtCilj);
        inputPanel.add(txtOstvareno);
        inputPanel.add(btnAdd);

        add(inputPanel, BorderLayout.NORTH);


        String[] kolone = {"ID", "Datum", "Predmet", "Cilj (h)", "Učeno (h)"};
        tableModel = new DefaultTableModel(kolone, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);


        JPanel controlPanel = new JPanel();
        JButton btnDelete = new JButton("Obriši označeno");
        JButton btnRefresh = new JButton("Prikaži za datum");
        controlPanel.add(btnRefresh);
        controlPanel.add(btnDelete);
        add(controlPanel, BorderLayout.SOUTH);


        btnAdd.addActionListener(e -> saveStudyPlan());
        btnRefresh.addActionListener(e -> loadStudyPlans());
        btnDelete.addActionListener(e -> deleteStudyPlan());

        loadStudyPlans(); 
        setVisible(true);
    }

    private void saveStudyPlan() {
        try {
            String datum = sdf.format((Date) dateSpinner.getValue());
            Document doc = new Document("username", trenutniKorisnik)
                    .append("datum", datum)
                    .append("predmet", txtPredmet.getText())
                    .append("cilj", Double.parseDouble(txtCilj.getText()))
                    .append("ostvareno", Double.parseDouble(txtOstvareno.getText()));

            MongoDBConnection.getDatabase().getCollection("study_plans").insertOne(doc);
            JOptionPane.showMessageDialog(this, "Plan spašen!");
            loadStudyPlans();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Greška: Provjerite unos brojeva!");
        }
    }

    private void loadStudyPlans() {
        tableModel.setRowCount(0); 
        MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("study_plans");

        for (Document doc : collection.find(Filters.eq("username", trenutniKorisnik))) {
            tableModel.addRow(new Object[]{
                    doc.getObjectId("_id"),
                    doc.getString("datum"),
                    doc.getString("predmet"),
                    doc.get("cilj"),
                    doc.get("ostvareno")
            });
        }
    }

    private void deleteStudyPlan() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Object id = tableModel.getValueAt(row, 0);
            MongoDBConnection.getDatabase().getCollection("study_plans").deleteOne(Filters.eq("_id", id));
            loadStudyPlans();
            JOptionPane.showMessageDialog(this, "Obrisano!");
        } else {
            JOptionPane.showMessageDialog(this, "Označite red u tabeli!");
        }
    }
}

package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

public class TransactionManager {

    private final MongoCollection<Document> collection;

    public TransactionManager() {
        MongoDatabase db = MongoDBConnection.getDatabase();

        if (db == null) {
            throw new IllegalStateException("Greška: Konekcija sa bazom nije uspostavljena!");
        }

        this.collection = db.getCollection("transactions");
    }

    public void addTransaction(Transaction t) {
        if (collection != null) {
            collection.insertOne(t.toDocument());
        }
    }

    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> list = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document d = cursor.next();

                double amountValue;
                Object amountObj = d.get("amount");

                if (amountObj instanceof Integer) {
                    amountValue = ((Integer) amountObj).doubleValue();
                } else if (amountObj instanceof Double) {
                    amountValue = (Double) amountObj;
                } else {
                    amountValue = 0.0;
                }

                list.add(new Transaction(
                        d.getString("type"),
                        amountValue,
                        d.getString("description")
                ));
            }
        } catch (Exception e) {
            System.err.println("Greška pri čitanju: " + e.getMessage());
        }
        return list;
    }

    public void deleteTransaction(String description) {
        if (collection != null) {
            collection.deleteOne(new Document("description", description));
        }
    }


    public void updateTransaction(String oldDescription, Transaction newTransaction) {

        collection.replaceOne(new org.bson.Document("description", oldDescription), newTransaction.toDocument());
    }
    public void deleteTransactionByValues(String type, double amount) {

        collection.deleteOne(new org.bson.Document("type", type).append("amount", amount));
    }

    public void updateTransactionByValues(String oldType, double oldAmount, Transaction newT) {
        if (collection != null) {
            Document filter = new Document("type", oldType)
                    .append("amount", oldAmount);

            var result = collection.replaceOne(filter, newT.toDocument());

            System.out.println("--- LOG AŽURIRANJA ---");
            System.out.println("Tražim: " + oldType + " sa iznosom " + oldAmount);
            System.out.println("Pronađeno u bazi: " + result.getMatchedCount());
            System.out.println("Izmijenjeno: " + result.getModifiedCount());
            System.out.println("----------------------");
        }
    }

    public void deleteAllTransactions() {
        collection.deleteMany(new org.bson.Document());
    }
}
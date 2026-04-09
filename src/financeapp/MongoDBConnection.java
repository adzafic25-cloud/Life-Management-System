package financeapp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static final String URI = "mongodb://localhost:27017";

    private static final String DB_NAME = "financeTrackerDB";

    private static MongoClient client = null;

    public static MongoDatabase getDatabase() {
        try {
            if (client == null) {
                client = MongoClients.create(URI);
                System.out.println("Povezivanje na MongoDB uspješno inicijalizovano.");
            }

            MongoDatabase database = client.getDatabase(DB_NAME);

            if (database == null) {
                System.err.println("Greška: Baza sa imenom " + DB_NAME + " nije pronađena!");
                return null;
            }

            return database;

        } catch (Exception e) {
            System.err.println("Kritična greška pri povezivanju na MongoDB: " + e.getMessage());
            return null;
        }
    }

    public static void close() {
        if (client != null) {
            client.close();
            client = null;
        }
    }
}
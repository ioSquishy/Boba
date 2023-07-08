package boba;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;
import java.util.TreeMap;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class Data {
    private static final String connectionString = "mongodb+srv://squishydb:" + Dotenv.load().get("MONGO_PASS") + "@sandbox.ujgwpn6.mongodb.net/?retryWrites=true&w=majority";
    private static final MongoClient mongoClient = MongoClients.create(connectionString);
    private static final MongoDatabase mongoDatabase = mongoClient.getDatabase("dev");
    private static MongoCollection<Document> mongoUsers = mongoDatabase.getCollection("users");

    private static TreeMap<Long, Document> newUsers = new TreeMap<>();
    private static TreeMap<Long, Document> existingCachedUsers = new TreeMap<>();

    public static void test() {
        
    }

    public static void getUser() {

    }
    private static void findUser() {

    }
    private static void addUser() {
        
    }

    private static void syncCacheToDatabase() {
        mongoUsers.insertMany(null);
    }
    private static boolean insertNewUsers() {
        List<Document> newUsersDocs = List.copyOf(newUsers.values());
        boolean acknowledged = mongoUsers.insertMany(newUsersDocs).wasAcknowledged();
        if (acknowledged) {
            existingCachedUsers.putAll(newUsers);
        }
        return acknowledged;
    }
    private static void updateExistingUsers() {

    }
}
package boba;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;


public class Data implements Serializable {
    private static transient final String connectionString = "mongodb+srv://squishydb:" + Dotenv.load().get("MONGO_PASS") + "@sandbox.ujgwpn6.mongodb.net/?retryWrites=true&w=majority";
    private static transient MongoClient mongoClient;
    private static transient MongoDatabase mongoDatabase;
    private static transient MongoCollection<Document> mongoUsers;
    private static transient boolean mongoOK;

    private static transient TreeMap<Long, Document> newUsers = new TreeMap<Long, Document>();
    private static transient TreeMap<Long, Document> existingCachedUsers = new TreeMap<Long, Document>();

    private static TreeMap<Long, Document> failover = new TreeMap<Long, Document>();

    public static void initMongoDB() {
        try {
            mongoClient = MongoClients.create(connectionString);
            mongoDatabase = mongoClient.getDatabase("dev");
            mongoUsers = mongoDatabase.getCollection("users");
            mongoOK = true;
        } catch (Error e) {
            e.printStackTrace();
            mongoNotOK();
        }
    }

    private static void mongoNotOK() {
        mongoOK = false;
    }

    public static void test() {
        
    }

    public static Document getUser(Long userID) {
        Document doc = findUser(userID);
        return doc = (doc != null) ? doc : addUser(userID);
    }
    private static Document findUser(Long userID) {
        //check existing cache first then new cache then database
        Document doc = null;
        doc = existingCachedUsers.getOrDefault(userID, null);
        doc = (doc != null) ? doc : newUsers.getOrDefault(userID, null);
        return null;
    }
    private static Document addUser(Long userID) {
        return null;
    }

    private static void syncCacheToDatabase() {
        updateExistingCachedUsers();
        insertNewCachedUsers();
    }
    private static transient ReplaceOptions replaceOpts = new ReplaceOptions().upsert(true);
    private static void updateExistingCachedUsers() {
        TreeMap<Long, Document> existingCacheCopy = new TreeMap<>();
        existingCacheCopy.putAll(existingCachedUsers);
        for (Map.Entry<Long, Document> doc : existingCacheCopy.entrySet()) {
            Bson query = eq("_id", doc.getKey());
            try {
                mongoUsers.replaceOne(query, doc.getValue(), replaceOpts);
            } catch (Error e) {
                e.printStackTrace();
                mongoNotOK();
                failover.put(doc.getKey(), doc.getValue());
            }
        }
    }
    private static void insertNewCachedUsers() {
        TreeMap<Long, Document> newUserCopy = new TreeMap<>();
        newUserCopy.putAll(newUsers);
        existingCachedUsers.putAll(newUsers);
        newUsers.clear();
        List<Document> newUsersDocs = List.copyOf(newUserCopy.values());
        try {
            mongoUsers.insertMany(newUsersDocs);
        } catch (Error e) {
            e.printStackTrace();
            mongoNotOK();
        }
    }
}
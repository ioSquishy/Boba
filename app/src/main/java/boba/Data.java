package boba;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.Serializable;
import java.time.Instant;
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


public class Data implements Serializable {
    private static transient final String connectionString = "mongodb+srv://squishydb:" + Dotenv.load().get("MONGO_PASS") + "@sandbox.ujgwpn6.mongodb.net/?retryWrites=true&w=majority";
    private static transient MongoClient mongoClient;
    private static transient MongoDatabase mongoDatabase;
    private static transient MongoCollection<Document> mongoUsers;
    private static transient boolean mongoOK;

    private static transient TreeMap<Long, Document> newUsersCache = new TreeMap<Long, Document>();
    private static transient TreeMap<Long, Document> existingUsersCache = new TreeMap<Long, Document>();

    private static TreeMap<Long, Document> failover = new TreeMap<Long, Document>();

    public static void initMongoDB() {
        try {
            mongoClient = MongoClients.create(connectionString);
            mongoDatabase = mongoClient.getDatabase("dev");
            mongoUsers = mongoDatabase.getCollection("users");
            mongoOK = true;
            System.out.println("Database Connected");
        } catch (Error e) {
            e.printStackTrace();
            mongoNotOK();
        }
    }

    private static void mongoNotOK() {
        System.out.println("Mongo is NOT OK!!");
        mongoOK = false;
    }

    public static void updateAllUsers() {

    }

    public static void test() {
        System.out.println(getUserDoc(263049275196309506L).get("_id").toString());
        System.out.println("newUsersCache: " + newUsersCache.keySet().toString());
        System.out.println("existingCache: " + existingUsersCache.keySet().toString());

        syncCacheToDatabase();
        System.out.println("newUsersCache: " + newUsersCache.keySet().toString());
        System.out.println("existingCache: " + existingUsersCache.keySet().toString());

        System.out.println(getUserDoc(263049275196309506L).get("_id").toString());
        System.out.println("newUsersCache: " + newUsersCache.keySet().toString());
        System.out.println("existingCache: " + existingUsersCache.keySet().toString());
    }

    public static Document getUserDoc(Long userID) {
        Document doc = checkCache(userID);
        if (doc != null) {
            return doc;
        } else {
            doc = checkDatabase(userID);
        }
        if (doc != null) {
            addDocToCache(userID, doc, false);
            return doc;
        } else if (mongoOK) {
            doc = createNewDoc(userID);
            addDocToCache(userID, doc, true);
            return doc;
        } else {
            return null;
        }
    }
    private static boolean checkDocNull(Document doc) {
        return doc != null;
    }
    private static Document checkCache(Long userID) {
        //check existing cache first then new cache
        Document doc = null;
        doc = existingUsersCache.getOrDefault(userID, null);
        doc = (doc != null) ? doc : newUsersCache.getOrDefault(userID, null);
        System.out.println("cache check: " + checkDocNull(doc));
        return doc;
    }
    private static Document checkDatabase(Long userID) {
        Document doc = null;
        System.out.println("database check: " + checkDocNull(doc));
        return doc = mongoOK ? mongoUsers.find(eq("_id", userID)).first() : null;
    }
    private static Document createNewDoc(Long userID) {
        return new Document()
            .append("_id", userID)
            .append("lastCmdUse", Instant.now().getEpochSecond())
            .append("coins", 0);
    }
    private static void addDocToCache(Long userID, Document document, boolean newUser) {
        if (newUser) {
            newUsersCache.put(userID, document);
        } else {
            existingUsersCache.put(userID, document);
        }
    }

    private static void syncCacheToDatabase() {
        System.out.println("sync database");
    }
    private static transient ReplaceOptions replaceOpts = new ReplaceOptions().upsert(true);
    private static void updateExistingCachedUsers() {
        TreeMap<Long, Document> existingCacheCopy = new TreeMap<>();
        existingCacheCopy.putAll(existingUsersCache);
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
        newUserCopy.putAll(newUsersCache);
        existingUsersCache.putAll(newUsersCache);
        newUsersCache.clear();
        List<Document> newUsersDocs = List.copyOf(newUserCopy.values());
        try {
            mongoUsers.insertMany(newUsersDocs);
        } catch (Error e) {
            e.printStackTrace();
            mongoNotOK();
        }
    }
}
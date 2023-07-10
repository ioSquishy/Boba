package boba;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;


public class Data implements Serializable {
    private static transient final String connectionString = "mongodb+srv://squishydb:" + Dotenv.load().get("MONGO_PASS") + "@sandbox.ujgwpn6.mongodb.net/?retryWrites=true&w=majority";
    private static transient MongoClient mongoClient;
    private static transient MongoDatabase mongoDatabase;
    private static transient MongoCollection<Document> mongoUsers;
    private static transient boolean mongoOK;

    private static HashMap<Long, Document> userCache = new HashMap<Long, Document>();

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
    
    public static void test() {
        System.out.println(getUserDoc(263049275196309506L).get("coins").toString());
        System.out.println("cache: " + userCache.keySet().toString());

        testIterate(getUserDoc(263049275196309506L));

        //syncCacheToDatabase();
    }
    public static void testUpdate(Document doc) {
        doc.put("coins", 3);
    }
    public static void testIterate(Document doc) {
        Document menu = doc.getEmbedded(List.of("menu", "boba"), Document.class);
        for (String key : menu.keySet()) {
            String value = menu.getString(key);
            System.out.println("Key: " + key + ", Value: " + value);
        }
    }

    public static Document getUserDoc(Long userID) {
        Document doc = checkCache(userID);
        System.out.println("cache check: " + checkDocNull(doc));
        if (doc != null) {
            return doc;
        } else {
            doc = checkDatabase(userID);
        }
        if (doc != null) {
            addDocToCache(userID, doc);
            return doc;
        } else if (mongoOK) {
            doc = createNewDoc(userID);
            addDocToCache(userID, doc);
            return doc;
        } else {
            return null;
        }
    }
    private static boolean checkDocNull(Document doc) {
        return doc != null;
    }
    private static Document checkCache(Long userID) {
        return userCache.get(userID);
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
    private static void addDocToCache(Long userID, Document document) {
        userCache.put(userID, document);
    }

    private static transient final ReplaceOptions replaceOpts = new ReplaceOptions().upsert(true);
    private static transient final BulkWriteOptions bulkOpts = new BulkWriteOptions().ordered(false);
    private static void syncCacheToDatabase() {
        HashMap<Long, Document> userCacheCopy = new HashMap<Long, Document>(userCache);
        ArrayList<ReplaceOneModel<Document>> writeReqs = new ArrayList<ReplaceOneModel<Document>>(userCacheCopy.size());
        for (Map.Entry<Long, Document> entry : userCacheCopy.entrySet()) {
            writeReqs.add(new ReplaceOneModel<Document>(eq("_id", entry.getKey()), entry.getValue(), replaceOpts));
        }
        try {
            mongoUsers.bulkWrite(writeReqs, bulkOpts);
        } catch (Error e) {
            e.printStackTrace();
            mongoNotOK();
        }
        System.out.println("cached synced");
    }
}
package iosquishy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

public class Player {
    //coins
    public static int getCoins(long userID) {
        Document doc = Data.getUserDoc(userID);
        // int coins = doc.get("coins", 0);
        // int coinsPerMinute = doc.get("coinsPerMinute", 0);
        // long currentMinute = Instant.now().getEpochSecond()/60;
        // long lastCoinUpdate = doc.get("lastCoinUpdate", currentMinute);
        // doc.put("lastCoinUpdate", currentMinute);
        // long timeElapsedFromLastCoinUpdate = currentMinute-lastCoinUpdate;
        // int bonusMultiplier = doc.get("coinBonusMultiplier", 1);
        // if (bonusMultiplier > 1) {
        //     long bonusEndMinute = doc.get("coinBonusEnd", 0L);
        //     long bonusTimeLeft = bonusEndMinute - currentMinute;
        //     if (bonusTimeLeft >= 0) { //still time left before bonus ends
        //         coins += timeElapsedFromLastCoinUpdate*(coinsPerMinute*bonusMultiplier);
        //         doc.put("coins", coins);
        //         return coins;
        //     } else { //bonus has ended
        //         long bonusTimeElapsed = bonusEndMinute-lastCoinUpdate;
        //         long remainingCoinsToAdd = Math.abs(bonusTimeLeft)*coinsPerMinute;
        //         coins += (bonusTimeElapsed*(coinsPerMinute*bonusMultiplier)) + remainingCoinsToAdd;
        //         doc.put("coinBonusMultiplier", 1);
        //         doc.put("coins", coins);
        //         return coins;
        //     }
        // } else {
        //     coins += timeElapsedFromLastCoinUpdate*coinsPerMinute;
        //     doc.put("coins", coins);
        //     return coins;
        // }
        
        return doc.get("coinBonusMultiplier", 1) > 1 
            ? (doc.get("coinBonusEnd", 0)-(Instant.now().getEpochSecond()/60)) >= 0 
            ? updateCoinsAndMultiplier(doc, (int) (doc.get("coins", 0)+(((Instant.now().getEpochSecond()/60)-doc.get("lastCoinUpdate", Instant.now().getEpochSecond()/60))*(doc.get("coinsPerMinute", 0)*doc.get("coinBonusMultiplier", 1)))), false)
            : updateCoinsAndMultiplier(doc, (int) (doc.get("coins", 0)+((doc.get("coinBonusEnd", 0L)-doc.get("lastCoinUpdate", Instant.now().getEpochSecond()/60))*(doc.get("coinsPerMinute", 0)*doc.get("coinBonusMultiplier", 1)))+(Math.abs(doc.get("coinBonusEnd", 0L)-Instant.now().getEpochSecond()/60)*doc.get("coinsPerMinute", 0))), true)
            : updateCoinsAndMultiplier(doc, (int) (doc.get("coins", 0)+(Instant.now().getEpochSecond()/60-doc.get("lastCoinUpdate", Instant.now().getEpochSecond()/60))*doc.get("coinsPerMinute", 0)), false);
    }
    //helper method
    private static int updateCoinsAndMultiplier(Document doc, int coins, boolean resetBonusMultiplier) {
        doc.put("lastCoinUpdate", Instant.now().getEpochSecond()/60);
        doc.put("coins", coins);
        if (resetBonusMultiplier) {
            doc.put("coinBonusMultiplier", 1);
        }
        return coins;
    }
    /**
     * 
     * @param userID
     * @param coins can accept negative numbers for removing coins
     */
    public static void addCoins(long userID, int coins) {
        Document doc = Data.getUserDoc(userID);
        int currentCoins = (int) doc.get("coins");
        doc.put("coins", currentCoins+coins);
    }
    public static void setCoinsPerMinute(long userID, int coinsPerMinute) {
        Document doc = Data.getUserDoc(userID);
        doc.put("coinsPerMinute", coinsPerMinute);
    }
    /**
     * 
     * @param userID
     * @param multiplier gets multiplied against the usual coinsPerMinute value
     * @param minutes sets how long the multiplier will last in minutes
     */
    public static void setCoinBonusMultiplier(long userID, int multiplier, short minutes) {
        Document doc = Data.getUserDoc(userID);
        doc.put("coinBonusMultiplier", multiplier);
        doc.put("coinBonusEnd", (Instant.now().getEpochSecond()/60) + minutes);
    }

    //bobas
    /**
     * 
     * @param userID
     * @return String[a][b]
     * <p>a: 0 = boba names
     * <p>a: 1 = boba image urls
     * <p>a: 2 = boba elements
     * 
     * <p>b iterates through the bobas themselves
     */
    public static String[][] getBobas(long userID) {
        Document doc = Data.getUserDoc(userID);
        return getBobaArray(doc);
    }
    public static void reorderBobas(long userID, byte[] reorderedOldIndicies) {
        Document doc = Data.getUserDoc(userID);
        String[][] ogBobas = getBobaArray(doc);
        byte totalBobas = (byte) ogBobas[0].length;
        String[] newBobaNames = new String[totalBobas];
        String[] newBobaImages = new String[totalBobas];
        String[] newBobaElements = new String[totalBobas];
        for (byte newIndex = 0; newIndex < totalBobas; newIndex++) {
            newBobaNames[newIndex] = ogBobas[0][reorderedOldIndicies[newIndex]];
            newBobaImages[newIndex] = ogBobas[1][reorderedOldIndicies[newIndex]];
            newBobaElements[newIndex] = ogBobas[2][reorderedOldIndicies[newIndex]];
        }
        doc.put("bobaNames", Arrays.asList(newBobaNames));
        doc.put("bobaImages", Arrays.asList(newBobaImages));
        doc.put("bobaElements", Arrays.asList(newBobaElements));
    }
    public static void addBoba(long userID, String bobaName, String bobaImage, String bobaElements) {
        Document doc = Data.getUserDoc(userID);
        List<List<String>> ogBobas = getBobaList(doc);
        ogBobas.get(0).add(bobaName);
        ogBobas.get(1).add(bobaImage);
        ogBobas.get(2).add(bobaElements);
        updateBobasViaList(doc, ogBobas);
    }
    public static void removeBoba(long userID, byte indexOfBoba) {
        Document doc = Data.getUserDoc(userID);
        List<List<String>> ogBobas = getBobaList(doc);
        ogBobas.get(0).remove(indexOfBoba);
        ogBobas.get(1).remove(indexOfBoba);
        ogBobas.get(2).remove(indexOfBoba);
        updateBobasViaList(doc, ogBobas);
    }
    public static void renameBoba(long userID, byte indexOfBoba, String newName) {
        Document doc = Data.getUserDoc(userID);
        String[] bobaNames = (String[]) doc.get("bobaNames");
        bobaNames[indexOfBoba] = newName;
        doc.put("bobaNames", bobaNames);
    }
    //boba helper methods
    private static List<List<String>> getBobaList(Document doc) {
        List<List<String>> boba =  new ArrayList<>();
        boba.add(doc.get("bobaNames", new ArrayList<String>()));
        boba.add(doc.get("bobaImages", new ArrayList<String>()));
        boba.add(doc.get("bobaElements", new ArrayList<String>()));
        return boba;
    }
    private static String[][] getBobaArray(Document doc) {
        String[] bobaNames = (String[]) doc.get("bobaNames");
        String[] bobaImages = (String[]) doc.get("bobaImages");
        String[] bobaElements = (String[]) doc.get("bobaElements");
        return new String[][] {bobaNames, bobaImages, bobaElements};
    }
    private static void updateBobasViaList(Document doc, List<List<String>> bobas) {
        doc.put("bobaNames", bobas.get(0));
        doc.put("bobaImages", bobas.get(1));
        doc.put("bobaElements", bobas.get(2));
    }

    //menu theme
    public static String getMenuTheme(long userID) {
        return (String) Data.getUserDoc(userID).get("menuTheme");
    }
    public static void setMenuTheme(long userID, String menuTheme) {
        Data.getUserDoc(userID).put("menuTheme", menuTheme);
    }
}

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
        long lastCoinUpdate = (long) doc.get("lastCoinUpdate");
        return (int) Data.getUserDoc(userID).get("coins");
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
        updateBobas(doc, ogBobas);
    }
    public static void removeBoba(long userID, byte indexOfBoba) {
        Document doc = Data.getUserDoc(userID);
        List<List<String>> ogBobas = getBobaList(doc);
        ogBobas.get(0).remove(indexOfBoba);
        ogBobas.get(1).remove(indexOfBoba);
        ogBobas.get(2).remove(indexOfBoba);
        updateBobas(doc, ogBobas);
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
        boba.add((List<String>) doc.get("bobaNames"));
        boba.add((List<String>) doc.get("bobaImages"));
        boba.add((List<String>) doc.get("bobaElements"));
        return boba;
    }
    private static String[][] getBobaArray(Document doc) {
        String[] bobaNames = (String[]) doc.get("bobaNames");
        String[] bobaImages = (String[]) doc.get("bobaImages");
        String[] bobaElements = (String[]) doc.get("bobaElements");
        return new String[][] {bobaNames, bobaImages, bobaElements};
    }
    private static void updateBobas(Document doc, List<List<String>> bobas) {
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

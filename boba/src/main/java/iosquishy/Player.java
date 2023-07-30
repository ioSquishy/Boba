package iosquishy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bson.Document;

public class Player {
    //coins
    public static int getCoins(long userID) {
        return (int) Data.getUserDoc(userID).get("coins");
    }
    public static void addCoins(long userID, short coins) {
        Document doc = Data.getUserDoc(userID);
        int currentCoins = (int) doc.get("coins");
        doc.put("coins", currentCoins+coins);
    }
    public static void removeCoins(long userID, short coins) {

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
    public static List<List<String>> getBobas(long userID) {
        Document doc = Data.getUserDoc(userID);
        List<List<String>> boba =  new ArrayList<>();
        boba.add((List<String>) doc.get("bobaNames"));
        boba.add((List<String>) doc.get("bobaImages"));
        boba.add((List<String>) doc.get("bobaElements"));
        return boba;
    }
    public static HashMap<String, List<String>> getBobas2(long userID) {
        Document doc = Data.getUserDoc(userID);
        HashMap<String, List<String>> boba = new HashMap<>();
        boba.put("bobaNames", (List<String>) doc.get("bobaNames"));
        boba.put("bobaImages", (List<String>) doc.get("bobaImages"));
        boba.put("bobaElements", (List<String>) doc.get("bobaElements"));
        System.out.println(boba);
        return boba;
    }
    public static void setBobas(long userID, List<List<String>> bobas) {

    }
    public static void addBoba(long userID, String bobaName, String bobaImage, String bobaElements) {
        Document doc = Data.getUserDoc(userID);
        List<List<String>> ogBobas = getBobas(doc);
        ogBobas.get(0).add(bobaName);
        ogBobas.get(1).add(bobaImage);
        ogBobas.get(2).add(bobaElements);
        doc.put("bobaNames", ogBobas.get(0));
        doc.put("bobaImages", ogBobas.get(1));
        doc.put("bobaElements", ogBobas.get(2));
    }
    public static void addBoba2(long userID, String bobaName, String bobaImage, String bobaElements) {
        Document doc = Data.getUserDoc(userID);
        HashMap<String, List<String>> ogBobas = new HashMap<>();
        ogBobas.get("bobaNames").add(bobaName);
        ogBobas.get("bobaImages").add(bobaName);
        ogBobas.get("bobaElements").add(bobaName);
        doc.putAll(ogBobas);
    }
    public static void removeBoba(long userID, int index) {

    }
    //boba helper methods
    private static List<List<String>> getBobas(Document doc) {
        List<List<String>> boba =  new ArrayList<>();
        boba.add((List<String>) doc.get("bobaNames"));
        boba.add((List<String>) doc.get("bobaImages"));
        boba.add((List<String>) doc.get("bobaElements"));
        return boba;
    }
    private static HashMap<String, List<String>> getBobas2(Document doc) {
        HashMap<String, List<String>> boba = new HashMap<>();
        boba.put("bobaNames", (List<String>) doc.get("bobaNames"));
        boba.put("bobaNames", (List<String>) doc.get("bobaImages"));
        boba.put("bobaNames", (List<String>) doc.get("bobaElements"));
        return boba;
    }

    //menu theme
    public static String getMenuTheme(long userID) {
        return null;
    }
    public static void setMenuTheme(long userID, String menuTheme) {
        
    }
}

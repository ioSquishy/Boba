package iosquishy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.awt.image.BufferedImage;

import org.bson.Document;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageBuilder;

import iosquishy.ImageGen.BobaGod;
import iosquishy.ImageGen.ImgEditor;

public class Player {//if ur reading this am going to the philippines :P
    //coins
    public static int getCoins(long userID) {
        Document doc = Data.getUserDoc(userID);
        int coins = doc.get("coins", 0);
        int coinsPerMinute = doc.get("coinsPerMinute", 0);
        long currentMinute = Instant.now().getEpochSecond()/60;
        long lastCoinUpdate = doc.get("lastCoinUpdate", currentMinute);
        doc.put("lastCoinUpdate", currentMinute);
        long timeElapsedFromLastCoinUpdate = currentMinute-lastCoinUpdate;
        int bonusMultiplier = doc.get("coinBonusMultiplier", 1);
        if (bonusMultiplier > 1) { //calculate addition of bonus if they have/had a bonus
            long bonusEndMinute = doc.get("coinBonusEnd", 0L);
            long bonusTimeLeft = bonusEndMinute - currentMinute;
            if (bonusTimeLeft >= 0) { //if still time left before bonus ends
                coins += timeElapsedFromLastCoinUpdate*(coinsPerMinute*bonusMultiplier);
                doc.put("coins", coins);
                return coins;
            } else { //bonus has ended so calculate how much of the bonus coins to give and add the remaining at normal
                long bonusTimeElapsed = bonusEndMinute-lastCoinUpdate;
                long remainingCoinsToAdd = Math.abs(bonusTimeLeft)*coinsPerMinute;
                coins += (bonusTimeElapsed*(coinsPerMinute*bonusMultiplier)) + remainingCoinsToAdd;
                doc.put("coinBonusMultiplier", 1);
                doc.put("coins", coins);
                return coins;
            }
        } else {
            coins += timeElapsedFromLastCoinUpdate*coinsPerMinute;
            doc.put("coins", coins);
            return coins;
        }
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
     * @param textChannel gets sent new images if current urls dont work
     * @return String[a][b]
     * <p>a: 0 = boba names
     * <p>a: 1 = boba image urls
     * <p>a: 2 = boba elements
     * 
     * <p>b iterates through the bobas themselves
     */
    public static String[][] getBobas(long userID, TextChannel textChannel) {
        Document doc = Data.getUserDoc(userID);
        String[][] bobaNamesAndImages = getBobaNamesAndImages(doc);
        if (ImgEditor.getImageFromURL(bobaNamesAndImages[1][0]) != null) {
            return bobaNamesAndImages;
        } else {
            return recompileBobas(doc, textChannel);
        }
    }
    public static void reorderBobas(long userID, byte[] reorderedOldIndicies) {
        Document doc = Data.getUserDoc(userID);
        String[][] ogBobas = getFullBobaArray(doc);
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
        List<List<String>> ogBobas = getBobaFullList(doc);
        ogBobas.get(0).add(bobaName);
        ogBobas.get(1).add(bobaImage);
        ogBobas.get(2).add(bobaElements);
        updateBobasViaList(doc, ogBobas);
    }
    public static void removeBoba(long userID, byte indexOfBoba) {
        Document doc = Data.getUserDoc(userID);
        List<List<String>> ogBobas = getBobaFullList(doc);
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
    private static List<List<String>> getBobaFullList(Document doc) {
        List<List<String>> boba =  new ArrayList<>();
        boba.add(doc.get("bobaNames", new ArrayList<String>()));
        boba.add(doc.get("bobaImages", new ArrayList<String>()));
        boba.add(doc.get("bobaElements", new ArrayList<String>()));
        return boba;
    }
    private static String[][] getFullBobaArray(Document doc) {
        String[] bobaNames = (String[]) doc.get("bobaNames");
        String[] bobaImages = (String[]) doc.get("bobaImages");
        String[] bobaElements = (String[]) doc.get("bobaElements");
        return new String[][] {bobaNames, bobaImages, bobaElements};
    }
    private static String[][] getBobaNamesAndImages(Document doc) {
        String[] bobaNames = (String[]) doc.get("bobaNames");
        String[] bobaImages = (String[]) doc.get("bobaImages");
        return new String[][] {bobaNames, bobaImages};
    }
    private static void updateBobasViaList(Document doc, List<List<String>> bobas) {
        doc.put("bobaNames", bobas.get(0));
        doc.put("bobaImages", bobas.get(1));
        doc.put("bobaElements", bobas.get(2));
    }
    private static void updateBobasViaArray(Document doc, String[][] bobas) {
        doc.put("bobaNames", bobas[0]);
        doc.put("bobaImages", bobas[1]);
        doc.put("bobaElements", bobas[2]);
    }
    /**
     * 
     * @param bobaImagesAndElements [0] is images<br>[1] is elements
     * @param channel to send message of new images to
     * @return String[][] of boba names and images
     */
    private static String[][] recompileBobas(Document doc, TextChannel channel) {
        String[][] fullBobaArray = getFullBobaArray(doc);
        MessageBuilder msgBuilder1 = new MessageBuilder();
        MessageBuilder msgBuilder2 = new MessageBuilder();
        msgBuilder1.setContent("Saiko stores your boba images using Discord CDNS so if you delete these messages, it will take longer to view your bobas!");
        for (int bobaImage = 0; bobaImage < fullBobaArray[2].length; bobaImage++) {
            BufferedImage newBobaImg = BobaGod.recompileBoba(fullBobaArray[2][bobaImage]);
            msgBuilder1.addAttachment(newBobaImg, bobaImage+"");
            if (bobaImage > 6) {
                msgBuilder2.addAttachment(newBobaImg, bobaImage+"");
            }
        }
        try {
            List<MessageAttachment> msgAttachments = msgBuilder1.send(channel).get().getAttachments();
            msgAttachments.addAll(msgBuilder2.send(channel).get().getAttachments());
            for (int bobaImg = 0; bobaImg < fullBobaArray[1].length; bobaImg++) {
                fullBobaArray[1][bobaImg] = msgAttachments.get(bobaImg).getUrl().toString();
            }
            updateBobasViaArray(doc, fullBobaArray);
            return new String[][] {fullBobaArray[0], fullBobaArray[1]};
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    //menu theme
    public static String getMenuTheme(long userID) {
        return (String) Data.getUserDoc(userID).get("menuTheme");
    }
    public static void setMenuTheme(long userID, String menuTheme) {
        Data.getUserDoc(userID).put("menuTheme", menuTheme);
    }
}

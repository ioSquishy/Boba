package iosquishy.ImageGen;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.entity.message.component.SelectMenuOptionBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class BobaGod {
    public static enum CupStyle {
        SEALED_CUP, CAPPED_CUP, JAR
    }
    public static enum Tea {
        MILK_TEA, GREEN_TEA, BLACK_TEA
    }
    public static enum Topping {
        PEARL, LYCHEE, JELLY, MANGO
    }
    private static enum CurrentlyEditing {
        CUP, TEA, TOPPINGS
    }
    private static HashMap<CupStyle, Image> cupImage = new HashMap<>(CupStyle.values().length);
    private static HashMap<CupStyle, Image> teaImage = new HashMap<>(CupStyle.values().length);
    private static HashMap<CupStyle, Short[][]> teaImageEdges = new HashMap<>(CupStyle.values().length);
    private static HashMap<Tea, Color> teaColor = new HashMap<>(Tea.values().length);
	private static HashMap<CupStyle, HashMap<Topping, Image>> toppingStyle = new HashMap<>(CupStyle.values().length);
    private static HashMap<Topping, Image> newToppings = new HashMap<>(CupStyle.values().length);
    private static HashMap<Topping, Image> sealed_cup_toppingImage = new HashMap<>(Topping.values().length);
    private static List<SelectMenuOption> cupOptions = Collections.emptyList();
    private static List<SelectMenuOption> teaOptions = Collections.emptyList();
    private static List<SelectMenuOption> toppingOptions = Collections.emptyList();
    public static void initBobaMaps() {
        /* Cup Styles */
        //SEALED_CUP:
        cupImage.put(CupStyle.SEALED_CUP, ImgEditor.getImageFromPath("boba\\src\\main\\assets\\CupStyles\\SEALED_CUP.png"));
        // cupOptions.add(new SelectMenuOptionBuilder().setLabel("Sealed Cup").setValue("SEALED_CUP").build());
        //CAPPED_CUP:

        //JAR:


        /* Tea Image */
        //SEALED_CUP
        teaImage.put(CupStyle.SEALED_CUP, ImgEditor.getImageFromPath("boba\\src\\main\\assets\\TeaShapes\\SEALED_CUP_TEA.png"));
        teaImageEdges.put(CupStyle.SEALED_CUP, getOpaqueEdges(ImgEditor.imageToBimage(teaImage.get(CupStyle.SEALED_CUP))));
        //CAPPED_CUP

        //JAR

        /* Tea Color */
        //MILK_TEA:
        teaColor.put(Tea.MILK_TEA, new Color(255, 209, 163));
        // teaOptions.add(new SelectMenuOptionBuilder().setLabel("Milk Tea").setValue("MILK_TEA").build());
        //GREEN_TEA:
        teaColor.put(Tea.GREEN_TEA, new Color(205, 225, 156));
        // teaOptions.add(new SelectMenuOptionBuilder().setLabel("Green Tea").setValue("GREEN_TEA").build());
        //BLACK_TEA:


        ///* Toppings */
		/* SEALED_CUP */
        //PEARL:
        sealed_cup_toppingImage.put(Topping.PEARL, ImgEditor.getImageFromPath("boba\\src\\main\\assets\\Toppings\\PEARL.png"));
        //LYCHEE:

        //JELLY:

        //MANGO:

		/* Topping Style */
		toppingStyle.put(CupStyle.SEALED_CUP, sealed_cup_toppingImage);

        // experimental:
        newToppings.put(Topping.PEARL, ImgEditor.getImageFromPath("boba\\src\\main\\assets\\Toppings\\SINGLE_PEARL.png"));
    }

    //Static stuff
    private static HashMap<Long, BobaGod> activeEditors = new HashMap<Long, BobaGod>();
    private static ScheduledExecutorService autoExe = Executors.newSingleThreadScheduledExecutor();
    private static Runnable removeInactiveEditors = () -> {
        HashMap<Long, BobaGod> activeEditorCopy = new HashMap<Long, BobaGod>(activeEditors);

        for (Map.Entry<Long, BobaGod> editor : activeEditorCopy.entrySet()) {
            if (Instant.now().getEpochSecond()-editor.getValue().getLastCmdUseSec() > (60*10)) { //10 minutes inactive
                activeEditors.remove(editor.getKey());
            }
        }
    };
    public static void startBobaGodCleaner() {
        autoExe.scheduleAtFixedRate(removeInactiveEditors, 30, 30, TimeUnit.MINUTES);
    }

    public static BobaGod getBobaGod(long userID) {
        return activeEditors.get(userID);
    }
    /**
     * Returns left and right edges of an opaque image on a transparent background.
     * @param img
     * @return
     * [0][x]: returns leftEdge
     * [1][x]: return rightEdge
     * [2][0]: lowest opaque point
     */
    private static Short[][] getOpaqueEdges(BufferedImage img) {
        Short[] leftEdges = new Short[512];
        Short[] rightEdges = new Short[512];
        // // gets the left/right borders between transparent and opaque pixels
        // int lossOfDetail = 8; //skips said amount of pixels to save time, must be >= 4 to be useful, otherwise should use binary type beat search | note: must be divisble by 512
        // for (short y = 0; y < 512; y++) {
        //     //left half
        //     for (short x = 256; x >= 0; x-=lossOfDetail) {
        //         if (img.getRGB(x, y)>>24 == 0x00) { //if pixel is transparent
        //             leftEdges[y] = x;
        //             break;
        //         }
        //     }
        //     //right half
        //     for (short x = 256; x < 512; x+=lossOfDetail) {
        //         if (img.getRGB(x, y)>>24 == 0x00) { //if pixel is transparent
        //             rightEdges[y] = x;
        //             break;
        //         }
        //     }
        // }
        boolean traversingOpaquePixels = false;
        short firstOpaquePixel = 0;
        short lastOpaquePixel = 0;
        byte distanceFromEdge = 16; // boba sprite is 32 pixels wide. so, the center should be at least 16 (half) pixels away from edge
        for (short y = 512-1; y >= 0; y--) {
            //left half
            boolean leftEdgeFound = false;
            boolean rightEdgeFound = false;
            short leftBound = 0;
            short rightBound = 256;
            while (!leftEdgeFound) {
                if (rightBound-leftBound <= 1) {
                    leftEdgeFound = true;
                }
                short xCheck = (short) (((rightBound-leftBound)/2) + leftBound);
                if (img.getRGB(xCheck, y)>>24 == 0x00) { //if pixel in middle of rB-lB is transparent
                    leftBound = xCheck;
                } else {
                    rightBound = xCheck;
                }
            }
            leftEdges[y] = rightBound-=distanceFromEdge;
            //right half
            leftBound = 256;
            rightBound = 512;
            while (!rightEdgeFound) {
                if (rightBound-leftBound <= 1) {
                    rightEdgeFound = true;
                }
                short xCheck = (short) (((rightBound-leftBound)/2) + leftBound);
                if (img.getRGB(xCheck, y)>>24 == 0x00) { //if pixel in middle of rB-lB is transparent
                    rightBound = xCheck;
                } else {
                    leftBound = xCheck;
                }
            }
            rightEdges[y] = leftBound+=distanceFromEdge;
            //checking if transparent pixels have been reached
            if (traversingOpaquePixels) {
                boolean leftTraversed = leftEdges[y] == 256;
                boolean rightTraversed = rightEdges[y] == 256;
                if (leftTraversed || rightTraversed) {
                    firstOpaquePixel = ++y;
                    break;
                }
            } else if (leftEdges[y] != 256 && rightEdges[y] != 256) {
                traversingOpaquePixels = true;
                lastOpaquePixel = ++y;
            }
        }
        Short[] opaqueLeftEdges = Arrays.copyOfRange(leftEdges, firstOpaquePixel, lastOpaquePixel);
        Short[] opaqueRightEdges = Arrays.copyOfRange(rightEdges, firstOpaquePixel, lastOpaquePixel);
        return new Short[][] {opaqueLeftEdges, opaqueRightEdges};
    }
    //Local Stuff
    private long lastCmdUseSec = Instant.now().getEpochSecond();

    private ImgEditor boba;
    private CupStyle cup = CupStyle.SEALED_CUP;
    private Tea tea = null;
    private ArrayList<Topping> toppings = new ArrayList<Topping>();
    private CurrentlyEditing currentlyEditing = CurrentlyEditing.CUP;

    public BobaGod(long userID) {
        this.boba = new ImgEditor(512, 512);
        activeEditors.put(userID, this);
    }

    //Set Methods
    public void setCupStyle(CupStyle cupStyle) {
        this.cup = cupStyle;
    }
    public void setTea(Tea tea) {
        this.tea = tea;
    }

    //Topping Methods
    public void addTopping(Topping topping) {
        if (!toppings.contains(topping)) {
            toppings.add(topping);
        }
    }
    public void removeTopping(Topping topping) {
        if (toppings.contains(topping)) {
            toppings.remove(topping);
        }
    }
    private static Random random = new Random();
    private static BufferedImage populateToppings(ArrayList<Topping> toppings, CupStyle cupStyle) {  //populate toppings progamatically
        ImgEditor populatedToppingsImage = new ImgEditor(512, 512);
        //get edges of teaBackground
        Short[] leftEdges = teaImageEdges.get(cupStyle)[0];
        Short[] rightEdges = teaImageEdges.get(cupStyle)[1];
        //create bounds for where toppings can spawn (only able to spawn in bottom third of tea)
        int validLowerBound = leftEdges.length-1;
        int validUpperBound = validLowerBound-(validLowerBound/3);
        System.out.println("vlb: " + validLowerBound);
        System.out.println("vub: " + validUpperBound);
        //split validBound into thirds
        int denominator = (validLowerBound-validUpperBound)/3; //denominator is used to find the ranges of the third that the validBounds will be split into
        System.out.println("den: " + denominator);
        int lowerThirdMin = validLowerBound;
        int lowerThirdMax = lowerThirdMin-denominator;
        System.out.println("low: " + lowerThirdMin + " - " + lowerThirdMax);
        int middleThirdMin = lowerThirdMax;
        int middleThirdMax = lowerThirdMax-denominator;
        System.out.println("mid: " + middleThirdMin + " - " + middleThirdMax);
        int upperThirdMin = middleThirdMax;
        int upperThirdMax = middleThirdMax-denominator;
        System.out.println("upp: " + upperThirdMin + "  - " + upperThirdMax);
        // generate template of spots to place boba sprites
        byte bobaSlotsToGen = (byte) (20 + random.nextInt(6)); //generates 20-25 bobaSlots
        ArrayList<Point> slots = new ArrayList<Point>(bobaSlotsToGen);
        for (byte slot = 0; slot < bobaSlotsToGen; slot++) {
            //pick a random y-val with higher chance of being higher number (lower on image)
            // choose a random bound with more weight to the bottom third
            byte r = (byte) random.nextInt(101);
            int upperBound;
            int lowerBound;
            if (r >= 80) { //upper third has 20% chance of spawning
                upperBound = upperThirdMax;
                lowerBound = upperThirdMin;
            } else if (r >= 70) { //middle third has 30% chance of spawning
                upperBound = middleThirdMax;
                lowerBound = middleThirdMin;
            } else { //bottom third has 50% chance of spawning
                upperBound = lowerThirdMax;
                lowerBound = lowerThirdMin;
            }
            //pick a random point in the range of the selected third 
            int potentialY = random.nextInt(upperBound, lowerBound);
            int potentialX = random.nextInt(leftEdges[potentialY], rightEdges[potentialY]);
            //add point to template
            slots.add(new Point(potentialX, potentialY));
        }
        //populate template with random toppings
        int slotsPerTopping = bobaSlotsToGen/toppings.size();
        for (Topping topping : toppings) {
            for (byte i = 0; i < slotsPerTopping; i++) {
                Point slot = slots.remove(0);
                populatedToppingsImage.setLayer(topping.toString() + i, newToppings.get(topping), (int) slot.getX(), (int) slot.getY());
            }
        }
        return populatedToppingsImage.getEditedImage();
    }

    //Return methods
    //drink -> toppings -> cup
    public BufferedImage getBobaImage() {
        if (this.cup!=null) {
            if (this.tea != null) {
                this.boba.setLayer("drink", ImgEditor.recolorImage(teaImage.get(cup), teaColor.get(tea)));
                if (!toppings.isEmpty()) {
                    this.boba.setLayer("Toppings", populateToppings(toppings, cup));
                    // for (Topping topping : toppings) { <- this is the old topping code
                    //     this.boba.setLayer(topping.toString(), sealed_cup_toppingImage.get(topping));
                    // }
                }
            }
            this.boba.setLayer("cup", cupImage.get(cup));
        }
        return boba.getEditedImage();
    }
    public CupStyle getCupStyle() {
        return cup;
    }
    public Tea getTeaType() {
        return tea;
    }
    public Topping[] getToppings() {
        return (Topping[]) toppings.toArray();
    }
    public String getBobaElements() {
        String toppingElements = "";
        for (int topping = 0; topping < toppings.size(); topping++) {
            toppingElements += toppings.get(topping).toString() + " ";
        }
        return tea.toString() + " " + toppingElements + cup.toString();
    }
    public long getLastCmdUseSec() {
        return lastCmdUseSec;
    }
    public EmbedBuilder getEditorEmbed() {
        List<SelectMenuOption> editingOptions = Collections.EMPTY_LIST;
        switch (currentlyEditing) {
            case CUP:
                break;
            case TEA:
                break;
            case TOPPINGS:
                break;
        }
        return null;
    }
    //create boba from elements
    public static BufferedImage recompileBoba(String bobaElements) {
        String[] elements = bobaElements.split(" ");
        ImgEditor boba = new ImgEditor(512, 512);
        CupStyle cupStyle = CupStyle.valueOf(elements[elements.length]);
        boba.setLayer("tea", ImgEditor.recolorImage(teaImage.get(cupStyle), teaColor.get(Tea.valueOf(elements[0]))), 0, 0);
        for (byte element = 1; element < elements.length-1; element++) {
            boba.setLayer("topping-"+element, toppingStyle.get(cupStyle).get(Topping.valueOf(elements[element])), 0, 0);
        }
        boba.setLayer("cup", cupImage.get(cupStyle), 0, 0);
        return boba.getEditedImage();
    }
}

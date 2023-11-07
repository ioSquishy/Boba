package iosquishy.ImageGen;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static HashMap<Tea, Color> teaColor = new HashMap<>(Tea.values().length);
	private static HashMap<CupStyle, HashMap<Topping, Image>> toppingStyle = new HashMap<>(CupStyle.values().length);
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

    //Return methods
    //drink -> toppings -> cup -> add metadata
    public BufferedImage getBobaImage() {
        if (this.cup!=null) {
            if (this.tea != null) {
                this.boba.setLayer("drink", ImgEditor.recolorImage(teaImage.get(cup), teaColor.get(tea)));
                if (!toppings.isEmpty()) {
                    for (Topping topping : toppings) {
                        this.boba.setLayer(topping.toString(), sealed_cup_toppingImage.get(topping));
                    }
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

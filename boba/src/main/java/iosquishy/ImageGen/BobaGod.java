package iosquishy.ImageGen;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

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
    private static HashMap<CupStyle, Image> cupImage = new HashMap<>(CupStyle.values().length);
    private static HashMap<CupStyle, Image> teaImage = new HashMap<>(CupStyle.values().length);
    private static HashMap<Tea, Color> teaColor = new HashMap<>(Tea.values().length);
	private static HashMap<CupStyle, HashMap<Topping, Image>> toppingStyle = new HashMap<>(CupStyle.values().length);
    private static HashMap<Topping, Image> sealed_cup_toppingImage = new HashMap<>(Topping.values().length);
    public static void initBobaMaps() {
        /* Cup Styles */
        //SEALED_CUP:
        cupImage.put(CupStyle.SEALED_CUP, ImgEditor.getImageFromPath("boba\\src\\main\\assets\\CupStyles\\SEALED_CUP.png"));
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
        //GREEN_TEA:
        teaColor.put(Tea.GREEN_TEA, new Color(205, 225, 156));
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
    
    //Local Stuff
    private ImgEditor boba;
    private CupStyle cup = null;
    private Tea tea = null;
    private ArrayList<Topping> toppings = new ArrayList<Topping>();

    public BobaGod() {
        this.boba = new ImgEditor(512, 512);
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
    public BufferedImage getBoba() {
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

    //create boba from elements
    public static BufferedImage recompileBoba(String bobaElements) {
        String[] elements = bobaElements.split(" ");
        ImgEditor boba = new ImgEditor(512, 512);
        for (String element : elements) {
            //convert string to enum and add img to boba
        }
        return boba.getEditedImage();
    }
}

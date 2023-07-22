package iosquishy.Utility;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

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
        cupImage.put(CupStyle.SEALED_CUP, getImage("src\\main\\assets\\CupStyles\\SEALED_CUP.png"));
        //CAPPED_CUP:

        //JAR:


        /* Tea Image */
        //SEALED_CUP
        teaImage.put(CupStyle.SEALED_CUP, getImage("src\\main\\assets\\TeaShapes\\SEALED_CUP_TEA.png"));
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
        sealed_cup_toppingImage.put(Topping.PEARL, getImage("src\\main\\assets\\Toppings\\PEARL.png"));
        //LYCHEE:

        //JELLY:

        //MANGO:

		/* Topping Style */
		toppingStyle.put(CupStyle.SEALED_CUP, sealed_cup_toppingImage);
    }
    
    //Local Stuff
    private ImgStacker boba;
    private CupStyle cup = null;
    private Tea tea = null;
    private ArrayList<Topping> toppings = new ArrayList<Topping>();

    public BobaGod() {
        this.boba = new ImgStacker(512, 512);
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
    //drink -> toppings -> cup
    public BufferedImage getBoba() {
        if (this.cup!=null) {
            if (this.tea != null) {
                this.boba.setLayer("drink", recolorImage(teaImage.get(cup), teaColor.get(tea)));
                if (!toppings.isEmpty()) {
                    for (Topping topping : toppings) {
                        this.boba.setLayer(topping.toString(), sealed_cup_toppingImage.get(topping));
                    }
                }
            }
            this.boba.setLayer("cup", cupImage.get(cup));
        }
        return boba.getStackedImage();
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

    /**
     * Changes opaque pixels on an image to specified color.
     * 
     * @param image Image to edit.
     * @param color Color to make opaque pixels in image.
     * @return Edited BufferedImage.
     */
    private static BufferedImage recolorImage(Image image, Color color) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        //Convert Image to BufferedImage
        BufferedImage bimage = new BufferedImage(width, height, 2);
        bimage.createGraphics().drawImage(image, 0, 0, null);

        //Recolor pixels in the BufferedImage
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color originalColor = new Color(bimage.getRGB(x, y), true);
                //Checks if original pixel was opaque
                if (originalColor.getAlpha() == 255) {
                    bimage.setRGB(x, y, color.getRGB());
                }
            }
        }
        return bimage;
    }

    /**
     * Gets an image from the specified filepath.
     * 
     * @param filePath Image file path.
     * @return Image
     */
    private static Image getImage(String filePath) {
        return new ImageIcon(filePath).getImage();
    }
}

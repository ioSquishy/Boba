package boba.Utility;

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
    private static HashMap<CupStyle, Image> cupImage = new HashMap<>(CupStyle.values().length, 0);
    private static HashMap<Tea, Color> teaColor = new HashMap<>(Tea.values().length, 0);
    private static HashMap<Topping, Image> toppingImage = new HashMap<>(Topping.values().length, 0);
    private static final Image teaImage = new ImageIcon().getImage();
    public static void initBobaMaps() {
        /* Cup Styles */
        //SEALED_CUP:

        //CAPPED_CUP:

        //JAR:


        /* Tea */
        //MILK_TEA:

        //GREEN_TEA:

        //BLACK_TEA:


        /* Toppings */
        //PEARL:

        //LYCHEE:

        //JELLY:

        //MANGO:
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
    public Image getBoba() {
        if (this.cup!=null) {
            if (this.tea != null) {
                this.boba.setLayer("drink", recolorImage(teaImage, teaColor.get(tea)));
                if (!toppings.isEmpty()) {
                    for (Topping topping : toppings) {
                        this.boba.setLayer(topping.toString(), toppingImage.get(topping));
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
}

package iosquishy;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import iosquishy.ImageGen.BobaGod;
import iosquishy.ImageGen.ImgEditor;
import iosquishy.ImageGen.MenuCompiler;
import iosquishy.ImageGen.BobaGod.CupStyle;
import iosquishy.ImageGen.BobaGod.Tea;
import iosquishy.ImageGen.BobaGod.Topping;
import iosquishy.ImageGen.MenuCompiler.Menu;

class Test {
    private static enum test{
        A, B, C
    }
    public static void main(String[] args) {
        // BufferedImage img = ImgEditor.getImageFromURL("https://cdn.discordapp.com/attachments/818275525797609472/1132459889692770416/image.png");
        String[] test = new String[10];
        for (String t : test) {
            System.out.print(t + ", ");
        }
        System.out.println();
        test[2] = "hai";
        for (String t : test) {
            System.out.print(t + ", ");
        }
        // Data.initMongoDB();

        // long userID = 263049275196309506L;
        // System.out.println(Player.getCoins(userID));
        // Player.addCoins(userID, (short) 2);
        // System.out.println(Player.getCoins(userID));



        // BobaGod.initBobaMaps();

        // BobaGod boba = new BobaGod();
        // boba.setCupStyle(CupStyle.SEALED_CUP);
        // boba.setTea(Tea.MILK_TEA);
        // boba.addTopping(Topping.PEARL);
        // BufferedImage singleBoba = boba.getBoba();

        // Image[] testImages = new Image[] {singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba};

        // File outputfile = new File("output.png");
        // try {
        //     ImageIO.write(img, "png", outputfile);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

}
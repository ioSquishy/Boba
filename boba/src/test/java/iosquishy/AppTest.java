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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import iosquishy.ImageGen.BobaGod;
import iosquishy.ImageGen.ImgEditor;
import iosquishy.ImageGen.MenuCompiler;
import iosquishy.ImageGen.BobaGod.CupStyle;
import iosquishy.ImageGen.BobaGod.Tea;
import iosquishy.ImageGen.BobaGod.Topping;
import iosquishy.ImageGen.MenuCompiler.Menu;

class Test {
    private static ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();
    public static void main(String[] args) {
        // BufferedImage img = ImgEditor.getImageFromURL("https://cdn.discordapp.com/attachments/818275525797609472/1132459889692770416/image.png");
        
        Data.initMongoDB();

        long userID = 263049275196309506L;
        System.out.println(Player.getCoins(userID));
        Player.setCoinsPerMinute(userID, 10);

        exe.schedule(() -> {
            System.out.println("\nfirst op");
            checkCoins(userID);
            Player.setCoinBonusMultiplier(userID, 2, (short) 2);
            System.out.println("bonus set for 2 mins");
        }, 62, TimeUnit.SECONDS);

        exe.schedule(() -> {
            System.out.println("\nsecond op");
            checkCoins(userID);
        }, 62*2, TimeUnit.SECONDS);

        exe.schedule(() -> {
            System.out.println("\nthird op, 2 mins passed");
            checkCoins(userID);
        }, 62*3, TimeUnit.SECONDS);

        exe.schedule(() -> {
            System.out.println("\nfourth op, 3 mins passed");
            checkCoins(userID);
        }, 62*4, TimeUnit.SECONDS);

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

    static void checkCoins(long userID) {
        System.out.println("coins " + Player.getCoins(userID));
    }

}
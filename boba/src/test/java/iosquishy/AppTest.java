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
import iosquishy.ImageGen.MenuCompiler;
import iosquishy.ImageGen.BobaGod.CupStyle;
import iosquishy.ImageGen.BobaGod.Tea;
import iosquishy.ImageGen.BobaGod.Topping;
import iosquishy.ImageGen.MenuCompiler.MenuTheme;

class Test {
    private static ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();

    private static enum TestEnum {
        A, B, C
    }
    public static void main(String[] args) {
        // BufferedImage img = ImgEditor.getImageFromURL("https://cdn.discordapp.com/attachments/818275525797609472/1132459889692770416/image.png");
        
        // Data.initMongoDB();

        BobaGod.initBobaMaps();

        BobaGod boba = new BobaGod(0);
        boba.setCupStyle(CupStyle.SEALED_CUP);
        boba.setTea(Tea.MILK_TEA);
        boba.addTopping(Topping.PEARL);
        BufferedImage singleBoba = boba.getBobaImage();

        Image[] testImages = new Image[] {singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba};
        String[] testNames = new String[] {"1234567890", "12345678901234567890", "Matcha Green Tea", "Mango Oolong Tea w/ Lychee Jellyyyyyyyyyyyyyyyyyy", "Strawberry Black Tea w/ Lychee Jelly", "test name", "test name", "testname", "test name", "test name", "test name", "testname"};

        File outputfile = new File("output.png");
        try {
            ImageIO.write(MenuCompiler.compileMenu("test cafe", MenuTheme.EMPTY, testImages, testNames), "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
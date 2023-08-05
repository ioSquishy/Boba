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
import iosquishy.ImageGen.MenuCompiler.MenuTheme;

class Test {
    private static ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();

    private static enum TestEnum {
        A, B, C
    }
    public static void main(String[] args) {
        // BufferedImage img = ImgEditor.getImageFromURL("https://cdn.discordapp.com/attachments/818275525797609472/1132459889692770416/image.png");
        
        System.out.println(TestEnum.B.ordinal());
        TestEnum t = TestEnum.valueOf("F");
        System.out.println(t.ordinal());
        // Data.initMongoDB();

        // BobaGod.initBobaMaps();

        // BobaGod boba = new BobaGod();
        // boba.setCupStyle(CupStyle.SEALED_CUP);
        // boba.setTea(Tea.MILK_TEA);
        // boba.addTopping(Topping.PEARL);
        // BufferedImage singleBoba = boba.getBoba();

        // Image[] testImages = new Image[] {singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba};

        // File outputfile = new File("output.png");
        // try {
        //     ImageIO.write(MenuCompiler.compileMenu("squishy boba", MenuTheme.EMPTY, testImages), "png", outputfile);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

}
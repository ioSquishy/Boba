package iosquishy;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
// import java.util.concurrent.Executors;
// import java.util.concurrent.ScheduledExecutorService;
// import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import iosquishy.Utility.BobaGod;
import iosquishy.Utility.MenuCompiler;
import iosquishy.Utility.BobaGod.CupStyle;
import iosquishy.Utility.BobaGod.Tea;
import iosquishy.Utility.BobaGod.Topping;
import iosquishy.Utility.MenuCompiler.Menu;

class Test {
    // private static transient ScheduledExecutorService testExe = Executors.newSingleThreadScheduledExecutor();
    // private static byte count;
    // private static transient Runnable test = () -> {
    //     System.out.println("hai");
    //     count++;
    //     if (count >= 2) {
    //         testFunction();
    //     }
    // };
    // private static transient Runnable test2 = () -> {
    //     System.out.println("bai");
    // };

    // private static void testFunction() {
    //     testExe.shutdownNow();
    //     testExe = Executors.newSingleThreadScheduledExecutor();
    //     testExe.scheduleAtFixedRate(test2, count, count, TimeUnit.SECONDS);
    // }
    public static void main(String[] args) {
        //testExe.scheduleAtFixedRate(test, 3, 3, TimeUnit.SECONDS);

        BobaGod.initBobaMaps();

        BobaGod boba = new BobaGod();
        boba.setCupStyle(CupStyle.SEALED_CUP);
        boba.setTea(Tea.MILK_TEA);
        boba.addTopping(Topping.PEARL);
        BufferedImage singleBoba = boba.getBoba();

        Image[] testImages = new Image[] {singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba, singleBoba};

        MenuCompiler menu = new MenuCompiler();
        

        File outputfile = new File("output.png");
        try {
            ImageIO.write(menu.compileMenu(Menu.EMPTY, testImages), "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
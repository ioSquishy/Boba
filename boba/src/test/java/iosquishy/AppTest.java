package iosquishy;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import iosquishy.ImageGen.BobaGod;
import iosquishy.ImageGen.MenuCompiler;
import iosquishy.ImageGen.BobaGod.CupStyle;
import iosquishy.ImageGen.BobaGod.Tea;
import iosquishy.ImageGen.BobaGod.Topping;
import iosquishy.ImageGen.MenuCompiler.Menu;

class Test {
    public static void main(String[] args) {

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
            ImageIO.write(menu.compileMenu(Menu.EMPTY, testImages, "Squishy's Boba"), "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
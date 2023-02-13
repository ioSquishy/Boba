package boba;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import boba.Utility.BobaGod;
import boba.Utility.BobaGod.CupStyle;
import boba.Utility.BobaGod.Tea;
import boba.Utility.BobaGod.Topping;

class Test {
    public static void main(String[] args) {
        BobaGod.initBobaMaps();

        BobaGod boba = new BobaGod();
        boba.setCupStyle(CupStyle.SEALED_CUP);
        
        boba.setTea(Tea.GREEN_TEA);
        boba.setTea(Tea.MILK_TEA);
        boba.addTopping(Topping.PEARL);

        File outputfile = new File("output.png");
        try {
            ImageIO.write(boba.getBoba(), "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
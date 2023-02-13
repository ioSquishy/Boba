package boba;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.image.BufferedImage;
import java.awt.Color;

import boba.Utility.ImgStacker;

class Test {
    private enum TestEnum {
        A, B, C
    }

    private static ArrayList<TestEnum> listEnums = new ArrayList<>();
    public static void main(String[] args) {
        listEnums.add(TestEnum.A);

        compareEnums(TestEnum.A);
        /*ImgStacker stack = new ImgStacker(100, 100);

        stack.setLayer("1", new ImageIcon("app\\src\\test\\resources\\1.png").getImage());
        stack.setLayer("2", new ImageIcon("app\\src\\test\\resources\\2.png").getImage());
        //BufferedImage img = recolorImage(new ImageIcon("app\\src\\test\\resources\\1.png").getImage(), Color.PINK);

        File outputfile = new File("output.png");
        try {
            ImageIO.write(stack.getStackedImage(), "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static void compareEnums(TestEnum testEnum) {
        if (listEnums.contains(testEnum)) {
            System.out.println("same address");
        }
    }

}
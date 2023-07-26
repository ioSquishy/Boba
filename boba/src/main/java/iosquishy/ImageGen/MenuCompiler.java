package iosquishy.ImageGen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class MenuCompiler {
    public static enum Menu {
        EMPTY, WOOD
    }
    private static HashMap<Menu, Image> menuImage = new HashMap<>(Menu.values().length);
    private static HashMap<Menu, Point[]> bobaCoords = new HashMap<>(Menu.values().length);
    public static void initMenuMaps() {
        //EMPTY
        menuImage.put(Menu.EMPTY, BobaGod.getImage("boba\\src\\main\\assets\\MenuImages\\EMPTY.png"));
        //WOOD
        menuImage.put(Menu.WOOD, BobaGod.getImage("boba\\src\\main\\assets\\MenuImages\\WOOD.jpg"));
    }

    private static final short menuImgWidth = 5500;
    private static final short menuImgHeight = 3000;
    private static final short menuHeaderHeight = 400;
    private static final short bobaDimensions = 512;
    private static final byte bobaLeftMargin = 100;
    private static final byte maxHorizontalBobas = 3;
    private static final short horizontalMenuDiv = menuImgWidth/maxHorizontalBobas;
    private static final byte maxVerticalBobas = 4;
    private static final short verticalMenuDiv = (menuImgHeight-menuHeaderHeight) / maxVerticalBobas;
    private static final short bobaVerticalCenteredOffset = bobaDimensions + ((verticalMenuDiv-bobaDimensions) / 2);
    private static final short TextDistanceX = (menuImgWidth/maxHorizontalBobas) - ((menuImgWidth/maxHorizontalBobas) - (bobaDimensions+bobaLeftMargin));
    private static final byte titlePadding = 100;
    private static final short titleFontSize = menuHeaderHeight-titlePadding;
    private static final short titleVerticalCenterOffset = titleFontSize + (titlePadding/2);
    public static BufferedImage compileMenu(Menu menu, Image[] bobas, String cafeName) {
        //Create ImgStacker
        ImgStacker compiledMenu = new ImgStacker(menuImgWidth, menuImgHeight);
        //Add menu backdrop
        compiledMenu.setLayer("backdrop", menuImage.get(menu));
        //Add cafe name
        BufferedImage textImage = new BufferedImage(menuImgWidth, menuImgHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D text = textImage.createGraphics();
        text.setFont(new Font("Arial", Font.PLAIN, titleFontSize));
        int fontWidth = text.getFontMetrics().stringWidth(cafeName);
        int textCenteredDisplacement = ((menuImgWidth - fontWidth) / 2);
        System.out.println("textCenteredDisplacement: "+textCenteredDisplacement);
        text.drawString(cafeName, textCenteredDisplacement, titleVerticalCenterOffset);
        text.dispose();
        compiledMenu.setLayer("cafeName", textImage);
        //Add boba
        byte currentBoba = 0;
        for (byte bobaImgX = 0; bobaImgX < maxHorizontalBobas; bobaImgX++)  {
            for (byte bobaImgY = 1; bobaImgY <= maxVerticalBobas; bobaImgY++) {
                // System.out.println("boba x: " + bobaImgX + "\nboba y:" + bobaImgY);
                // System.out.println("currentboba: " + currentBoba);
                if (currentBoba < bobas.length) {
                    // System.out.println("passed check");
                    int currentBobaX = (bobaImgX*horizontalMenuDiv) + bobaLeftMargin;
                    int currentBobaY = menuHeaderHeight + (bobaImgY*verticalMenuDiv - bobaVerticalCenteredOffset);
                    // System.out.println("cbobax: " + currentBobaX + "\ncbobay: " + currentBobaY);
                    compiledMenu.setLayer(""+currentBoba, bobas[currentBoba], currentBobaX, currentBobaY);
                    currentBoba++;
                } else {
                    break;
                }
                // System.out.println();
            }
        }
        //Return
        return compiledMenu.getStackedImage();
    }
}


package iosquishy.Utility;

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
        menuImage.put(Menu.EMPTY, getImage("boba\\src\\main\\assets\\MenuImages\\EMPTY.png"));
        //WOOD
        menuImage.put(Menu.WOOD, getImage("boba\\src\\main\\assets\\MenuImages\\WOOD.jpg"));
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
    public static BufferedImage compileMenu(Menu menu, Image[] bobas, String cafeName) {
        //Create ImgStacker
        ImgStacker compiledMenu = new ImgStacker(menuImgWidth, menuImgHeight);
        //Add menu backdrop
        compiledMenu.setLayer("backdrop", menuImage.get(menu));
        //Add cafe name
        BufferedImage textImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D text = textImage.createGraphics();
        text.setFont(new Font("Arial", Font.PLAIN, 20));
        text.drawString(cafeName, 20, 20);
        text.dispose();
        compiledMenu.setLayer("cafeName", textImage);
        //Add boba
        byte currentBoba = 0;
        for (byte bobaImgX = 0; bobaImgX < maxHorizontalBobas; bobaImgX++)  {
            for (byte bobaImgY = 1; bobaImgY <= maxVerticalBobas; bobaImgY++) {
                // System.out.println("boba x: " + bobaImgX + "\nboba y:" + bobaImgY);
                System.out.println("currentboba: " + currentBoba);
                if (currentBoba < bobas.length) {
                    // System.out.println("passed check");
                    int currentBobaX = (bobaImgX*horizontalMenuDiv) + bobaLeftMargin;
                    int currentBobaY = menuHeaderHeight + (bobaImgY*verticalMenuDiv - bobaVerticalCenteredOffset);
                    System.out.println("cbobax: " + currentBobaX + "\ncbobay: " + currentBobaY);
                    compiledMenu.setLayer(""+currentBoba, bobas[currentBoba], currentBobaX, currentBobaY);
                    currentBoba++;
                } else {
                    break;
                }
                System.out.println();
            }
        }
        //Return
        return compiledMenu.getStackedImage();
    }

    /**
     * Gets an image from the specified filepath.
     * 
     * @param filePath Image file path.
     * @return Image
     */
    private static Image getImage(String filePath) {
        return new ImageIcon(filePath).getImage();
    }
}


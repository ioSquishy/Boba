package iosquishy.ImageGen;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class MenuCompiler {
    public static enum MenuTheme {
        EMPTY, WOOD
    }
    private static HashMap<MenuTheme, Image> menuImage = new HashMap<>(MenuTheme.values().length);
    public static void initMenuMaps() {
        //EMPTY
        menuImage.put(MenuTheme.EMPTY, ImgEditor.getImageFromPath("boba\\src\\main\\assets\\MenuImages\\EMPTY.png"));
        //WOOD
        menuImage.put(MenuTheme.WOOD, ImgEditor.getImageFromPath("boba\\src\\main\\assets\\MenuImages\\WOOD.jpg"));
    }

    private static final short menuImgWidth = 5500;
    private static final short menuImgHeight = 3000;
    private static final short menuHeaderHeight = 400;
    private static final short bobaDimensions = 512;
    private static final byte bobaLeftMargin = 100;
    private static final byte bobaNameLeftMargin = 50;
    private static final byte maxHorizontalBobas = 3;
    private static final short horizontalMenuDiv = menuImgWidth/maxHorizontalBobas;
    private static final byte maxVerticalBobas = 4;
    private static final short verticalMenuDiv = (menuImgHeight-menuHeaderHeight) / maxVerticalBobas;
    private static final short bobaVerticalCenteredOffset = bobaDimensions + ((verticalMenuDiv-bobaDimensions) / 2);
    private static final byte titlePadding = 100;
    private static final short titleFontSize = menuHeaderHeight-titlePadding;
    private static final short titleVerticalCenterOffset = titleFontSize + (titlePadding/2);
    public static BufferedImage compileMenu(String cafeName, MenuTheme menuTheme, Image[] bobas) {
        //Create ImgStacker
        ImgEditor compiledMenu = new ImgEditor(menuImgWidth, menuImgHeight);
        //Add menu backdrop
        compiledMenu.setLayer("backdrop", menuImage.get(menuTheme));
        //Add cafe name
        BufferedImage cafeTitleImg = new BufferedImage(menuImgWidth, menuHeaderHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D cafeTitleText = cafeTitleImg.createGraphics();
        cafeTitleText.setFont(new Font("Arial", Font.PLAIN, titleFontSize));
        int textCenteredDisplacement = ((menuImgWidth - cafeTitleText.getFontMetrics().stringWidth(cafeName)) / 2);
        cafeTitleText.drawString(cafeName, textCenteredDisplacement, titleVerticalCenterOffset);
        cafeTitleText.dispose();
        compiledMenu.setLayer("cafeName", cafeTitleImg);
        //Add boba
        byte currentBoba = 0;
        for (byte bobaImgX = 0; bobaImgX < maxHorizontalBobas; bobaImgX++)  {
            for (byte bobaImgY = 1; bobaImgY <= maxVerticalBobas; bobaImgY++) {
                if (currentBoba < bobas.length) {
                    //Add boba images
                    int currentBobaX = (bobaImgX*horizontalMenuDiv) + bobaLeftMargin;
                    int currentBobaY = menuHeaderHeight + (bobaImgY*verticalMenuDiv - bobaVerticalCenteredOffset);
                    compiledMenu.setLayer(""+currentBoba, bobas[currentBoba], currentBobaX, currentBobaY);
                    //Add boba names
                    int currentBobaNameX = currentBobaX + menuImgWidth + bobaNameLeftMargin;
                    BufferedImage bobaNameImg = new BufferedImage(horizontalMenuDiv, verticalMenuDiv, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D bobaNameText = bobaNameImg.createGraphics();
                    bobaNameText.setFont(new Font("Arial", Font.PLAIN, verticalMenuDiv/2));
                    bobaNameText.drawString(cafeName, textCenteredDisplacement, titleVerticalCenterOffset);
                    bobaNameText.dispose();
                    compiledMenu.setLayer(""+currentBoba+"Name", bobaNameImg, currentBobaNameX, currentBobaY);
                    currentBoba++;
                } else {
                    break;
                }
            }
        }
        //Return
        return compiledMenu.getEditedImage();
    }
}


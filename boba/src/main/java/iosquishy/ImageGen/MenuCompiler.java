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
    private static final byte maxHorizontalBobas = 3;
    private static final short horizontalMenuDiv = menuImgWidth/maxHorizontalBobas;
    private static final byte maxVerticalBobas = 4;
    private static final short verticalMenuDiv = (menuImgHeight-menuHeaderHeight) / maxVerticalBobas;
    private static final short bobaVerticalCenteredOffset = bobaDimensions + ((verticalMenuDiv-bobaDimensions) / 2);
    private static final byte titlePadding = 100;
    private static final short titleFontSize = menuHeaderHeight-titlePadding;
    private static final short titleVerticalCenterOffset = titleFontSize + (titlePadding/2);
    private static final Font titleFont = new Font("Arial", Font.PLAIN, titleFontSize);
    private static final Font nameFont = new Font("Arial", Font.PLAIN, verticalMenuDiv/4);
    public static BufferedImage compileMenu(String cafeName, MenuTheme menuTheme, Image[] bobas, String[] bobaNames) {
        //Create ImgStacker
        ImgEditor compiledMenu = new ImgEditor(menuImgWidth, menuImgHeight);
        //Add menu backdrop
        compiledMenu.setLayer("backdrop", menuImage.get(menuTheme));
        //Create text buffered image
        BufferedImage textImg = new BufferedImage(menuImgWidth, menuImgHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D text = textImg.createGraphics();
        //add cafe name text
        text.setFont(titleFont);
        text.drawString(cafeName, ((menuImgWidth - text.getFontMetrics().stringWidth(cafeName)) / 2), titleVerticalCenterOffset);
        //Change boba name
        text.setFont(nameFont);
        //Add boba
        byte currentBoba = 0;
        for (byte bobaImgX = 0; bobaImgX < maxHorizontalBobas; bobaImgX++)  {
            for (byte bobaImgY = 1; bobaImgY <= maxVerticalBobas; bobaImgY++) {
                if (currentBoba < bobas.length) {
                    //Add boba images
                    int currentBobaX = (bobaImgX*horizontalMenuDiv) + bobaLeftMargin;
                    int currentBobaY = menuHeaderHeight + (bobaImgY*verticalMenuDiv - bobaVerticalCenteredOffset);
                    compiledMenu.setLayer(""+currentBoba, bobas[currentBoba], currentBobaX, currentBobaY);
                    //add boba names
                    if (bobaDimensions+text.getFontMetrics().stringWidth(bobaNames[currentBoba]) > horizontalMenuDiv) {
                        System.out.println("occupancy:  " + bobaDimensions+text.getFontMetrics().stringWidth(bobaNames[currentBoba]) + " | hmd: " + horizontalMenuDiv);
                        String[] splitWords = bobaNames[currentBoba].split(" ");
                        System.out.println("length: " + splitWords.length);
                        int fontHeight = text.getFontMetrics().getHeight();
                        //first try splitting name name in half
                        if (splitWords.length >= 1) {
                            short topXlength = 0;
                            short bottomXlength = 0;
                            for (short word = 0; word < splitWords.length; word++) {
                                System.out.println("word: " + word);
                                if (word+1 > splitWords.length/2) {
                                    //top word
                                    text.drawString(splitWords[word] +" ", currentBobaX+bobaDimensions+topXlength, currentBobaY+menuHeaderHeight+(fontHeight/2));
                                    topXlength += text.getFontMetrics().stringWidth(splitWords[word] + " ");
                                } else {
                                    //bottom word
                                    text.drawString(splitWords[word]+" ", currentBobaX+bobaDimensions+bottomXlength, currentBobaY+menuHeaderHeight-(fontHeight/2));
                                    bottomXlength += text.getFontMetrics().stringWidth(splitWords[word] + " ");
                                }
                            }
                        }
                    } else {
                        text.drawString(bobaNames[currentBoba], currentBobaX+bobaDimensions, currentBobaY+menuHeaderHeight);
                    }
                    currentBoba++;
                } else {
                    break;
                }
            }
        }
        text.dispose();
        compiledMenu.setLayer("text", textImg, 0, 0);
        //Return
        return compiledMenu.getEditedImage();
    }
}


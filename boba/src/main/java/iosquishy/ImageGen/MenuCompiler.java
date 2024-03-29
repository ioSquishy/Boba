package iosquishy.ImageGen;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final short initNameFontSize = verticalMenuDiv/4;
    private static final Font nameFont = new Font("Arial", Font.PLAIN, initNameFontSize);
    private static final byte maxNameShrinkingLoops = 100;
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
                    //Add boba image
                    int currentBobaX = (bobaImgX*horizontalMenuDiv) + bobaLeftMargin;
                    int currentBobaY = menuHeaderHeight + (bobaImgY*verticalMenuDiv - bobaVerticalCenteredOffset);
                    compiledMenu.setLayer(""+currentBoba, bobas[currentBoba], currentBobaX, currentBobaY);
                    //add boba name
                    if (bobaDimensions+text.getFontMetrics().stringWidth(bobaNames[currentBoba]) > horizontalMenuDiv) { //if name is longer than horizontal menu div
                        String[] splitWords = bobaNames[currentBoba].split(" ");
                        if (splitWords.length > 1) { //if name has more than one word
                            ArrayList<String> topLine = new ArrayList<String>(Arrays.asList(splitWords));
                            ArrayList<String> bottomLine = new ArrayList<String>();
                            byte loops = 0;
                            boolean nameTooLong = false;
                            do {
                                if (bobaDimensions+text.getFontMetrics().stringWidth(compileSentenceFromArray(topLine)) > horizontalMenuDiv) { //if top line is still longer than menudiv
                                    int transWordLength = text.getFontMetrics().stringWidth(topLine.get(topLine.size()-1));
                                    if (text.getFontMetrics().stringWidth(compileSentenceFromArray(bottomLine))+transWordLength <= text.getFontMetrics().stringWidth(compileSentenceFromArray(topLine))) { //check whether adding the word to the bottom line would be more space effecient or not, if so add it to bottom
                                        bottomLine.add(0, topLine.remove(topLine.size()-1));
                                    } else { //if keeping the word on top is more effecient but the top is still too long, shrink font
                                        short shrinkCount = 1;
                                        while (bobaDimensions+text.getFontMetrics().stringWidth(compileSentenceFromArray(bottomLine)) > horizontalMenuDiv || bobaDimensions+text.getFontMetrics().stringWidth(compileSentenceFromArray(topLine)) > horizontalMenuDiv) { //checks both lines to see if they are short enough
                                            if (initNameFontSize-shrinkCount <= 10) { //if font size is <= 10
                                                text.setFont(new Font("Arial", Font.PLAIN, initNameFontSize-(initNameFontSize/3)));
                                                text.drawString("Boba name is too long...", currentBobaX+bobaDimensions, currentBobaY+menuHeaderHeight);
                                                nameTooLong = true;
                                                break;
                                            } else {
                                                text.setFont(new Font("Arial", Font.PLAIN, initNameFontSize-shrinkCount));
                                                shrinkCount++;
                                            }
                                        }
                                        if (nameTooLong) {
                                            break; //already drew the string if it was too long
                                        }
                                        text.drawString(compileSentenceFromArray(bottomLine), currentBobaX+bobaDimensions, currentBobaY+menuHeaderHeight+(text.getFontMetrics().getHeight()/2)); //bottom word
                                        text.drawString(compileSentenceFromArray(topLine), currentBobaX+bobaDimensions, currentBobaY+menuHeaderHeight-(text.getFontMetrics().getHeight()/2)); //top word
                                        text.setFont(nameFont);
                                        break;
                                    }
                                } else { //top line fits and bottom line fits
                                    text.drawString(compileSentenceFromArray(bottomLine), currentBobaX+bobaDimensions, currentBobaY+menuHeaderHeight+(text.getFontMetrics().getHeight()/2)); //bottom word
                                    text.drawString(compileSentenceFromArray(topLine), currentBobaX+bobaDimensions, currentBobaY+menuHeaderHeight-(text.getFontMetrics().getHeight()/2)); //top word
                                    break;
                                }
                            } while (loops <= maxNameShrinkingLoops);
                        } else { //if name is too long and only 1 word
                            short shrinkCount = 1;
                            while (bobaDimensions+text.getFontMetrics().stringWidth(bobaNames[currentBoba]) > horizontalMenuDiv) { 
                                if (initNameFontSize-shrinkCount <= 10) { //if font size is <= 10
                                    text.setFont(new Font("Arial", Font.PLAIN, initNameFontSize-(initNameFontSize/3)));
                                    text.drawString("Boba name is too long...", currentBobaX+bobaDimensions, currentBobaY+menuHeaderHeight);
                                    break;
                                } else {
                                    text.setFont(new Font("Arial", Font.PLAIN, initNameFontSize-shrinkCount));
                                    shrinkCount++;
                                }
                            }
                            text.drawString(bobaNames[currentBoba], currentBobaX+bobaDimensions, currentBobaY+menuHeaderHeight);
                            text.setFont(nameFont);
                        }
                    } else { //if name fits with no modification needed
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

    private static String compileSentenceFromArray(ArrayList<String> list) {
        String sentence = "";
        for (String word : list) {
            sentence += word + " ";
        }
        return sentence.stripLeading().stripTrailing();
    }
}


package iosquishy.Utility;

import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class MenuCompiler {
    public static enum Menu {
        WOOD
    }
    private static HashMap<Menu, Image> menuImage = new HashMap<>(Menu.values().length);
    private static HashMap<Menu, Point[]> bobaCoords = new HashMap<>(Menu.values().length);
    public static void initMenuMaps() {
        //WOOD
        menuImage.put(Menu.WOOD, getImage("boba\\src\\main\\assets\\MenuImages\\WOOD.jpg"));
    }

    public static Image compileMenu(Menu menu, Image[] bobas) {
        //Create ImgStacker
        ImgStacker compiledMenu = new ImgStacker(6000, 3000);
        //Add menu backdrop
        compiledMenu.setLayer("backdrop", menuImage.get(menu));
        //Add boba
        for (int bobaImg = 0; bobaImg < bobas.length; bobaImg++) {
            compiledMenu.setLayer(""+bobaImg, bobas[bobaImg], (int) bobaCoords.get(menu)[bobaImg].getX(), (int) bobaCoords.get(menu)[bobaImg].getY());
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


package boba.Utility;

import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;

public class SettingCompiler {
    public static enum Setting {
        WOOD
    }
    private static HashMap<Setting, Image> settingImage = new HashMap<>(Setting.values().length, 0);
    private static HashMap<Setting, Point[]> bobaCoords = new HashMap<>(Setting.values().length, 0);
    public static void initSettingMaps() {
        //WOOD:
    }

    public static Image compileSetting(Setting setting, Image[] bobas) {
        //Create ImgStacker
        ImgStacker compiledSetting = new ImgStacker(settingImage.get(setting).getWidth(null), settingImage.get(setting).getHeight(null));
        //Add setting backdrop
        compiledSetting.setLayer("backdrop", settingImage.get(setting));
        //Add boba
        for (int bobaImg = 0; bobaImg < bobas.length; bobaImg++) {
            compiledSetting.setLayer(""+bobaImg, bobas[bobaImg], (int) bobaCoords.get(setting)[bobaImg].getX(), (int) bobaCoords.get(setting)[bobaImg].getY());
        }
        //Return
        return compiledSetting.getStackedImage();
    }
}

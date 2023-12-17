package iosquishy.ImageGen;

import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImgEditor {
    private final BufferedImage stackedImage;
    private final LinkedHashMap<String, Image> layers = new LinkedHashMap<>();
    private final HashMap<String, Point> transforms = new HashMap<>();

    /**
     * Constructs a {@link BufferedImage} object with ARGB type set automatically.
     * 
     * @param width Width of image.
     * @param height Height of image.
     */
    public ImgEditor(int width, int height) {
        this.stackedImage = new BufferedImage(width, height, 2);
    }

    /**
     * Sets specified layer to the image provided.
     * 
     * @param layerName Layer to change.
     * @param newImg Image to set layer to.
     */
    public void setLayer(String layerName, Image newImage) {
        this.layers.put(layerName, newImage);
    }

    /**
     * Sets specified layer to the image provided and offsets it from the top-left corner by the specified x and y values.
     * 
     * @param layerName Layer to change.
     * @param newImage Image to set layer to.
     * @param x X-coordinate from top-left corner to place image.
     * @param y Y-coordinate from top-left corner to place image.
     */
    public void setLayer(String layerName, Image newImage, int x, int y) {
        this.layers.put(layerName, newImage);
        this.transforms.put(layerName, new Point(x, y));
    }

    /**
     * Removes specified layer form the final image.
     * 
     * @param layerName Name of layer to remove.
     * @return Name of layer.
     */
    public String removeLayer(String layerName) {
        this.layers.remove(layerName);
        this.transforms.remove(layerName);
        return layerName;
    }

    /**
     * Returns the image.
     * 
     * @return Image
     */
    public BufferedImage getEditedImage() {
        Graphics graphics = this.stackedImage.createGraphics();
        for (Map.Entry<String, Image> entry : this.layers.entrySet()) {
            String layerName = entry.getKey();
            Image image = entry.getValue();

            if (this.transforms.get(layerName) == null) {
                graphics.drawImage(image, 0, 0, null);
            } else {
                graphics.drawImage(image, (int) transforms.get(layerName).getX(), (int) transforms.get(layerName).getY(), null);
            }
        }
        return stackedImage;
    }

    /**
     * Changes opaque pixels on an image to specified color.
     * 
     * @param image Image to edit.
     * @param color Color to make opaque pixels in image.
     * @return Edited BufferedImage.
     */
    public static BufferedImage recolorImage(Image image, Color color) {
        //Convert Image to BufferedImage
        BufferedImage bimage = imageToBimage(image);
        
        //Recolor pixels in the BufferedImage
        int width = bimage.getWidth();
        int height = bimage.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color originalColor = new Color(bimage.getRGB(x, y), true);
                //Checks if original pixel was opaque
                if (originalColor.getAlpha() == 255) {
                    bimage.setRGB(x, y, color.getRGB());
                }
            }
        }
        return bimage;
    }

    /**
     * Gets an image from the specified filepath.
     * 
     * @param filePath Image file path.
     * @return Image
     */
    public static Image getImageFromPath(String filePath) {
        return new ImageIcon(filePath).getImage();
    }

    /**
     * Converts Image to BufferedImage
     * @param image
     * @return BufferedImage
     */
    public static BufferedImage imageToBimage(Image image) {
        BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bimage.createGraphics().drawImage(image, 0, 0, null);
        return bimage;
    }

    /**
     * Gets an image from specified URL.
     * 
     * @param url Url as string.
     * @return Image
     */
    public static BufferedImage getImageFromURL(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

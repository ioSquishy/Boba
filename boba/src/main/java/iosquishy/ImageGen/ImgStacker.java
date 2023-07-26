package iosquishy.ImageGen;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImgStacker {
    private final BufferedImage stackedImage;
    private final LinkedHashMap<String, Image> layers = new LinkedHashMap<>();
    private final HashMap<String, Point> transforms = new HashMap<>();

    /**
     * Constructs a {@link BufferedImage} object with ARGB type set automatically.
     * 
     * @param width Width of image.
     * @param height Height of image.
     */
    public ImgStacker(int width, int height) {
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
     * Returns the stacked image.
     * 
     * @return Image
     */
    public BufferedImage getStackedImage() {
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
}

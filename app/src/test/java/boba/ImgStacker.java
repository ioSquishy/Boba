package boba;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImgStacker {
    private final BufferedImage stackedImage;
    private final HashMap<String, Image> layers = new HashMap<>();
    private final HashMap<String, int[]> transforms = new HashMap<>();

    /**
     * Constructs a {@link BufferedImage} object with ARGB type set automatically.
     * 
     * @param width Width of image.
     * @param height Height of image.
     */
    public ImgStacker(int width, int height) {
        stackedImage = new BufferedImage(width, height, 2);
    }

    /**
     * Adds a layer to the to the final image with the specified image.
     * 
     * @param layerName What you want to call the layer.
     * @param image The image you want on the layer.
     */
    public void addLayer(String layerName, Image image) {
        layers.put(layerName, image);
    }

    /**
     * Adds a layer to the final image with the specified image and offsets it from the top left corner with specified x and y values.
     * 
     * @param layerName What you want to call the layer.
     * @param image The image you want on the layer.
     * @param x X-coordinate from top-left corner to place image.
     * @param y Y-coordinate from top-left corner to place image.
     */
    public void addLayer(String layerName, Image image, int x, int y) {
        layers.put(layerName, image);
        transforms.put(layerName, new int[] {x, y});
    }

    /**
     * Removes specified layer form the final image.
     * 
     * @param layerName Name of layer to remove.
     * @return Name of layer.
     */
    public String removeLayer(String layerName) {
        layers.remove(layerName);
        transforms.remove(layerName);
        return layerName;
    }

    /**
     * Sets specified layer to the image provided.
     * 
     * @param layerName Layer to change.
     * @param newImg Image to set layer to.
     */
    public void setLayer(String layerName, Image newImage) {
        layers.put(layerName, newImage);
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
        layers.put(layerName, newImage);
        transforms.put(layerName, new int[] {x, y});
    }

    /**
     * Returns the stacked image.
     * 
     * @return Image
     */
    public BufferedImage getStackedImage() {
        Graphics graphics = stackedImage.createGraphics();
        for (Map.Entry<String, Image> entry : layers.entrySet()) {
            String layerName = entry.getKey();
            Image image = entry.getValue();
            
            if (transforms.get(layerName) == null) {
                graphics.drawImage(image, 0, 0, null);
            } else {
                graphics.drawImage(image, transforms.get(layerName)[0], transforms.get(layerName)[1], null);
            }
        }
        /*for (Image layer : layers.values()) {
            graphics.drawImage(layer, 0, 0, null);
        }*/
        return stackedImage;
    }
}

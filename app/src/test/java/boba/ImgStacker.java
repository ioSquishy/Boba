package boba;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ImgStacker {
    private final BufferedImage finalImage;
    private final HashMap<String, Image> layers = new HashMap<>();

    /**
     * Constructs a {@link BufferedImage} object with ARGB type set automatically.
     * 
     * @param width Width of image.
     * @param height Height of image.
     */
    public ImgStacker(int width, int height) {
        finalImage = new BufferedImage(width, height, 2);
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
     * Removes specified layer form the final image.
     * 
     * @param layerName Name of layer to remove.
     * @return Name of layer.
     */
    public String removeLayer(String layerName) {
        layers.remove(layerName);
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
     * Returns the final image.
     * 
     * @return Image
     */
    public BufferedImage getFinalImage() {
        Graphics graphics = finalImage.createGraphics();
        for (Image layer : layers.values()) {
            graphics.drawImage(layer, 0, 0, null);
        }
        return finalImage;
    }
}

package boba;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

class Test {
    public static void main(String[] args) {
        ImgStacker stack = new ImgStacker(100, 100);

        stack.addLayer("1", new ImageIcon("C:\\Users\\arthu\\Documents\\Boba\\app\\src\\test\\resources\\1.png").getImage());
        stack.addLayer("2", new ImageIcon("C:\\Users\\arthu\\Documents\\Boba\\app\\src\\test\\resources\\2.png").getImage(), 20, 20);
        stack.setLayer("1", new ImageIcon("C:\\Users\\arthu\\Documents\\Boba\\app\\src\\test\\resources\\3.png").getImage());
        stack.addLayer("4", new ImageIcon("C:\\Users\\arthu\\Documents\\Boba\\app\\src\\test\\resources\\4.png").getImage());
        //stack.removeLayer("1");


        File outputfile = new File("output.png");
        try {
            ImageIO.write(stack.getStackedImage(), "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        } // Write the Buffered Image into an output file
    }
}
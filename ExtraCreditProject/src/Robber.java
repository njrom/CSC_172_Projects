import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Robber extends JComponent{
    Vertex vertexOn;
    Graphics g;
    public void paintComponent(Graphics g){
        this.g = g;
        Graphics2D g2 = (Graphics2D) g;
        drawRobber(g2);
    }

    public void paintComponent(){
        Graphics2D g2 = (Graphics2D) g;
        drawRobber(g2);
    }
    private void drawRobber(Graphics2D g){
        BufferedImage robImage;
        final Double COP_SCALING_FACTOR = 0.10;
        try {
            robImage = ImageIO.read(new File("Robber.png"));
            Vertex v = this.vertexOn;
            if(!v.hasUp) { // Want to draw the house above
                int ImageWidth = (int) (robImage.getWidth()*COP_SCALING_FACTOR);
                int ImageHeight = (int) (robImage.getHeight()*COP_SCALING_FACTOR);
                g.drawImage(robImage, v.x - (ImageWidth/2+58), v.y - (ImageHeight + 10), ImageWidth, ImageHeight, null);
            } else {
                int ImageWidth = (int) (robImage.getWidth()*COP_SCALING_FACTOR);
                int ImageHeight = (int) (robImage.getHeight()*COP_SCALING_FACTOR);
                g.drawImage(robImage, v.x - (ImageWidth + 68), v.y - (ImageHeight + 10), ImageWidth, ImageHeight, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

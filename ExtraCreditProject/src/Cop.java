import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Cop extends JComponent {
    Vertex vertexOn;
    Graphics g;
    int copNumber = 1;
    public Cop(int n){
        this.copNumber =n;
    }

    public void paintComponent(Graphics g) {
        this.g = g;
        Graphics2D g2 = (Graphics2D) g;
        drawCops(g2);

    }
    public void paintComponent() {
        Graphics2D g2 = (Graphics2D) g;
        drawCops(g2);

    }
    public void drawCops(Graphics2D g){
        BufferedImage copImage;
        BufferedImage cop2Image;
        final Double COP_SCALING_FACTOR = 0.4;
        try {

            copImage = ImageIO.read(new File("cop.png"));
            cop2Image = ImageIO.read(new File("cop2.png"));
            if(copNumber == 1) {
                Vertex v = this.vertexOn;
                if (!v.hasUp) { // Want to draw the house above
                    int ImageWidth = (int) (copImage.getWidth() * COP_SCALING_FACTOR);
                    int ImageHeight = (int) (copImage.getHeight() * COP_SCALING_FACTOR);
                    g.drawImage(copImage, v.x - (ImageWidth / 2 + 55), v.y - (ImageHeight + 10), ImageWidth, ImageHeight, null);
                } else {
                    int ImageWidth = (int) (copImage.getWidth() * COP_SCALING_FACTOR);
                    int ImageHeight = (int) (copImage.getHeight() * COP_SCALING_FACTOR);
                    g.drawImage(copImage, v.x - (ImageWidth + 65), v.y - (ImageHeight + 10), ImageWidth, ImageHeight, null);
                }
            }
            if(copNumber == 2) {
                Vertex w = this.vertexOn;
                if (!w.hasUp) { // Want to draw the house above
                    int ImageWidth = (int) (cop2Image.getWidth() * COP_SCALING_FACTOR);
                    int ImageHeight = (int) (cop2Image.getHeight() * COP_SCALING_FACTOR);
                    g.drawImage(cop2Image, w.x - (ImageWidth / 2+55), w.y - (ImageHeight + 10), ImageWidth, ImageHeight, null);
                } else {
                    int ImageWidth = (int) (cop2Image.getWidth() * COP_SCALING_FACTOR);
                    int ImageHeight = (int) (cop2Image.getHeight() * COP_SCALING_FACTOR);
                    g.drawImage(cop2Image, w.x - (ImageWidth + 65), w.y - (ImageHeight + 10), ImageWidth, ImageHeight, null);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

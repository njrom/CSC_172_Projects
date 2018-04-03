/*
Project 1: 2048 Created by Nick Romano on 2/11/18
This game was going to use images for the tiles, but unfortunately had to be changed at the last minute because
of a displaying bug I was having.
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import javax.imageio.*;
import java.io.IOException;
import java.io.File;
import javax.swing.*;
import java.util.Map;

public class Game extends JPanel {
    public Map<Integer, BufferedImage>images = new HashMap<Integer, BufferedImage>();
    private static JFrame frame = new JFrame("2048");
    private GameBoard board = new GameBoard();
    private int moves = 0;
    final int PAD = 20;

    public Game(){
        readImages();
        setLayout(null);
        setBackground(Color.gray);
        setPreferredSize(new Dimension(500, 500));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyHandler());
        print2DArr(board.boardArr());
    }

    public static void print2DArr(int[][] array2D){
        for(int[] row : array2D){
            for(int n : row){
                System.out.print(n+"\t");
            }
            System.out.println();
        }
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.BLACK);
        int[][] arr2D = board.boardArr();
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        double xInc = (double) ( w - 2*PAD)/arr2D[0].length;
        double yInc = (double) (h -2*PAD)/arr2D.length;
        // Vertical Grid Lines
        for(int i = 0; i <= arr2D[0].length; i++){
            double x = PAD+ i*xInc;
            g2.draw(new Line2D.Double(x, PAD, x, h-PAD));
        }

        // Horizontal Grid Lines
        for(int i = 0; i <= arr2D.length; i++){
            double y = PAD + i*yInc;
            g2.draw(new Line2D.Double(PAD, y, w-PAD, y));
        }

        for(int i = 0; i < arr2D[0].length; i++){
            double x = PAD+ i*xInc;
            for(int j = 1; j <= arr2D.length; j++){
                double y = PAD + j*yInc;
                if(arr2D[j-1][i] != 0) {
                    String val = Integer.toString(arr2D[j - 1][i]);
                    AffineTransform at = AffineTransform.getTranslateInstance((int) x, (int) y);
                    g.setFont(new Font("Helvetica", Font.BOLD, 32));
                    g2.drawString(val, (int) x + PAD, (int) y - PAD);
                }
            }

        }
        System.out.println("Resolution of squares"+xInc+" "+yInc);
    }
    protected class KeyHandler implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {
            if(e.getKeyChar() == 'r'){
                board = new GameBoard();
            }

            if(e.getKeyChar() == 'q'){
                System.exit(0);
            }

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e){
            switch(e.getKeyCode()){
                case (KeyEvent.VK_DOWN):
                    board.move(Direction.DOWN);
                    System.out.println(KeyEvent.getKeyText(e.getKeyCode()) + " "+ board.generateTile());
                    break;
                case KeyEvent.VK_UP:
                    board.move(Direction.UP);
                    System.out.println(KeyEvent.getKeyText(e.getKeyCode()) +" "+ board.generateTile());
                    break;
                case KeyEvent.VK_LEFT:
                    board.move(Direction.LEFT);
                    System.out.println(KeyEvent.getKeyText(e.getKeyCode()) + board.generateTile());
                    break;
                case KeyEvent.VK_RIGHT:
                    board.move(Direction.RIGHT);
                    System.out.println(KeyEvent.getKeyText(e.getKeyCode()) + board.generateTile());
                    break;
                default:
                    break;
            }
            moves++;
            System.out.println("Moves: "+moves);
            System.out.println("Max Value: "+board.getMax());
            print2DArr(board.boardArr());
            if(board.gameOver){
                System.out.println("You made it to: "+board.getMax()+" Thanks for playing!");
                System.exit(0);
            }
            repaint();
        }
    }
    public void readImages(){ // I know I could've done this in a loop, but it was a last minute addition for fun sorry
        try {
            images.put(2,ImageIO.read(new File("2.png")));
        } catch (IOException e) {
            System.out.println("Failed to read in image");
        }
        try {
            images.put(4,ImageIO.read(new File("4.png")));
        } catch (IOException e) {
            System.out.println("Failed to read in image");
        }
        try {
            images.put(8,ImageIO.read(new File("8.png")));
        } catch (IOException e) {
            System.out.println("Failed to read in image");
        }
        try {
            images.put(16,ImageIO.read(new File("16.png")));
        } catch (IOException e) {
            System.out.println("Failed to read in image");
        }
        try {
            images.put(32,ImageIO.read(new File("32.png")));
        } catch (IOException e) {
            System.out.println("Failed to read in image");
        }
        try {
            images.put(64,ImageIO.read(new File("64.png")));
        } catch (IOException e) {
            System.out.println("Failed to read in image");
        }
        try {
            images.put(128,ImageIO.read(new File("128.png")));
        } catch (IOException e) {
            System.out.println("Failed to read in image");
        }
        try {
            images.put(256,ImageIO.read(new File("256.png")));
        } catch (IOException e) {
            System.out.println("Failed to read in image");
        }
        try {
            images.put(512,ImageIO.read(new File("512.png")));
        } catch (IOException e) {
            System.out.println("Failed to read in image");
        }
        try {
            images.put(1024,ImageIO.read(new File("1024.png")));
        } catch (IOException e) {
            System.out.println("Failed to read in image");
        }
        try {
            images.put(2048,ImageIO.read(new File("2048.png")));
        } catch (IOException e) {
            System.out.println("Failed to read in image");
        }
    }
    public static void main(String[] args) {
        Game game = new Game();
        frame.add(game);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}

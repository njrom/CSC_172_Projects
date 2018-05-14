import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class Board extends JFrame {
    private int numberOfMoves = 1;
    private final int NUMBER_OF_HOMES = 49;
    private final String FRAME_NAME = "Cops & Robbers";
    private final int WINDOW_WIDTH = 1100;
    private final int WINDOW_HEIGHT = 1100;
    private double topBorder = (1/9.)*WINDOW_HEIGHT;
    private static final int UPDATE_RATE = 30;
    private static Graph graph;
    private static boolean gameOver = false;
    boolean turn = true;  // On true move first cop and false move second


    public static void main(String[] args) {
        new Board();
    }
    public void reset(){

        numberOfMoves = 1;
        gameOver = false;
        turn = true;
        makeGraphArray();
        graph.generateConnections(20);
        graph.addCops();
        graph.addRobber();
        graph.updateDirections();
    }
    public  Board(){
        System.out.println("Called");
        reset();
        this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        this.setTitle(FRAME_NAME);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // DrawStuff drawStuff = new DrawStuff();
       //  this.getContentPane().add(drawStuff, BorderLayout.CENTER);
        this.getContentPane().add(new GamePanel());
        this.setVisible(true);
        this.getContentPane().getBounds();

    }

    private void makeGraphArray(){
        // We want to draw everything 1/8 of the way down on the height
        graph = new Graph();
        int numRow = (int) Math.ceil(Math.sqrt(NUMBER_OF_HOMES));
        int numCol = (int) Math.floor(Math.sqrt(NUMBER_OF_HOMES));
        // Want to divide the width by the number of desired colums + 2 for padding then start at this delta and go up to width minus delta

        int deltaX = WINDOW_WIDTH/(numRow+1);
        int deltaY = (int )(WINDOW_HEIGHT- topBorder)/(numCol+1);

        for(int i = 0; i < numRow; i++){
            for(int j = 0; j < numCol; j++){
                graph.addVertex(("H"+i+j),deltaX + deltaX*i, (int) topBorder + deltaY + deltaY*j);
            }
        }
        for(Vertex v: graph.vertices){
            for(Vertex w: graph.vertices){
                if(Math.abs(v.x -w.x) <= deltaX && Math.abs(v.y - w.y) <= deltaY){
                    if(Math.abs(v.x -w.x) == 0 ^ Math.abs(v.y - w.y) == 0){
                        v.possibleConnections.add(w);
                    }
                }
            }
        }
    }

    class GamePanel extends JPanel implements KeyListener{
        Graphics2D graphics;
        public GamePanel(){
            ActionListener animate = new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    repaint();
                }
            };
            Timer timer = new Timer(1, animate);
            timer.start();
            this.setBackground(Color.BLUE);
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
            addKeyListener(this);
            this.add(graph.cop1);
            this.add(graph.cop2);
            this.add(graph.robber);

        }

        public void addNotify() {
            super.addNotify();
            requestFocus();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.clearRect(0, 0, getWidth(), getHeight());
            graphics = (Graphics2D)g;
            readInImages();
            drawBackground(graphics);
            drawVertices(graphics);
            drawHouses(graphics);

            if(!gameOver) {
                drawHeader(graphics);
            } else
                drawGameOver(graphics);

            graph.cop1.paintComponent(g);
            graph.cop2.paintComponent(g);
            graph.robber.paintComponent(g);
        }

        public void drawGameOver(Graphics2D g){
            g.setColor(Color.LIGHT_GRAY);
            //g.fillRect(0,0, WINDOW_WIDTH, (int) topBorder);
            g.setColor(Color.black);
            g.setFont(new Font("Helvetica", Font.BOLD, 40));
            g.drawString("You Won!", 10, 40);
            g.setFont(new Font("Helvetica", Font.BOLD, 20));
            g.drawString("The Robber robed : "+numberOfMoves+" houses", 60, 80);
            g.drawString("Press r to Regenerate Level", 700, 80);
            g.setStroke(new BasicStroke(7));
            g.drawLine(0,(int) topBorder, WINDOW_WIDTH, (int) topBorder);
        }

        private void drawVertices(Graphics2D g){ //TODO: Need to figure out relative positioning on screen
            for (Vertex v : graph.vertices) {
                // drawCenteredCircle(g, v, height/60, 0);
                //for(Vertex w: v.weightedEdges.keySet())
                //  drawEdge(g,v,w,0);
                for(Vertex w : v.edges)
                    drawEdge(g, v, w, 0);
            }
        }

        private void drawEdge(Graphics2D g, Vertex v1, Vertex v2, int color ){
            int x = v1.x;
            int y = v1.y;
            int x2 = v2.x;
            int y2 = v2.y;
            if(color == 1){
                g.setPaint(Color.GREEN);
            }
            g.setStroke(new BasicStroke(7));
            Shape line = new Line2D.Double(x, y, x2, y2);
            g.draw(line);
            g.setPaint(Color.BLACK);
        }

        ArrayList<BufferedImage> possibleHouses = new ArrayList<>();

        public void readInImages(){
            BufferedImage house;
            BufferedImage house1;
            BufferedImage house2;
            BufferedImage house3;
            BufferedImage rhouse;
            try {
                house = ImageIO.read(new File("House.png"));
                possibleHouses.add(house);
                house1 = ImageIO.read(new File("House1.png"));
                possibleHouses.add(house1);
                house2 = ImageIO.read(new File("House2.png"));
                possibleHouses.add(house2);
                house3 = ImageIO.read(new File("House3.png"));
                possibleHouses.add(house3);
                rhouse = ImageIO.read(new File("rhouse.png"));
                possibleHouses.add(rhouse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void drawHouses(Graphics2D g){
            graph.updateRelLocations();
            final Double HOUSE_SCALING_FACTOR = .12;

            for(Vertex v: graph.vertices){
                BufferedImage house = possibleHouses.get(v.houseIndex);
                if(!v.hasUp) { // Want to draw the house above
                    int ImageWidth = (int) (house.getWidth()*HOUSE_SCALING_FACTOR);
                    int ImageHeight = (int) (house.getHeight()*HOUSE_SCALING_FACTOR);
                    g.drawImage(house, v.x - ImageWidth / 2, v.y - (ImageHeight + 10), ImageWidth, ImageHeight, null);

                } else {
                    int ImageWidth = (int) (house.getWidth()*HOUSE_SCALING_FACTOR);
                    int ImageHeight = (int) (house.getHeight()*HOUSE_SCALING_FACTOR);
                    g.drawImage(house, v.x - (ImageWidth + 10), v.y - (ImageHeight + 10), ImageWidth, ImageHeight, null);
                }

            }
        }

        public void drawHeader(Graphics2D g){
            g.setColor(Color.LIGHT_GRAY);
            //g.fillRect(0,0, WINDOW_WIDTH, (int) topBorder);
            g.setColor(Color.black);
            g.setFont(new Font("Helvetica", Font.BOLD, 40));
            g.drawString("Cops and Robbers", 10, 40);
            g.setFont(new Font("Helvetica", Font.BOLD, 20));
            g.drawString("The Robber has robed : "+numberOfMoves+" houses", 60, 80);
            g.drawString("Press r to Regenerate Level", 700, 80);
            g.setStroke(new BasicStroke(7));
            g.drawLine(0,(int) topBorder, WINDOW_WIDTH, (int) topBorder);
        }

        public void drawBackground(Graphics2D g){
            BufferedImage background;
            try {
                background = ImageIO.read(new File("Cloth.png"));
                g.drawImage(background, 0, 0,WINDOW_WIDTH,WINDOW_HEIGHT, null);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("Key Hit");
            boolean validMove = false;
            String direction = "";
            Cop currentCop;
            if(turn){
                currentCop = graph.cop1;

            } else{
                currentCop = graph.cop2;
            }
            if(e.getKeyChar() == 'w'){
                if(currentCop.vertexOn.up != null && !gameOver) {
                    validMove = graph.moveCop(currentCop, currentCop.vertexOn.up);
                    direction = "up";
                    if(validMove)
                        currentCop.paintImmediately(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
                }

            }
            if(e.getKeyChar() == 's'){
                if(currentCop.vertexOn.down != null && !gameOver) {
                    validMove = graph.moveCop(currentCop, currentCop.vertexOn.down);
                    direction = "down";
                    if(validMove)
                        currentCop.paintImmediately(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
                }

            }
            if(e.getKeyChar() == 'a'){
                if(currentCop.vertexOn.left != null && !gameOver) {
                    validMove = graph.moveCop(currentCop, currentCop.vertexOn.left);
                    direction = "left";
                    if(validMove)
                        currentCop.paintImmediately(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
                }

            }
            if(e.getKeyChar() == 'd'){
                if(currentCop.vertexOn.right != null && !gameOver) {
                    validMove = graph.moveCop(currentCop, currentCop.vertexOn.right);
                    direction = "right";
                    if(validMove)
                        currentCop.paintImmediately(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
                }
            }
            if(e.getKeyChar() == 'r'){
                System.out.println("Resetting");
                reset();
                repaint();
            }
            if(validMove){
                System.out.println(gameOver);
                if(graph.isGameOver()) {
                    gameOver = true;
                }
                if(!gameOver) {
                 if (graph.moveRobber(direction)){
                     graph.robber.paintImmediately(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
                     if(graph.robber.vertexOn.houseIndex != 4)
                        numberOfMoves++;
                 }
                }


                if(graph.isGameOver() && !gameOver) {
                    gameOver = true;
                }

                if(turn) turn = false;
                else turn = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }


}

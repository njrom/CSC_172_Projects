
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class StreetMap extends JFrame {
    private final String FRAME_NAME = "Project 4: Street Mapping";
    private static boolean show = false;
    private static boolean directions = false;
    private static boolean iMap = false;
    private static HashMap<Shape, Vertex> drawnVertices = new HashMap<>(); // For iMap selecting of starting and ending point
    private static Graph graph;
    private static ArrayList<Edge> path = new ArrayList<>();
    private static String startName;
    private static String endName;
    private final int DOT_FACTOR = 55; // ratio between dot size and screen height
    public static void main(String[] args) {
        try{
            String fileName = args[0];
            if(args.length > 1){ // Contains Extra parameters
                for (String s: args) {
                    if(s.equals("--show")) show = true;
                    if(s.equals("--iMap")) iMap = true;
                }
                for(int i = 0; i < args.length; i++){
                    if(args[i].equals("--directions")){
                        try{
                            directions = true;
                            startName = args[i+1];
                            endName = args[i+2];
                        } catch(IndexOutOfBoundsException e){
                            System.out.println("Did not list starting and ending location after directions");
                            e.printStackTrace();
                        }
                    }
                }
            }
            DataReader reader = new DataReader();
            graph = reader.populateGraph(fileName);
            if(directions){
                path = graph.shortestPath(startName,endName);

            }
            new StreetMap();
        } catch(IndexOutOfBoundsException e){ // no arguments passed to program
            e.printStackTrace();
        }
    }

    private StreetMap(){
        this.setSize(600,600);
        this.setTitle(FRAME_NAME);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        DrawStuff draw = new DrawStuff();
        this.add(draw, BorderLayout.CENTER);
        this.setVisible(show);
        this.getContentPane().getBounds();
        this.getContentPane().addMouseListener(new MouseDetector());

    }

    public class DrawStuff extends JComponent{
        int width;
        int height;
        Graphics g;

        public void paint(Graphics g){
            this.g = g;
            drawGraph();

        }
        private void drawGraph(){
            Graphics2D graphics = (Graphics2D)g;
            Rectangle r = getBounds();
            int border = 30;
            width = r.width - border; // Base everything on the bounds of the Window
            height = r.height - border;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setPaint(Color.BLACK);
            graph.updateRange(width, height);

            if(show) {
                // drawVertices(graphics);
                drawEdges(graphics);
                if(directions)
                    drawPath(graphics);
            }
            if(iMap){
                drawVertices(graphics);
                drawEdges(graphics);
                if(directions)
                    drawPath(graphics);
            }
        }

        private void drawPath(Graphics2D g){
            for(Edge e: path){
                drawEdge(g,e,1);
                if(iMap){ // If vertices are included we want to draw green ones too
                    drawCenteredCircle(g,e.v1, height/DOT_FACTOR, 1);
                    drawCenteredCircle(g, e.v2, height/DOT_FACTOR, 1);
                }
            }
        }
        private void drawEdges(Graphics2D g){
            for(Vertex v : graph.vertices){
                for(Edge e : v.edges)
                    drawEdge(g, e, 0);
            }
        }
        private void drawCenteredCircle(Graphics2D g,Vertex v, int r, int color) {
            if(color == 1) g.setPaint(Color.GREEN);

            int x = (int)v.x+10-(r/2); // 10 added to account for border
            int y = (int)v.y+10-(r/2);
            Shape oval = new Ellipse2D.Double(x,y,r,r);
            if(drawnVertices.containsValue(v)){
                drawnVertices.remove(v);
            }

            drawnVertices.put(oval, v);

            g.draw(oval);
            g.fill(oval);
            g.setPaint(Color.BLACK);
        }
        private void drawVertices(Graphics2D g){ //TODO: Need to figure out relative positioning on screen
            for (Vertex v : graph.vertices) {
                drawCenteredCircle(g, v, height/DOT_FACTOR, 0);
            }

        }
        private void drawEdge(Graphics2D g, Edge edge, int color ){
            int x = (int)edge.v1.x + 10;
            int y = (int)edge.v1.y + 10;
            int x2 = (int)edge.v2.x + 10;
            int y2 = (int)edge.v2.y + 10;
            if(color == 1){
                g.setPaint(Color.GREEN);
            }
            Shape line = new Line2D.Double(x, y, x2, y2);
            g.draw(line);
            g.setPaint(Color.BLACK);
        }

    }

    private class MouseDetector implements MouseListener{
        private Vertex firstV = null;
        private Vertex endV = null;
        boolean draw = false;
        @Override
        public void mouseClicked(MouseEvent me) {
            for(Shape s: drawnVertices.keySet()){
                if(s.contains(me.getPoint())){
                    if(!draw){
                        firstV = drawnVertices.get(s);
                        System.out.println("First Point: "+firstV.name);
                        draw = true;
                    } else {
                        endV = drawnVertices.get(s);
                        System.out.println("End Point: "+endV.name);
                        System.out.println();
                        path = graph.shortestPath(firstV.name, endV.name);
                        directions = true;
                        draw = false;
                        repaint();
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}

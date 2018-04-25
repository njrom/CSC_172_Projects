import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class StreetMap extends JFrame {
    private final String FRAME_NAME = "Project 4: Street Mapping";
    public static void main(String[] args) {
        try{
            String fileName = args[0];
            //String fileName = "ur.txt";
            if(args.length > 1){ // Contains Extra parameters
                //TODO: Need to listen to the parameters passed
                System.out.println();
            }
            DataReader reader = new DataReader();
            Graph graph = reader.populateGraph(fileName);
            new StreetMap(graph);
        } catch(IndexOutOfBoundsException e){ // no arguments passed to program
            e.printStackTrace();
        }
    }

    private StreetMap(Graph graph){
        this.setSize(600,600);
        this.setTitle(FRAME_NAME);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new DrawStuff(graph), BorderLayout.CENTER);
        this.setVisible(true);
        this.getContentPane().getBounds();
    }

    private class DrawStuff extends JComponent {
        int width;
        int height;
        Graph graph;
        Graphics g;
        public DrawStuff(Graph graph){
            this.graph = graph;
        }
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
            graph.updateRange();
            for(Vertex v: graph.vertices){ //Draws edges and updates x and y positions
                drawVertex(graphics, v);
            }
            for(Vertex v: graph.vertices){
                for(Edge e : v.edges){
                    drawEdge(graphics, e);
                }
            }
            //TODO: Add Mouse overtext for extra credit
        }
        private void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
            x = x+10-(r/2); // 10 added to account for border
            y = y+10-(r/2);
            g.fillOval(x,y,r,r);
        }
        private void drawVertex(Graphics2D g, Vertex v){ //TODO: Need to figure out relative positioning on screen
            double x = ((graph.minX - v.x)/(graph.minX - graph.maxX))*(width);
            // double y = ((v.y - graph.minY)/(graph.maxY - graph.minY))*height;
            double y = ((graph.maxY - v.y)/(graph.maxY - graph.minY))*(height);
            v.x = x+10; // Update the vertex to have relative positions
            v.y = y+10;
            drawCenteredCircle(g, (int)v.x, (int)v.y, height/100);
        }
        private void drawEdge(Graphics2D g, Edge edge){
            int x = (int)edge.v1.x + 10;
            int y = (int)edge.v1.y + 10;
            int x2 = (int)edge.v2.x + 10;
            int y2 = (int)edge.v2.y + 10;
            Shape line = new Line2D.Double(x, y, x2, y2);
            g.draw(line);
        }
    }
}

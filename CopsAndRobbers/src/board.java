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

public class board extends JFrame{
    private final String FRAME_NAME = "Testing Generating Planar Graphs";
    public static void main(String[] args) {
        new board();
    }
    public static class Vertex{
        int color;
        ArrayList<Vertex> edges; //TODO: Reimplement this so that every vertex knows it's edges and doesn't need adjMap
        //TODO: After doing ^ I can implement comparable to Vertex then use a priority queue to rearrange them in vertices so that the elements with the most edges are drawn first
        private Vertex(){
        }
        public int getX(int width, int height, ArrayList<Vertex> vertices){
            double theta = 2 * Math.PI / vertices.size();
            double x = width/2+(height*.4)*Math.cos(theta * vertices.indexOf(this));
            return (int) x;
        }
        public int getY(int width, int height, ArrayList<Vertex> vertices) {
            double theta = 2 * Math.PI / vertices.size();
            double y = height / 2 + (height * .4) * Math.sin(theta * vertices.indexOf(this));
            return (int) y;
        }
    }

    static class Graph {

        int numberOfVertices;
        boolean planar = false;
        ArrayList<Vertex> vertices = new ArrayList<>();
        HashMap<Vertex,ArrayList<Vertex>> adjMap;

        Graph(){
            // Must first make the graph connected.  Every nodes randomly connects another other than itself, we have n edges
            // Then for the remaining 2*n - 6 randomly connect two node
            // Then check if the Chromatic number is < 4
            while(!planar) {
                this.numberOfVertices = ThreadLocalRandom.current().nextInt(3, 20 + 1);
                this.adjMap = new HashMap<>();
                for (int i = 0; i < this.numberOfVertices; i++) {
                    Vertex vertex = new Vertex();
                    this.vertices.add(vertex);
                    this.adjMap.put(vertex, new ArrayList<>());
                }
                for (int n = 0; n < this.vertices.size(); n++) { // Adds One connection from each node to another node
                    int index = ThreadLocalRandom.current().nextInt(0, this.vertices.size()-1);
                    System.out.println("First vertex :"+n+" Second Index:"+index);
                    System.out.println(this.vertices.get(n));
                    System.out.println(this.vertices.get(index));
                    boolean connectionExists = this.adjMap.get(this.vertices.get(n)).contains(this.vertices.get(index));
                    while (n == index || connectionExists) {
                        System.out.println("Stuck1"); //TODO: Catch connection bug from other spot
                        index = ThreadLocalRandom.current().nextInt(0, this.vertices.size()); // Don't pick the same vertex
                        connectionExists = this.adjMap.get(this.vertices.get(n)).contains(this.vertices.get(index));
                        // Adds a random adjacent node
                    }
                    this.addEdge(n, index);
                }
                int numberOfAdditionalEdges = ThreadLocalRandom.current().nextInt(numberOfVertices-3, 2 * numberOfVertices - 6+1);
                for (int n = 0; n < numberOfAdditionalEdges + 1; n++) {
                    int randomNode1 = ThreadLocalRandom.current().nextInt(0, numberOfVertices);
                    int randomNode2 = ThreadLocalRandom.current().nextInt(0, numberOfVertices);
                    System.out.println("Stuck2 "+randomNode1+" "+randomNode2);
                    boolean connectionExists = this.adjMap.get(this.vertices.get(randomNode1)).contains(this.vertices.get(randomNode2));
                    int trys = 0;
                    while ((randomNode1 == randomNode2 || connectionExists) && trys < numberOfVertices) {
                        System.out.println(connectionExists);
                        randomNode2 = ThreadLocalRandom.current().nextInt(0, numberOfVertices);
                        randomNode1 = ThreadLocalRandom.current().nextInt(0, numberOfVertices);
                        trys++;
                    }
                    if(trys < numberOfVertices)
                        this.addEdge(randomNode1, randomNode2);
                }
                this.checkPlanar();
            }


        }
        Graph(int v){
            this.numberOfVertices = v;
            this.adjMap = new HashMap<>();
            for(int i = 0; i < this.numberOfVertices; i++){
                Vertex vertex = new Vertex();
                this.vertices.add(vertex);
                this.adjMap.put(vertex,new ArrayList<>());
            }
        }

        public void addEdge(int node1, int node2){
            this.adjMap.get(this.vertices.get(node1)).add(this.vertices.get(node2)); // Assuming direction doesn't matter we want it going in both
            this.adjMap.get(this.vertices.get(node2)).add(this.vertices.get(node1));
        }
        public int numberOfEdges(int vertexNumber){
            Vertex vertex = this.vertices.get(vertexNumber);
            return(this.adjMap.get(vertex).size());
        }

        private void checkPlanar(){
            System.out.println("Stuck on color");
            int[] result = new int[numberOfVertices];
            Arrays.fill(result, -1);
            result[0] = 0;
            boolean[] available = new boolean[numberOfVertices];
            Arrays.fill(available, true);

            for(int u = 1; u < numberOfVertices; u++) { // For each vertex vertices.get(u)
                Vertex vertex = this.vertices.get(u);
                for (Vertex adjVertex : adjMap.get(vertex)) {
                    if (result[this.vertices.indexOf(adjVertex)] != -1)
                        available[result[this.vertices.indexOf(adjVertex)]] = false;
                }

                int cr;
                for (cr = 0; cr < numberOfVertices; cr++) {
                    if (available[cr])
                        break;
                }

                result[u] = cr;
                this.vertices.get(u).color = cr;
                Arrays.fill(available, true);
            }
            ArrayList<Integer> uniqueColors = new ArrayList<>();
            for(int i : result){
                if(!uniqueColors.contains(i)){
                    uniqueColors.add(i);
                }
            }
            System.out.println(uniqueColors.size());
            this.planar = uniqueColors.size() <= 4;
            System.out.println(planar);
        }

    }

    private board(){

        this.setSize(600,600);
        this.setTitle(FRAME_NAME);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new DrawStuff(), BorderLayout.CENTER);
        this.setVisible(true);
        this.getContentPane().getBounds();
    }

    private class DrawStuff extends JComponent implements MouseListener{
        // Current State: Generates a shape equal to the number of vertices w/ edges drawn in as lines
        //TODO: I want to draw nodes with highest number of connections such that they are always next to nodes they are
        // Connected to
        Graphics g;
        public DrawStuff(){
            addMouseListener(this);
        }
        public void paint(Graphics g){
            this.g = g;
            drawGraph();

        }

        private void drawGraph(){
            Rectangle r = getBounds();
            Graph graph = new Graph();
            int width = r.width;
            int height = r.height;
            Graphics2D graph2D = (Graphics2D)g;
            graph2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graph2D.setPaint(Color.BLACK);

            for(Vertex vertex : graph.vertices){
                int x = vertex.getX(width, height, graph.vertices);
                int y = vertex.getY(width, height, graph.vertices);
                for(Vertex vertex2 : graph.adjMap.get(vertex)){
                    graph2D.setPaint(Color.BLACK);
                    int x2 = vertex2.getX(width, height, graph.vertices);
                    int y2 = vertex2.getY(width, height, graph.vertices);
                    Shape line2D = new Line2D.Double(x,y,x2,y2);
                    graph2D.draw(line2D);
                }
            }
            for(Vertex vertex: graph.vertices){ // Separated Out so this paints over the lines
                int x = vertex.getX(width, height, graph.vertices);
                int y = vertex.getY(width, height, graph.vertices);
                System.out.println(vertex.color);
                drawCenteredCircle(graph2D, x, y, 25, vertex.color);
            }
        }
        private void drawCenteredCircle(Graphics2D g, int x, int y, int r, int color) {
            switch(color){
                case 0:
                    g.setPaint(Color.ORANGE);
                    break;
                case 1:
                    g.setPaint(Color.BLUE);
                    break;
                case 2:
                    g.setPaint(Color.GREEN);
                    break;
                case 3:
                    g.setPaint(Color.RED);
                    break;
            }
            x = x-(r/2);
            y = y-(r/2);
            g.fillOval(x,y,r,r);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            this.repaint();
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

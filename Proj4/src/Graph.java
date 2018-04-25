import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Graph {
    ArrayList<Vertex> vertices = new ArrayList<>();
    double minX;
    double maxX;
    double minY;
    double maxY;

    private Vertex getVertex(String name){
        for(Vertex v : vertices){
            if(v.name.equals(name)) return v;
        }
        throw new RuntimeException("Vertex was not contained in list");
    }

    private ArrayList<Edge> shortestPath(Vertex v1, Vertex v2){ //TODO: Implement Dijkstra’s algorithm
        Queue<Edge> queue = new PriorityQueue<>();
        ArrayList<Edge> shortestPath = new ArrayList<>();
        for(Edge e : v1.edges) // Add on the edges to a priority queue to pick the next vertex to go to
            queue.add(e);



        return null;
    }
    // Public Methods
    public void addEdge(String name, String v1Name, String v2Name){
        Vertex v1 = this.getVertex(v1Name);
        Vertex v2 = this.getVertex(v2Name);
        Edge e1 = new Edge(name, v1, v2);
        v1.edges.add(e1);
        v2.edges.add(e1); // For non directed Graph it goes both ways
    }
    public void addVertex(String name, double x, double y){
        Vertex v = new Vertex(name, x , y);
        this.vertices.add(v);

    }
    public void updateRange(){
        minX = vertices.get(0).x;
        maxX = vertices.get(0).x;
        minY = vertices.get(0).y;
        maxY = vertices.get(0).y;
        for(Vertex v : vertices){
            if(minX > v.x) minX = v.x;
            if(maxX < v.x) maxX = v.x;
            if(minY > v.y) minY = v.y;
            if(maxY < v.y) maxY = v.y;
        }
    }
}

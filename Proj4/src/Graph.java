import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Collections;
import java.util.HashMap;

public class Graph {
    private HashMap<String, Vertex> vertexNameLookUp = new HashMap<>();
    ArrayList<Vertex> vertices = new ArrayList<>();
    private double minLog;
    private double maxLog;
    private double minLat;
    private double maxLat;

    public ArrayList<Edge> shortestPath(String sourceName, String endName) {
        ArrayList<Edge> edgePath = new ArrayList<>();
        ArrayList<Vertex> vertexPath = new ArrayList<>();
        Queue<Vertex> priorityQueue = new PriorityQueue<>();
        for(Vertex v : this.vertices){
            v.previous = null;
            v.dist = Double.MAX_VALUE;
        }
        Vertex source = this.vertexNameLookUp.get(sourceName);
        source.dist = 0;
        priorityQueue.add(source);
        while(!priorityQueue.isEmpty()) {
            source = priorityQueue.poll();
            for (Edge e: source.edges) {
                Vertex neighbor = e.vertexConnectedTo(source);
                if(neighbor.dist > source.dist + e.weight) { // Shorter path was found
                    neighbor.dist = source.dist + e.weight;
                    neighbor.previous = source;
                    priorityQueue.add(neighbor);
                }
            }
        }
        Vertex end = this.vertexNameLookUp.get(endName);
        while(end.previous != null){
            edgePath.add(end.getBackEdge());
            vertexPath.add(end);
            end = end.previous;
        }
        vertexPath.add(end);  // Adds start node (missed by the loop)
        Collections.reverse(edgePath);
        Collections.reverse(vertexPath);
        System.out.println("Directions:");
        for(Vertex v: vertexPath){
            System.out.println(v.name);
        }
        return edgePath;
    }


    public void addEdge(String name, String v1Name, String v2Name){
        Vertex v1 = this.vertexNameLookUp.get(v1Name);
        Vertex v2 = this.vertexNameLookUp.get(v2Name);
        Edge e = new Edge(name, v1, v2);
        v1.edges.add(e);
        v2.edges.add(e); // For non directed Graph it goes both ways
    }
    public void addVertex(String name, double x, double y){
        Vertex v = new Vertex(name, x , y);
        this.vertices.add(v);
        this.vertexNameLookUp.put(v.name, v);

    }
    public void updateRange(int width, int height){
        minLog = vertices.get(0).log;
        maxLog = vertices.get(0).log;
        minLat = vertices.get(0).lat;
        maxLat = vertices.get(0).lat;
        for(Vertex v : vertices){
            if(minLog > v.log) minLog = v.log;
            if(maxLog < v.log) maxLog = v.log;
            if(minLat > v.lat) minLat = v.lat;
            if(maxLat < v.lat) maxLat = v.lat;
        }
        for(Vertex v: this.vertices) {
            double x = ((this.minLog - v.log) / (this.minLog - this.maxLog)) * (width);
            // double y = ((v.y - graph.minLat)/(graph.maxLat - graph.minLat))*height;
            double y = ((this.maxLat - v.lat) / (this.maxLat - this.minLat)) * (height);
            v.x = x + 10; // Update the vertex to have relative positions
            v.y = y + 10;
        }
    }
}

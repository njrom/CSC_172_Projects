import java.util.ArrayList;

public class Vertex implements Comparable<Vertex>{
    boolean visted = false;
    String name;
    Vertex previous = null;
    double lat;
    double log;
    double x;
    double y;
    double dist = Double.MAX_VALUE;
    ArrayList<Edge> edges = new ArrayList<>();
    public Vertex(String name, double x, double y){
        this.name = name;
        this.log = x;
        this.lat = y;
    }

    public Edge getBackEdge(){
        for(Edge e: this.edges){
            if(e.v2 == this.previous || e.v1 == this.previous){
                return e;
            }
        }
        throw new RuntimeException("Vertex was not contained in edge");
    }

    @Override
    public int compareTo(Vertex v) {
        if(this.dist < v.dist) return -1;
        if(this.dist == v.dist) return 0;
        else return 1;
    }
}
import java.util.ArrayList;

public class Vertex{
    boolean visted = false;
    String name;
    double x;
    double y;
    ArrayList<Edge> edges = new ArrayList<>();
    public Vertex(String name, double x, double y){
        this.name = name;
        this.x = x;
        this.y = y;
    }
}
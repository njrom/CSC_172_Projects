import java.io.BufferedReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public class Vertex implements Comparable<Vertex>, Serializable{
    String name;
    Vertex previous;

    boolean hasUp = false;
    boolean visited = false;
    Vertex left;
    Vertex right;
    Vertex up;
    Vertex down;
    int distance = Integer.MAX_VALUE;
    int x;
    int y;
    int houseIndex = ThreadLocalRandom.current().nextInt(0,4);
    HashMap<Vertex, Integer> weightedEdges = new HashMap<>();
    ArrayList<Vertex> possibleConnections = new ArrayList<>();
    ArrayList<Vertex> edges = new ArrayList<>();

    public Vertex(String name, int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
    }
    public void setDirection(){ //TODO:  For vertex edges set a left right top and bottom
        Vertex desired = null;
        for(Vertex w: this.edges){
            if(w.y < this.y && w.x == this.x){
                this.up = w;
            }
            if(w.y > this.y && w.x == this.x){
                this.down = w;
            }
            if(w.x < this.x && w.y == this.y){
                this.left = w;
            }
            if(w.x > this.x && w.y == this.y){
                this.right = w;
            }
        }

    }
    @Override
    public int compareTo(Vertex o) {
        return Integer.compare(this.distance, o.distance);
    }
}
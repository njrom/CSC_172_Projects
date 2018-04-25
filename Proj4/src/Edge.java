public class Edge implements Comparable<Edge>{
    String name;
    double weight;
    Vertex v1;
    Vertex v2;
    public Edge(String name,Vertex v1, Vertex v2){
        this.name = name;
        this.v1 = v1;
        this.v2 = v2;
        this.weight = Math.sqrt(Math.pow((v1.x - v2.x),2) + Math.pow((v1.y - v2.y),2)); // Distance Formula
    }

    @Override
    public int compareTo(Edge e) {
        if(this.weight < e.weight) return -1;
        if(this.weight == e.weight) return 0;
        else return 1;
    }
}

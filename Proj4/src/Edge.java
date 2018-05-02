public class Edge implements Comparable<Edge>{
    String name;
    double weight;
    Vertex v1;
    Vertex v2;
    public Edge(String name,Vertex v1, Vertex v2){
        this.name = name;
        this.v1 = v1;
        this.v2 = v2;
        this.weight = haversine(v1.lat, v1.log, v2.lat, v2.log); // Distance Formula
    }
    public Vertex vertexConnectedTo(Vertex v){
        if(this.v1 == v) return v2;
        if(this.v2 == v) return v1;
        else throw new RuntimeException("Vertex was not in this edge.  Something in dataReader went wrong");
    }

    @Override
    public int compareTo(Edge e) {
        return Double.compare(this.weight, e.weight);
    }
    private static final double R = 6372.8; // In kilometers
    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) *
                                 Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}

import java.util.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

public class Graph  implements Serializable{
    ArrayList<Vertex> vertices = new ArrayList<>();
    Cop cop1 = new Cop(1);
    Cop cop2 = new Cop(2);
    Robber robber;
    public void addVertex(String name, int x, int y){
        Vertex v = new Vertex(name, x , y);
        this.vertices.add(v);
    }

    public void addCops(){
        boolean occupied = false;
        int randomVIndex = ThreadLocalRandom.current().nextInt(0, vertices.size());
        cop1.vertexOn = this.vertices.get(randomVIndex);
        int randomV2ndex = ThreadLocalRandom.current().nextInt(0, vertices.size());
        while(randomV2ndex == randomVIndex){
            randomV2ndex = ThreadLocalRandom.current().nextInt(0, vertices.size());
        }
        cop2.vertexOn = this.vertices.get(randomV2ndex);

    }

    public Robber addRobber(){
        robber = new Robber();
        boolean occupied = false;
        int randomVIndex = ThreadLocalRandom.current().nextInt(0, vertices.size());
        if(cop1.vertexOn == vertices.get(randomVIndex))
            occupied = true;
        if(cop2.vertexOn == vertices.get(randomVIndex))
            occupied = true;

        while(occupied){
            randomVIndex = ThreadLocalRandom.current().nextInt(0, vertices.size());
            if(cop1.vertexOn == vertices.get(randomVIndex))
                occupied = true;
            if(cop2.vertexOn == vertices.get(randomVIndex))
                occupied = true;
        }
        robber.vertexOn = vertices.get(randomVIndex);
        return robber;
    }

    public void generateConnections(int numberOfRandomEdges){
        for(Vertex v: this.vertices){
            for(Vertex w: v.possibleConnections){
                if(!v.weightedEdges.keySet().contains(w))
                    v.weightedEdges.put(w, ThreadLocalRandom.current().nextInt(0, this.vertices.size()));
            }
            System.out.println(v.possibleConnections.size());
        }
        this.primsAlgorithm(0);
        for(Vertex v: vertices) {
            if (v.previous != null) {
                v.edges.add(v.previous);
                v.previous.edges.add(v);
            }
        }

        for(int i = 0; i < numberOfRandomEdges; i++){
            Vertex vertex1 = vertices.get(0);
            Vertex vertex2 = vertices.get(0);
            while(vertex1 == vertex2 ) {
                vertex1 = vertices.get(ThreadLocalRandom.current().nextInt(0, vertices.size()));
                vertex2 = vertex1.possibleConnections.get(
                        ThreadLocalRandom.current().nextInt(0, vertex1.possibleConnections.size()));
            }
            vertex1.edges.add(vertex2);
            vertex2.edges.add(vertex1);
        }

    }
    public void updateRelLocations(){
        for(Vertex v: vertices){ // Update
            for(Vertex w: v.edges){
                if(v.y > w.y){
                    v.hasUp = true;
                }
            }
        }
    }

    private void primsAlgorithm(int startIndex){
        Vertex startVertex = this.vertices.get(startIndex);
        Queue<Vertex> priorityQueue = new PriorityQueue<>(this.vertices);
        for (Vertex v: this.vertices) {
            v.previous = null;
            v.distance = Integer.MAX_VALUE;
            v.visited = false;
        }
        startVertex.distance = 0;
        while(!priorityQueue.isEmpty()){
            startVertex = priorityQueue.poll();
            startVertex.visited = true;
            for(Vertex v : startVertex.weightedEdges.keySet()){
                if((v.distance > startVertex.weightedEdges.get(v))&&!v.visited){
                    v.distance = startVertex.weightedEdges.get(v);
                    v.previous = startVertex;
                    priorityQueue.remove(v);
                    priorityQueue.add(v);
                }
            }


        }
    }

    public boolean moveCop(Cop cop, Vertex v){
        boolean occupied = false;
        Vertex w = cop.vertexOn;
        Cop otherCop;
        if(cop == this.cop1){
            otherCop = this.cop2;
        } else{
            otherCop = this.cop1;
        }
        if(v == otherCop.vertexOn)
            occupied = true;
        if(w.edges.contains(v) && !occupied) {
            cop.vertexOn = v;
            return true;
        }
        return false;
    }

    public void moveRobber(){
        State s0 = new State(this.clone(), 1);
        Minimax minimax = new Minimax(3);
        robber.vertexOn = minimax.miniMaxAction(s0);
    }
    public boolean moveRobber(String direction){
        robber.vertexOn.houseIndex = 4;
        if(direction.equals("up") && robber.vertexOn.up != null) {
            robber.vertexOn = robber.vertexOn.up;

            return true;
        }
        if(direction.equals("down") && robber.vertexOn.down != null) {
            robber.vertexOn = robber.vertexOn.down;

            return true;
        }
        if(direction.equals("left") && robber.vertexOn.left != null) {
            robber.vertexOn = robber.vertexOn.left;

            return true;
        }
        if(direction.equals("right") && robber.vertexOn.right != null) {
            robber.vertexOn = robber.vertexOn.right;

            return true;
        }
        return false;
    }

    @Override
    protected Graph clone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Graph)ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }





    public boolean isGameOver(){
        return (cop1.vertexOn == robber.vertexOn || cop2.vertexOn == robber.vertexOn);
    }

    public void updateDirections(){
        for(Vertex v: vertices){
            v.setDirection();
        }
    }

}
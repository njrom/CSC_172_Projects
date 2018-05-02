import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


public class DataReader {

    public Graph populateGraph(String fileName) {
        Graph graph = new Graph();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(fileName));
            String line;

            while((line = bf.readLine()) != null) {
                String[] elements = line.split("\\s");
                String type = elements[0];
                if(type.equals("i")){ // It is a vertex
                    String name = elements[1];
                    double x = Double.parseDouble(elements[3]);
                    double y = Double.parseDouble(elements[2]);
                    graph.addVertex(name, x, y);
                }
                else if(type.equals("r")){ // It is an edge
                    String name = elements[1];
                    String nameOfV1 = elements[2];
                    String nameOfV2 = elements[3];
                    graph.addEdge(name, nameOfV1, nameOfV2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

}

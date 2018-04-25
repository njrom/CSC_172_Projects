import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataReader {
    public static void main(String[] args) {
        DataReader reader = new DataReader();
        reader.populateGraph("ur.txt");
    }
    public Graph populateGraph(String fileName) {
        Graph graph = new Graph();
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: Catch the index out of bounds expression maybe
        for(String line : lines) {
            String[] elements = line.split("\\s");
            String type = elements[0].replaceAll("\\s","");
            if(type.equals("i")){ // It is a vertex
                String name = elements[1].replaceAll("\\s","");
                double x = Double.parseDouble(elements[3]);
                double y = Double.parseDouble(elements[2]);
                graph.addVertex(name, x, y);
            }
            else if(type.equals("r")){ // It is an edge
                String name = elements[1].replaceAll("\\s","");
                String nameOfV1 = elements[2].replaceAll("\\s","");
                String nameOfV2 = elements[3].replaceAll("\\s","");
                graph.addEdge(name, nameOfV1, nameOfV2);
            }
        }
        return graph;
    }

}

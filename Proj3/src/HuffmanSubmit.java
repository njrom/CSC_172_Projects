
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanSubmit implements Huffman {

    private static class Node implements Comparable<Node>{
        private final Character value;
        private final int frequency;
        private final Node left;
        private final Node right;

        private Node(final char element, final int frequency, final Node leftChild, final Node rightChild){
            this.value = element;
            this.frequency = frequency;
            this.left = leftChild;
            this.right = rightChild;
        }

        boolean isLeaf(){
            return this.left == null && this.right == null;
        }

        @Override
        public int compareTo(Node o) {
            if(Integer.compare(this.frequency, o.frequency) != 0) return Integer.compare(this.frequency, o.frequency);
            else return this.value.compareTo(o.value); // Determine priority based on names if frequencies equal
        } // ^ Makes the tree stable (always arranges the tree the same way)
    }
 
	public void encode(String inputFile, String outputFile, String freqFile){
        BinaryIn in = new BinaryIn(inputFile);
        ArrayList<Character> elementArray = new ArrayList<>();
        while(!in.isEmpty()){
            char c = in.readChar();
            elementArray.add(c);  // Parses every 8Bits to a char
        }
        // Make Frequency Table of chars
        HashMap<Character, Integer> frequencyTable = makeFrequencyTable(elementArray);
        // Build HuffmanTree based on the frequency table
        final Node root = buildHuffmanTree(frequencyTable);
        // Assigns new more efficient bytes to each char in the lookup table
        final HashMap<Character, String> lookupTable = makeLookupTable(root);
        BinaryOut out = new BinaryOut(outputFile);

        for(Character element : elementArray){
            String binary = lookupTable.get(element);
            for(char c : binary.toCharArray()){
                if(c=='1')  out.write(true);
                else out.write(false);
            }
        }
        out.flush();
        out.close();
       writeFrequencyTable(frequencyTable, freqFile);
   }

   private static Node buildHuffmanTree(HashMap<Character, Integer> freqTable){
	    final PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
	    for(Character element : freqTable.keySet()){
	        priorityQueue.add(new Node(element, freqTable.get(element), null,null));
        }
        if(priorityQueue.size() == 1){ // For the final parent node that gets tossed into the queue
	        priorityQueue.add(new Node('\0',1,null,null));
        }
        while(priorityQueue.size() > 1){
	        final Node left = priorityQueue.poll();
	        final Node right = priorityQueue.poll();
	        final Node parent = new Node('\0', left.frequency+right.frequency, left, right);
	        priorityQueue.add(parent);
        }
        return priorityQueue.poll();
   }

   private HashMap<Character,Integer> makeFrequencyTable(ArrayList<Character> elementArray){
        HashMap<Character, Integer> frequencyTable = new HashMap<>();
        for(Character element: elementArray){
            frequencyTable.merge(element, 1, Integer::sum);
        }
        return frequencyTable;
    }
    private void writeFrequencyTable(HashMap<Character, Integer> freqTable,String fqFile){
	    try{
            PrintWriter printer = new PrintWriter(fqFile);
            for (Character element : freqTable.keySet()) {
                StringBuilder binary = new StringBuilder(Integer.toBinaryString(element));
                while(binary.length() < 8){
                    binary.insert(0, "0");
                }
                int frequency = freqTable.get(element);
                printer.println(binary+":"+frequency);
            }
            printer.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }

    }
    private static HashMap<Character,Integer> readFrequencyTable(String fileName) {
        HashMap<Character,Integer> map = new HashMap<>();
        String line;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(":", 2);
                if (parts.length >= 2)
                {
                    String key = parts[0];
                    String value = parts[1];
                    map.put((char) Integer.parseInt(key,2), Integer.parseInt(value));
                } else {
                    System.out.println("ignoring line: " + line);
                }
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return map;
    }



    private HashMap<Character, String> makeLookupTable(Node root){
	    final HashMap<Character, String> lookupTable = new HashMap<>();

	    makeLookupTableRecur(root ,"" , lookupTable);
        return lookupTable;
    }

    private static void makeLookupTableRecur(Node root, String s, final HashMap<Character, String> lookupTable) {
	    if(!root.isLeaf()){
	        makeLookupTableRecur(root.left, s+"0", lookupTable);
	        makeLookupTableRecur(root.right, s+"1", lookupTable);
        } else{
	        lookupTable.put(root.value, s);
        }
    }



    public void decode(String inputFile, String outputFile, String freqFile) {
        BinaryIn in = new BinaryIn(inputFile);
        HashMap<Character, Integer> orgFrequencyTable = readFrequencyTable(freqFile);
        Node root = buildHuffmanTree(orgFrequencyTable); //Rebuilds original Huffman Tree
        Node node = new Node(root.value,root.frequency,root.left,root.right); // Copy of the root
        BinaryOut out = new BinaryOut(outputFile);
        while(!in.isEmpty()){
            if(node.isLeaf()){
                out.write(node.value);
                node = new Node(root.value,root.frequency,root.left,root.right); // Jump back up to root
            }
            if(in.readBoolean()){
                node = node.right;
            } else{
                node = node.left;
            }
        }
        out.flush();
        out.close();
   }

   public static void main(String[] args) {
	    Huffman  huffman = new HuffmanSubmit();
        huffman.encode("alice30.txt", "alice.enc", "alice_freq.txt");
        huffman.decode("alice.enc", "alice_dec.txt","alice_freq.txt");
        huffman.encode("ur.jpg", "ur.enc", "ur_freq.txt");
		huffman.decode("ur.enc", "ur_dec.jpg", "ur_freq.txt");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same.
		// On linux and mac, you can use `diff' command to check if they are the same.
   }

}

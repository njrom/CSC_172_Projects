// Import any package as required


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



public class HuffmanSubmit implements Huffman {
 
	public void encode(String inputFile, String outputFile, String freqFile) throws IOException{
		// TODO: Read into char array from string by seperating by ":"
        BinaryIn in = new BinaryIn(inputFile);
        ArrayList<Character> elementArray = new ArrayList<>();
        while(!in.isEmpty()){
            char c = in.readChar();
            elementArray.add(c);
        }
        // Make Frequency Table
        HashMap<Character, Integer> frequencyTable = makeFrequencyTable(elementArray);
        // Build HuffmanTree based on the frequency table
        final Node root = buildHuffmanTree(frequencyTable);
        final HashMap<Character, String> lookupTable = makeLookupTable(root);
        // TODO: Need to write to file the correct way to have file size be smaller
        BinaryOut out = new BinaryOut(outputFile);

        for(Character element : elementArray){
            String binary = lookupTable.get(element);
            for(char c : binary.toCharArray()){
                if(c=='1')  out.write(true);
                else if(c=='0') out.write(false);
                else System.out.println("Something went very wrong "+c); //TODO: Throw an actual error
            }
        }
        out.flush();
        out.close();

        PrintWriter printer = new PrintWriter(freqFile);
        for (Character element : frequencyTable.keySet()) {
            String binary = Integer.toBinaryString(element);
            while(binary.length() < 8){
                binary = "0"+binary;
            }
            int frequency = frequencyTable.get(element);
            printer.println(binary+":"+frequency);
        }
        printer.close();
   }

   static class Node implements Comparable<Node>{
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

   public HashMap<Character,Integer> makeFrequencyTable(ArrayList<Character> elementArray){
        HashMap<Character, Integer> frequencyTable = new HashMap<>();
        for(Character element: elementArray){
            frequencyTable.merge(element, 1, Integer::sum);
        }
        return frequencyTable;
    }

    private static HashMap<Character, String> makeLookupTable(Node root){
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

    public static HashMap<Character,Integer> readFrequencyTable(String fileName) throws IOException, FileNotFoundException{
	    HashMap<Character,Integer> map = new HashMap<>();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        try {
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
        } catch (FileNotFoundException ex){
            System.out.println("File Not Found");
        } catch (IOException ex){
            System.out.println("IOException");
        }
        return map;
    }

    public void decode(String inputFile, String outputFile, String freqFile) throws IOException {
		// TODO: Read Frequency file in and use it to decode
        BinaryIn in = new BinaryIn(inputFile);
        String decodedFile = "";
        HashMap<Character, Integer> orgFrequencyTable = readFrequencyTable(freqFile);
        Node root = buildHuffmanTree(orgFrequencyTable); //Rebuilds original Huffman Tree
        Node node = new Node(root.value,root.frequency,root.left,root.right); // Copy of the root
        while(!in.isEmpty()){
            if(node.isLeaf()){
                decodedFile += node.value;
                node = new Node(root.value,root.frequency,root.left,root.right); // Jump back up to root
            }
            if(in.readBoolean()){
                node = node.right;
            } else{
                node = node.left;
            }
        }
        PrintWriter printer = new PrintWriter(outputFile);
        printer.print(decodedFile);
        printer.close();
   }



   public static void main(String[] args) throws IOException {
	    Huffman  huffman = new HuffmanSubmit();
        // huffman.encode("alice30.txt", "alice.enc", "freq.txt");
        // huffman.decode("alice.enc", "alice_dec.txt","freq.txt");
        huffman.encode("ur.jpg", "ur.enc", "freq.txt");
		huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same.
		// On linux and mac, you can use `diff' command to check if they are the same.

   }

}

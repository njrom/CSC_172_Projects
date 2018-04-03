// Import any package as required


import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanSubmit implements Huffman {
	// Feel free to add more methods and variables as required. 
 
	public void encode(String inputFile, String outputFile, String freqFile){
		// TODO: Read into char array from string by seperating by ":"
        String[] elementArray = inputFile.split(":");
        // Make Frequency Table
        HashMap<String, Integer> frequencyTable = makeFrequencyTable(elementArray);
        // Build HuffmanTree based on the frequency table
        final Node root = buildHuffmanTree(frequencyTable);
        System.out.println(root);
        // TODO: Use Binary Tree to Hoffman encode File (Traverse the tree)


   }
   static class Node implements Comparable<Node>{
	    private final String element;
	    private final int frequency;
	    private final Node leftChild;
	    private final Node rightChild;

	    private Node(final String element, final int frequency, final Node leftChild, final Node rightChild){
	        this.element = element;
	        this.frequency = frequency;
	        this.leftChild = leftChild;
	        this.rightChild = rightChild;
        }

        boolean isLeaf(){
	        return this.leftChild == null && this.rightChild == null;
        }

       @Override
       public int compareTo(Node o) {
	        if(Integer.compare(this.frequency, o.frequency) != 0) return Integer.compare(this.frequency, o.frequency);
	        else return this.element.compareTo(o.element); // Determine priority based on names if frequencies equal
       } // ^ Makes the tree stable (always arranges the tree the same way)
   }

   private static Node buildHuffmanTree(HashMap<String, Integer> freqTable){
	    final PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
	    for(String element : freqTable.keySet()){
	        priorityQueue.add(new Node(element, freqTable.get(element), null,null));
        }
        if(priorityQueue.size() == 1){ // For the final parent node that gets tossed into the queue
	        priorityQueue.add(new Node("temp",1,null,null));
        }
        while(priorityQueue.size() > 1){
	        final Node left = priorityQueue.poll();
	        final Node right = priorityQueue.poll();
	        final Node parent = new Node("Combined Parent Node", left.frequency+right.frequency, left, right);
	        priorityQueue.add(parent);
        }
        return priorityQueue.poll();
   }

   public HashMap<String,Integer> makeFrequencyTable(String[] elementArray){
	    HashMap<String, Integer> frequencyTable = new HashMap<>();
	    for(String element: elementArray){
	        frequencyTable.merge(element, 1, Integer::sum);
       }
       return frequencyTable;
   }


   public void decode(String inputFile, String outputFile, String freqFile){
		// TODO: Read Frequency file in and use it to decode
        // TODO:
   }



   public static void main(String[] args) {
	    Huffman  huffman = new HuffmanSubmit();
	    huffman.encode("a:a:b:c:d:e:f:g:g:g",null,null);
	    /*
      Huffman  huffman = new HuffmanSubmit();
		huffman.encode("ur.jpg", "ur.enc", "freq.txt");
		huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same.
		// On linux and mac, you can use `diff' command to check if they are the same.
		*/
   }

}

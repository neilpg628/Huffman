//Implements the HuffmanCode object, which creates a Huffman Compression code from a frequency table or a .code file or decompresses a given file using a given code

import java.util.*;
import java.io.*;

//Implementation of HuffmanCode object
public class HuffmanCode {

	//PriorityQueue that stores the HuffmanNodes	
	private Queue<HuffmanNode> q;
	
	//This constructor initializes a new HuffmanCode object from an array of frequencies.
	//frequencies is an array of frequencies where frequences[i] 
	//is the count of the character with ASCII value i. 
	public HuffmanCode(int[] frequencies) {
	
		q = new PriorityQueue<>();
		
		for(int i = 0; i < frequencies.length; i++)
			if(frequencies[i] > 0)
				q.add(new HuffmanNode(i, frequencies[i]));
	
		while(q.size() > 1) {
			HuffmanNode n1 = q.remove();
			HuffmanNode n2 = q.remove();
			
			q.add(new HuffmanNode(0, n1.frequency + n2.frequency, n1, n2));
		}
	}
	
	//This constructor initializes a new HuffmanCode object 
	//by reading in a previously constructed code from a .code file.
	//.code files store the ASCII value of a character and the Huffman code
	//for that character on consecutive lines. 
	//Requires a Scanner for the .code file.
	public HuffmanCode(Scanner input) {
		
		q = new PriorityQueue<>();
		HuffmanNode overallRoot = new HuffmanNode();
		
		while(input.hasNextLine()) {
			int ch = Integer.parseInt(input.nextLine());
			String code = input.nextLine();
			
			HuffmanNode current = overallRoot;
			
			while(code.length() != 0) {
				
				if(code.charAt(0) == '0') {
					if(current.left == null) current.left = new HuffmanNode();
					current = current.left;
				}
			
				else {
					if(current.right == null) current.right = new HuffmanNode();
					current = current.right;
				}
				
				code = code.substring(1);
			}
		
			current.character = ch;
			current.frequency = 0;
		}
		
		q.add(overallRoot);
	}
	
	//This private helper method stores the current Huffman codes to the given output stream 
	//in the .code format
	private void save(PrintStream output, HuffmanNode node, String code) {
		
		if(node != null) {
			if(node.character != 0) {
				output.println(node.character);
				output.println(code);
			}
			
			else {
				save(output, node.left, code + "0");
				save(output, node.right, code + "1");
			}
		}
	}
	
	//This public method stores the current Huffman codes to the given output stream 
	//in the .code format
	//.code files store the ASCII value of a character and the HuffmanCode
	//for that character on consecutive lines. 
	public void save(PrintStream output) {
		save(output, q.peek(), "");
	}
	
	//This method reads individual bits from a passed BitInputStream of a compressed file
	//and writes the corresponding characters to the passed PrintStream.
	//It stops reading when the BitInputStream is empty.
	public void translate(BitInputStream input, PrintStream output) {
	
		while(input.hasNextBit()) {
			HuffmanNode current = q.peek();
			while(current.character == 0) {
				if(input.nextBit() == 0) current = current.left;
				else current = current.right;
			}
			
			output.write((char) current.character);
		}
	}

	//This is a private class that implements the HuffmanNode object
	//Also implements Comparable
	private static class HuffmanNode implements Comparable<HuffmanNode> {
		
		//ASCII value of a character		
		public int character;
		public int frequency;
		
		//The left and right sides of the node
		public HuffmanNode left;
		public HuffmanNode right;
		
		//Constructs a HuffmanNode given a frequency and character
		public HuffmanNode(int character, int frequency) {
			this(character, frequency, null, null);
		}
		
		//Constructs a default HuffmanNode
		public HuffmanNode() {
			this(0, 0, null, null);
		}
		
		//Constructs a HuffmanNode with a frequency, character and left and right sides
		public HuffmanNode(int character, int frequency, HuffmanNode left, HuffmanNode right) {
			this.character = character;
			this.frequency = frequency;
			this.left = left;
			this.right = right;
		}
	
		//HuffmanNode compareTo method
		//When used by a sorting algorithm, lower frequencies will be put ahead of higher ones
		public int compareTo(HuffmanNode node) {
			return frequency - node.frequency;
		}
	}
}

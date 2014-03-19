package autocorrect;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author mjhart
 * 
 * This generates suggestions based on the Levinshtein Edit
 * Distance
 *
 */
public class LEDGenerator implements Generator {
	
	private Trie trie;
	private int maxCost;
	
	/**
	 * Returns a new LEDGenerator
	 * 
	 * @param trie
	 * @param maxCost
	 */
	public LEDGenerator(Trie trie, int maxCost) {
		this.trie = trie;
		this.maxCost = maxCost;
	}

	@Override
	public List<String> suggest(String word) {
		List<String> results = new LinkedList<String>(); // create result list
		
		if(word == null) {
			return results;
		}
		
		//create first row in edit distance matrix
		int[] row = new int[word.length()+1]; 
		for(int i=0; i<=word.length(); i++) {
			row[i] = i;
		}
		
		// call suggestHelper on each child of root
		for(Entry<Character, TrieNode> node : trie.getRoot().getChildren()) {
			suggestHelper(node.getValue(), word, new StringBuilder(), results, row);
		}
		return results;
	}
	
	/**
	 * This method does most of the work in generating suggestions within the specified 
	 * edit distance of the given word. Gets recursively called through the trie until
	 * a node has no children or the number of edits to get to a node is greater than
	 * the specified limit.
	 * 
	 * @param node
	 * @param word
	 * @param prefix
	 * @param results
	 * @param prevRow
	 */
	private void suggestHelper(TrieNode node, String word, StringBuilder prefix, List<String> results, int[] prevRow) {
		
		prefix.append(node.c); // add letter at current node to prefix being built
		
		int[] newRow = new int[word.length()+1]; // create next row
		newRow[0] = prevRow[0]+1; // first item always one more than prev first item
		
		for(int i=1; i<=word.length(); i++) {
			int insertCost = newRow[i-1]+1; // insert cost 
			int deleteCost = prevRow[i] + 1; // delete cost
			
			int replaceCost;
			if (word.charAt(i - 1) == node.c) {
	            replaceCost = prevRow[i - 1]; // no cost to replace if correct letter
	        } else {
	            replaceCost = prevRow[i - 1] + 1; // else add one
	        }
	        newRow[i] = min(insertCost, deleteCost, replaceCost);
	    }
		// add to list if is a word and has small enough cost
	    if (newRow[newRow.length - 1] <= this.maxCost && node.isWord()) {
	        results.add(prefix.toString());
	    }
	    
	    // recur on all children if all entries are not above max distance
	    for(int i=0; i<newRow.length; i++) {
		    if (newRow[i] <= this.maxCost) {
				for(Entry<Character, TrieNode> child : node.getChildren()) {
					suggestHelper(child.getValue(), word, new StringBuilder(prefix), results, newRow);
				}
		        break;
		    }
	    }
	}
	
	// retruns min of a, b, and c
	private int min(int a, int b, int c) {
		if(a > b) {
			a = b;
		}
		if(a > c) {
			a = c;
		}
		return a;
	}

}

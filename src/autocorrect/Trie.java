package autocorrect;

import java.io.FileNotFoundException;

/**
 * @author mjhart
 *	Models a Trie using TrieNodes internally. 
 */
public class Trie {
	
	private TrieNode root;
	
	/**
	 * Instantiates an empty Trie
	 */
	public Trie() {
		root = new TrieNode(' ');
	}
	
	/**
	 * Inserts a word into the trie
	 * @param word
	 */
	public TrieNode insertOrGet(String word) {
		TrieNode node = root;
		int i = 0;
		int n = word.length();
		
		while(i<n) {
			if(node.getChild(word.charAt(i)) != null) {
				node = node.getChild(word.charAt(i));
				i++;
			}
			else {
				break;
			}
		}
		if(i==n && node.isWord()) {
			return node;
		}
		while(i<n) {
			node.insertChild(new TrieNode(word.charAt(i)));
			node = node.getChild(word.charAt(i));
			i++;
		}
		node.setWord(true);
		return node;
	}
	
	public TrieNode getRoot() {
		return this.root;
	}
	
	
	/**
	 * Checks if this trie contains word
	 * @param word
	 * @return boolean
	 */
	public boolean contains(String word) {
		TrieNode node = this.root;
		for(int i=0; i<word.length(); i++) {
			if(node.getChild(word.charAt(i)) != null) {
				node = node.getChild(word.charAt(i));
			}
			else return false;
		}
		if(node.isWord()) return true;
		else return false;
	}
	
	public TrieNode getNode(String word) {
		TrieNode node = this.root;
		for(int i=0; i<word.length(); i++) {
			if(node.getChild(word.charAt(i)) != null) {
				node = node.getChild(word.charAt(i));
			}
			else return null;
		}
		if(node.isWord()) return node;
		else return null;
	}
}

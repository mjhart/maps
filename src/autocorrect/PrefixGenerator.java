package autocorrect;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class PrefixGenerator implements Generator {
	private Trie trie;
	
	/**
	 * A generator which suggests words of which the given
	 * word is a prefix
	 * 
	 * @param trie
	 */
	public PrefixGenerator(Trie trie) {
		this.trie = trie;
	}

	@Override
	public List<String> suggest(String word) {
		List<String> results = new LinkedList<String>();
		if(word == null) {
			return results;
		}
		int i = 0;
		int n = word.length();
		TrieNode node = trie.getRoot();
		
		// find word in trie
		while(i<n) {
			if(node.getChild(word.charAt(i)) != null) { // if letter is in node children
				node = node.getChild(word.charAt(i)); // continue to child
				i++;
			}
			else {
				return results; // return empty list if word is not in trie
			}
		}
		StringBuilder str = new StringBuilder(word); 
		for(Entry<Character, TrieNode> child : node.getChildren()) {
			buildWordList(results, new StringBuilder(str), child.getValue());
		}
		return results;
	}
	
	/* prefix suggestion helper method */
	private void buildWordList(List<String> results, StringBuilder str, TrieNode node) {
		
		// append letter at node to string being built
		str.append(node.c);
		
		// if this node completes a word add it to list
		if(node.isWord()) {
			results.add(str.toString());
		}
		
		// recursively call on all children
		for(Entry<Character, TrieNode> child : node.getChildren()) {
			buildWordList(results, new StringBuilder(str), child.getValue());
		}
	}

}

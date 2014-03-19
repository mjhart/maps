package autocorrect;

import java.util.LinkedList;
import java.util.List;

/**
 * @author mjhart
 * 
 * A generator which splits strings into two vaild words 
 * and returns those words in a list
 *
 */
public class WhiteSpaceGen implements Generator {

	private Trie trie;
	
	public WhiteSpaceGen(Trie trie) {
		this.trie = trie;
	}
	
	@Override
	public List<String> suggest(String word) {
		List<String> resultSet = new LinkedList<String>();
		
		if(word == null) { // return empty if null input
			return resultSet;
		}
		
		TrieNode node = trie.getRoot();
		for(int i=0; i<word.length(); i++) { // iterate through word
			if(node.getChild(word.charAt(i)) != null) { // if child exists
				node = node.getChild(word.charAt(i));
				if(node.isWord()) { // if at a word
					if(trie.contains(word.substring(i+1))) { // check if rest of string is a word
						resultSet.add(String.format("%s %s", word.substring(0, i+1), word.substring(i+1)));
					}
				}
			}
			else break;
		}
		return resultSet;
	}

}

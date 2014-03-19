package autocorrect;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class TrieNode {
	public final char c;
	private HashMap<Character,TrieNode> children;
	private boolean isWord;
	private int count;
	private String data;
	
	/**
	 * Creates a new TrieNode containing the character c
	 * @param c
	 */
	public TrieNode(char c) {
		this.c = c;
		children = new HashMap<Character,TrieNode>();
		count = 0;
	}
	
	/**
	 * @returns a Set of this nodes children
	 */
	public Set<Entry<Character, TrieNode>> getChildren() {
		return children.entrySet();
	}
	
	/**
	 * @return number of children in node
	 */
	public int numChildren() {
		return children.size();
	}
	
	/**
	 * Inserts a TrieNode as this nodes child
	 * using the passed node's character as
	 * a key
	 * @param node
	 */
	public void insertChild(TrieNode node) {
		children.put(node.c, node);
	}

	public boolean isWord() {
		return isWord;
	}

	public void setWord(boolean isWord) {
		this.isWord = isWord;
	}
	
	public TrieNode getChild(char c) {
		return children.get(c);
	}
	
	public int getCount() {
		return count;
	}
	
	public void incrementCount() {
		count++;
	}
	
	public String getData() {
		return data;
	}
	
	public void concatData(String newData) {
		if(data==null) {
			data=newData;
		}
		else {
			data+=",";
			data+=newData;
		}
	}
	
	@Override
	public String toString() {
		return String.format("String: %s numChildren: %d isWord: %b", this.c, this.numChildren(), this.isWord);
	}
}

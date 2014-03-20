package autocorrect;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mjhart
 * 
 * Top level class that runs the application in
 * either a gui or the command line
 *
 */
public class TrieEngine {
	
	private Trie _trie;
	private Generator _preGen;
	private Generator _wsGen;
	private Generator _ledGen;
	private Ranker _ranker;
	
	/**
	 * Parses the arguments given and starts the application with
	 * the given settings
	 * 
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public TrieEngine(String indexFile) throws FileNotFoundException {
		_trie = new Trie();
		_preGen = new PrefixGenerator(_trie);
		_wsGen = new WhiteSpaceGen(_trie);
		_ledGen = new LEDGenerator(_trie, 2);
		_ranker = new SimpleRanker(_trie);
		new IndexParser(indexFile, _trie);
		
	}
	
	/**
	 * Gets suggestions from the generators
	 * 
	 * @param A street name
	 * @return A list of street names suggested
	 */
	public List<String> getSuggestion(String street) {
		String query = formatStreet(street);
		HashSet<String> results = new HashSet<String>();
		
		// get suggestions
		results.addAll(_preGen.suggest(query));
		results.addAll(_wsGen.suggest(query));
		results.addAll(_ledGen.suggest(query));
		
		// rank suggestions
		List<String> list = new LinkedList<String>(results);
		_ranker.rank(list, query);
		
		// return result
		return list;
	}
	
	/**
	 * Looks up the list of nodes associated with each street
	 * and compares the two lists to find a common node.
	 * 
	 * @param street1
	 * @param street2
	 * @return
	 */
	public String getIntersection(String street1, String street2) {
		
		// get the string of nodes stored at each trie node
		String list1 = _trie.getNode(formatStreet(street1)).getData();
		String list2 = _trie.getNode(formatStreet(street2)).getData();
		
		if(list1 == null || list2 == null) {
			return null;
		}
		
		// turn into arrays
		String[] nodes1 = list1.split(",");
		String[] nodes2 = list2.split(",");
		
		// add strings in list1 to hashset for comparison
		HashSet<String> set = new HashSet<String>();
		
		for(String n1 : nodes1) {
			set.add(n1);
		}
		
		// get intersection
		for(String n2 : nodes2) {
			if(set.contains(n2)) {
				return n2;
			}
		}
		return null;
	}
	
	/**
	 * Formats a street name in the way they 
	 * are stored in the trie. New words start 
	 * with uppercase letters.
	 * 
	 * @param street
	 * @return
	 */
	private String formatStreet(String street) {
		StringBuilder sb = new StringBuilder();
		if(street.length() == 0) {
			return sb.toString();
		}
		sb.append(Character.toUpperCase(street.charAt(0)));
		for(int i=1; i<street.length(); i++) {
			if(street.charAt(i)==' ') {
				while(street.charAt(i)==' ') {
					sb.append(' ');
					i++;
					if(i==street.length()) {
						return sb.toString();
					}
				}
				sb.append(Character.toUpperCase(street.charAt(i)));
			}
			else {
				sb.append(Character.toLowerCase(street.charAt(i)));
			}
		}
		return sb.toString();
	}
	
	
}

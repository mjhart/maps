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
	
	public List<String> getSuggestion(String query) {
		LinkedList<String> results = new LinkedList<String>();
		results.addAll(_preGen.suggest(query));
		results.addAll(_wsGen.suggest(query));
		results.addAll(_ledGen.suggest(query));
		_ranker.rank(results, query);
		return results;
	}
	
	public String getIntersection(String street1, String street2) {
		
		
		String list1 = _trie.getNode(street1).getData();
		String list2 = _trie.getNode(street2).getData();
		
		if(list1 == null || list2 == null) {
			return null;
		}
		
		String[] nodes1 = list1.split(",");
		String[] nodes2 = list2.split(",");
		
		HashSet<String> set = new HashSet<String>();
		
		for(String n1 : nodes1) {
			set.add(n1);
		}
		
		for(String n2 : nodes2) {
			if(set.contains(n2)) {
				return n2;
			}
		}
		return null;
	}
}

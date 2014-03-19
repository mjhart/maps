package autocorrect;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleRanker implements Ranker {
	private Trie _trie;
	
	public SimpleRanker(Trie trie) {
		_trie = trie;
	}

	@Override
	public void rank(List<String> list, String current) {
		Collections.sort(list, new MyComparator());
		
	}
	
	private class MyComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			return Integer.compare(_trie.getNode(o2).getCount(), _trie.getNode(o1).getCount());
		}
		
	}
	
}

package trieTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import autocorrect.*;


public class TrieTester {

	@Test
	public void importTest() throws FileNotFoundException {
		Trie trie = new Trie();
		new IndexParser("smallindex.tsv", trie);
		assertTrue(trie.contains("Brown Street"));
		assertFalse(trie.contains("NOT A STREET"));
	}
	
	@Test
	public void countTest() throws FileNotFoundException {
		Trie trie = new Trie();
		new IndexParser("smallindex.tsv", trie);
		assertTrue(trie.getNode("Brown Street").getCount() == 15);
	}
	
	@Test
	public void dataTest() throws FileNotFoundException {
		Trie trie = new Trie();
		new IndexParser("smallindex.tsv", trie);
		assertTrue(trie.getNode("Cady Street").getData().equals("/n/4182.7140.201401194,/n/4182.7140.201507820,/n/4182.7140.201401194,/n/4182.7140.201507820"));
	}
	
	@Test
	public void suggestTest() throws FileNotFoundException {
		TrieEngine eng = new TrieEngine("smallindex.tsv");
		assertTrue(eng.getSuggestion("Brow").get(0).equals("Brown University"));
	}
	
	@Test
	public void intersectTest() throws FileNotFoundException {
		TrieEngine eng = new TrieEngine("smallindex.tsv");
		assertTrue(eng.getIntersection("Charlesfield Street", "Brown Street").equals("/n/4182.7140.201515126"));
	}
	
	@Test
	public void largeTest() throws FileNotFoundException {
		TrieEngine eng = new TrieEngine("/course/cs032/data/maps/index.tsv");
	}
	
}

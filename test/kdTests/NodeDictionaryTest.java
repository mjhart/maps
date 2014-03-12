package kdTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import kdTree.KDTree;

import newGraph.Node;
import newGraph.NodeDictionary;

import org.junit.Test;

public class NodeDictionaryTest {

	@Test
	public void getNodeTest() throws FileNotFoundException {
		NodeDictionary dict = new NodeDictionary("small_nodes.tsv");
		assertTrue(dict.getNode("/n/4015.7374.527767659").getId().equals("/n/4015.7374.527767659"));
		
	}
	@Test
	public void getWayIdsTest() throws FileNotFoundException {
		NodeDictionary dict = new NodeDictionary("small_nodes.tsv");
		List<String> result = dict.getWayIds("/n/4015.7374.527767659");
		assertTrue(result.size() == 1);
		assertTrue(result.get(0).equals("/w/4015.7374.42295268.0.1"));
	}

}
package kdTests;

import static org.junit.Assert.*;

import graph.Node;
import graph.NodeDictionary;

import java.io.FileNotFoundException;
import java.util.List;

import kdTree.KDTree;

import org.junit.Test;

public class KDTreeTest {

	@Test
	public void insertTest() throws Exception {
		KDTree<Node> tree = new KDTree<Node>(2);
		NodeDictionary dict = new NodeDictionary("small_nodes.tsv");
		tree.addAll(dict.nodeList());
	}
	
	@Test
	public void nearestNeighborTest() throws Exception {
		KDTree<Node> tree = new KDTree<Node>(2);
		NodeDictionary dict = new NodeDictionary("small_nodes.tsv");
		tree.addAll(dict.nodeList());
		double[] coords = {40.0, -73.75};
		List<Node> results = tree.nearestNeighbors(coords, 1);
		assertTrue(results.size() == 1);
		assertTrue(results.get(0).getId().equals("/n/4015.7374.527767659"));
	}
	
	@Test
	public void boundingBoxTest() throws Exception {
		KDTree<Node> tree = new KDTree<Node>(2);
		NodeDictionary dict = new NodeDictionary("small_nodes.tsv");
		tree.addAll(dict.nodeList());
		double[] max = {40.16, -73.748};
		double[] min = {40.15, -73.75};
		List<Node> results = tree.withinBox(max, min);
		assertTrue(results.size() == 1);
		assertTrue(results.get(0).getId().equals("/n/4015.7374.527767659"));
	}


}

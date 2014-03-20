package kdTests;

import static org.junit.Assert.*;

import graph.NodeDictionary;
import graph.WayDictionary;

import java.io.FileNotFoundException;


import org.junit.Before;
import org.junit.Test;

public class WayDictionaryTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws Exception {
		NodeDictionary nodes = new NodeDictionary("small_nodes.tsv");
		WayDictionary ways = new WayDictionary("small_ways.tsv", nodes);
		assertTrue(ways.getWay("/w/4016.7374.42295268.4.1").getFilm().equals("/w/4016.7374.42295268.4.1"));
	}

}

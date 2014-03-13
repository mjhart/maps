package kdTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import newGraph.NodeDictionary;
import newGraph.WayDictionary;

import org.junit.Before;
import org.junit.Test;

public class WayDictionaryTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws FileNotFoundException {
		NodeDictionary nodes = new NodeDictionary("small_nodes.tsv");
		WayDictionary ways = new WayDictionary("small_ways.tsv", nodes);
		assertTrue(ways.getWay("/w/4016.7374.42295268.4.1").getFilm().equals("/w/4016.7374.42295268.4.1"));
	}

}

package baconTests;

import org.junit.Test;
import static org.junit.Assert.*;
import newGraph.*;
import parsers.*;

public class BaconTests {
	
	@Test
	public void baconTest1() throws Exception{
		NodeParser np = new NodeParser("smallnodes.tsv");
		WayParser wp = new WayParser("smallways.tsv");
		Astar astar = new Astar();
		Graph g  = new Graph();
		astar.printSearch(g, "/n/4182.7140.201312088", "/n/4182.7140.201260632", wp, np);
	}

}

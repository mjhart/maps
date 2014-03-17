package baconTests;

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;
import newGraph.*;
import parsers.*;

public class BaconTests {
	
	@Test
	public void baconTest1() throws Exception{
		//NodeParser np = new NodeParser("smallnodes.tsv");
		//WayParser wp = new WayParser("smallways.tsv");
		Astar astar = new Astar("smallnodes.tsv","smallways.tsv");
		//Astar astar = new Astar("/course/cs032/data/maps/nodes.tsv", "/course/cs032/data/maps/ways.tsv");
		Graph g  = new Graph();
		//astar.printSearch(g, "/n/4182.7140.201312088", "/n/4182.7140.201260632", wp, np);
		ArrayList<Node> path = (ArrayList<Node>) astar.getPath("/n/4182.7140.201312088", "/n/4182.7140.201260632");
		ArrayList<Edge> edges = new ArrayList();
		for(int i = 0; i < path.size()-2; i++){
			for(Edge e: path.get(i).getEdges()){
				if(e.getDest().equals(path.get(i+1))){
					System.out.println(e.getSource()+" -> "+e.getDest()+" : "+e.getFilm());
					edges.add(e);
					break;
				}
			}
		}
	}

}

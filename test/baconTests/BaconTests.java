package baconTests;

import java.io.RandomAccessFile;
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
	
	@Test
	public void NodeParserTestIn() throws Exception{
		NodeParser np = new NodeParser("smallnodes.tsv");
		String[] test = np.search("/n/4182.7140.201260632", false);
		//System.out.println(test.length);
	}
	
	@Test
	public void NodeParserTestOut() throws Exception{
		NodeParser np = new NodeParser("smallnodes.tsv");
		String[] test = np.search("/n/4182.7139.201260632", false);
		assertNull(test);
	}
	
	@Test
	public void NodeParserTestFull() throws Exception{
		NodeParser np = new NodeParser("smallnodes.tsv");
		RandomAccessFile raf = new RandomAccessFile("smallnodes.tsv","r");
		int _idcol = Integer.MAX_VALUE;
		int _latcol = Integer.MAX_VALUE;
		int _loncol = Integer.MAX_VALUE;
		int _wayscol = Integer.MAX_VALUE;
		String[] header = raf.readLine().split("\t");
		for(int i=0; i<header.length; i++) {
			if(header[i].equals("id")) {
				_idcol = i;
			}
			if(header[i].equals("latitude")) {
				_latcol = i;
			}
			if(header[i].equals("longitude")) {
				_loncol = i;
			}
			if(header[i].equals("ways")) {
				_wayscol = i;
			}
		}
		if(_idcol==Integer.MAX_VALUE || _wayscol==Integer.MAX_VALUE || _latcol==Integer.MAX_VALUE || _loncol==Integer.MAX_VALUE){
			System.err.println("ERROR: Improper Columns in Nodes file");
			System.exit(1);
		}
		while(raf.getFilePointer()<raf.length()){
			String[] line = raf.readLine().split("\t");
			String id = line[_idcol];
			try{
				if(line.length==np.getCols()){
					assertNotNull(np.search(id, false));
				}
				else{
					assertNull(np.search(id, false));
				}
			}
			catch(AssertionError e){
				System.out.println(id);
				System.exit(1);
			}
		}
	}

}
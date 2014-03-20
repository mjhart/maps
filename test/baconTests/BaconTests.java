package baconTests;

import graph.*;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;


import org.junit.Test;

import static org.junit.Assert.*;
import parsers.*;

public class BaconTests {
	
	//@Test
	public void getBorsTest() throws Exception{
		Astar astar = new Astar("smallnodes.tsv","smallways.tsv");
		NodeParser np =  astar.getNp();
		WayParser wp = astar.getWp();
		String[] wayids = np.search("/n/4182.7140.1094902259", false);
		assertNotNull(wayids);
		//System.out.println(wayids.length);
		assertTrue(wayids.length==4);
		for(String s: wayids){
			System.out.println("Examining way: "+s);
			String[] wayinfo = wp.search(s);
			if(wayinfo!=null){
				assertTrue(wayinfo.length==3);
				if(wayinfo[1].equals("/n/4182.7140.1094902259")){
					System.out.println("Examining dst node: "+wayinfo[2]);
					String[] dlatlon = np.search(wayinfo[2], true);
					assertNotNull(dlatlon);
					assertTrue(dlatlon.length==2);
				}
			}
		}
	}
	
	@Test
	public void astarTest1() throws Exception{
		//NodeParser np = new NodeParser("smallnodes.tsv");
		//WayParser wp = new WayParser("smallways.tsv");
		Astar astar = new Astar("smallnodes.tsv","smallways.tsv");
		//Astar astar = new Astar("/course/cs032/data/maps/nodes.tsv", "/course/cs032/data/maps/ways.tsv");
		Graph g  = new Graph();
		//astar.printSearch(g, "/n/4182.7140.201312088", "/n/4182.7140.201260632", wp, np);
		List<Node> path = astar.getPath("/n/4182.7140.201312088", "/n/4182.7140.201260636");
		for(int i = 0; i < path.size()-1; i++){
			for(Edge e: path.get(i).getEdges()){
				if(e.getDest().equals(path.get(i+1))){
					System.out.println(e.getSource()+" -> "+e.getDest()+" : "+e.getFilm());
					break;
				}
			}
		}
	}
	
	@Test
	public void astarTest2() throws Exception{
		//NodeParser np = new NodeParser("smallnodes.tsv");
		//WayParser wp = new WayParser("smallways.tsv");
		Astar astar = new Astar("smallnodes.tsv","smallways.tsv");
		//Astar astar = new Astar("/course/cs032/data/maps/nodes.tsv", "/course/cs032/data/maps/ways.tsv");
		Graph g  = new Graph();
		//astar.printSearch(g, "/n/4182.7140.201312088", "/n/4182.7140.201260632", wp, np);
		ArrayList<Node> path = (ArrayList<Node>) astar.getPath("/n/4182.7140.201312088", "/n/4182.7140.2100936248");
		ArrayList<Edge> edges = new ArrayList();
		for(int i = 0; i < path.size()-1; i++){
			for(Edge e: path.get(i).getEdges()){
				if(i==path.size()-1){
					if(e.getDest().equals("/n/4182.7140.2100936248")){
						System.out.println(e.getSource()+" -> "+e.getDest()+" : "+e.getFilm());
						edges.add(e);
						break;
					}
				}
				else{
					if(e.getDest().equals(path.get(i+1))){
						System.out.println(e.getSource()+" -> "+e.getDest()+" : "+e.getFilm());
						edges.add(e);
						break;
					}
				}
			}
		}
	}
	
	//@Test
	public void astarTest1reversed() throws Exception{
		//NodeParser np = new NodeParser("smallnodes.tsv");
				//WayParser wp = new WayParser("smallways.tsv");
				//Astar astar = new Astar("smallnodes.tsv","smallways.tsv");
				Astar astar = new Astar("/course/cs032/data/maps/nodes.tsv", "/course/cs032/data/maps/ways.tsv");
				Graph g  = new Graph();
				//astar.printSearch(g, "/n/4182.7140.201312088", "/n/4182.7140.201260632", wp, np);
				ArrayList<Node> path = (ArrayList<Node>) astar.getPath("/n/4182.7140.201260642", "/n/4182.7140.201312088");
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
	
	//@Test
	public void NodeParserTestLastLine() throws Exception{
		NodeParser np = new NodeParser("smallnodes.tsv");
		String[] test = np.search("/n/4182.7140.357257904", false);
		assertNotNull(test);
	}
	
	//@Test
	public void WayParserTestIn() throws Exception{
		WayParser wp = new WayParser("smallways.tsv");
		String[] test = wp.search("/w/4182.7140.90092610.0.1");
		assertNotNull(test);
	}
	
	
	//@Test
	public void NodeParserTestIn() throws Exception{
		NodeParser np = new NodeParser("smallnodes.tsv");
		String[] test = np.search("/n/4182.7140.201260632", false);
		assertNotNull(test);//System.out.println(test.length);
	}
	
	//@Test
	public void NodeParserTestOut() throws Exception{
		NodeParser np = new NodeParser("smallnodes.tsv");
		String[] test = np.search("/n/4182.7139.201260632", false);
		assertNull(test);
	}
	
	//@Test
	public void NodeParserTestCol() throws Exception{
		NodeParser np = new NodeParser("smallnodes.tsv");
		String[] test = np.search("/n/4182.7140.366687983", false);
		assertNotNull(test);//System.out.println(test.length);
	}
	
	//@Test
	public void WayParserTestFull() throws Exception{
		WayParser wp = new WayParser("smallways.tsv");
		RandomAccessFile raf = new RandomAccessFile("smallways.tsv","r");
		int _idcol = Integer.MAX_VALUE;
		int _namecol = Integer.MAX_VALUE;
		int _srccol = Integer.MAX_VALUE;
		int _dstcol = Integer.MAX_VALUE;
		String[] header = raf.readLine().split("\t");
		for(int i=0; i<header.length; i++) {
			if(header[i].equals("id")) {
				_idcol = i;
			}
			if(header[i].equals("start")) {
				_srccol = i;
			}
			if(header[i].equals("end")) {
				_dstcol = i;
			}
			if(header[i].equals("name")){
				_namecol = i;
			}
		}
		if(_idcol==Integer.MAX_VALUE || _namecol==Integer.MAX_VALUE || _srccol==Integer.MAX_VALUE || _dstcol==Integer.MAX_VALUE){
			System.err.println("ERROR: Improper Columns in Ways file");
			System.exit(1);
		}
		while(raf.getFilePointer()<raf.length()){
			String[] line = raf.readLine().split("\t");
			String id = line[_idcol];
			try{
				//if(line.length==np.getCols()){
				//System.out.println("SEARCHING FOR "+id);
				String[] nodeids = wp.search(id);
				assertNotNull(nodeids);
				assertTrue(nodeids.length==3);
				//}
				//else{
				//	for(int i = 0; i < line.length; i++){
				//		System.out.println("i: "+i+", "+line[i]);
				//	}
				//}
			}
			catch(AssertionError e){
				System.out.println(id);
				System.exit(1);
			}
		}
	}
	
	//@Test
	public void NodeParserTestFull() throws Exception{
		NodeParser np = new NodeParser("smallnodes.tsv");
		RandomAccessFile raf = new RandomAccessFile("smallnodes.tsv","r");
		int _idcol = Integer.MAX_VALUE;
		int _latcol = Integer.MAX_VALUE;
		int _loncol = Integer.MAX_VALUE;
		int _wayscol = Integer.MAX_VALUE;
		String[] header = raf.readLine().split("\t");
		int _cols = header.length;
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
				//if(line.length==np.getCols()){
				System.out.println("SEARCHING FOR "+id);
				String[] wayids = np.search(id, false);
				//System.out.println("aaaaaaaaaaaaaaaaaaaa");
				assertNotNull(wayids);
				//System.out.println("line length: "+line.length);
				if(line.length>=_cols){
					//System.out.println("above");
					assertTrue(wayids.length==line[_wayscol].split(",").length);
					//System.out.println("HERERERERERRE");
				}
				else{
					assertTrue(wayids.length==1);
				}
				String[] latlon = np.search(id, true);
				assertNotNull(latlon);
				assertTrue(latlon.length==2);
				//}
				//else{
				//	for(int i = 0; i < line.length; i++){
				//		System.out.println("i: "+i+", "+line[i]);
				//	}
				//}
			}
			catch(AssertionError e){
				System.out.println(id);
				System.exit(1);
			}
		}
	}

}
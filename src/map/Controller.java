package map;
import graph.Astar;
import graph.Edge;
import graph.Node;
import graph.NodeDictionary;
import graph.WayDictionary;
import gui.MainPanel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import autocorrect.TrieEngine;

import kdTree.KDTree;

public class Controller {
	private KDTree<Node> _tree;
	private WayDictionary _ways;
	private NodeDictionary _nodes;
	private TrieEngine _trie;
	private Astar _astar;
	
	public Controller(String wayFile, String nodeFile, String indexFile, boolean gui) {
		
		
		try {
			_nodes = new NodeDictionary(nodeFile);
			System.out.println("node dict built");
			_ways = new WayDictionary(wayFile, _nodes);
			System.out.println("here1");
			_trie = new TrieEngine(indexFile);
			System.out.println("here2");
			_astar = new Astar(nodeFile, wayFile);
		}
		catch(IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		_tree = new KDTree<Node>(2);
		_tree.addAll(_nodes.nodeList());
		
		
		if(gui) {
			startGUI();
		}
		else {
			startREPL();
		}
		
	}
	
	//TODO 
	private void startREPL() {
		// read from System.in
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String line = null;
		String[] commands;
		while(true) {

			// read line
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if(line == null || line.length() == 0) {
				return;
			}

			// split line 
			commands = line.split("\"");
			if(commands.length==1){// assuming input is valid, must be lat/lon lat/lon
				//System.out.println(line);
				commands = line.split(" ");
				//System.out.println(commands.length);
				if(commands.length==4){
					try{
						double slat = Double.parseDouble(commands[0]);
						double slon = Double.parseDouble(commands[1]);
						double dlat = Double.parseDouble(commands[2]);
						double dlon = Double.parseDouble(commands[3]);
						//System.out.println(slat);
						//System.out.println(slon);
						//System.out.println(dlat);
						//System.out.println(dlon);
						double[] src = {slon,slat};
						double[] dst = {dlon,dlat};
						Node source = this.nearestNeighbor(src);
						//System.out.println(source.toString());
						Node destination = this.nearestNeighbor(dst);
						try{
							List<Node> path = this.getPath(source.toString(), destination.toString());
							if(path!=null){
								for(int i = 0; i < path.size()-1; i++){
									for(Edge e: path.get(i).getEdges()){
										if(e.getDest().equals(path.get(i+1))){
											System.out.println(e.getSource()+" -> "+e.getDest()+" : "+e.getFilm());
											break;
										}
									}
								}
							}
							else{
								System.out.println(source.toString()+" -/- "+destination.toString());
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					catch(NumberFormatException e){
						System.err.println("ERROR: invalid lattidue/longitude value(s)");
						continue;
					}
				}
				else{
					System.err.println("ERROR: invalid number of lattitude/longitude pairs");
				}
			}
			else{
				if(commands.length==4){
					Node src = this.getIntersection(commands[0], commands[1]);
					Node dst = this.getIntersection(commands[2], commands[3]);
					try {
						List<Node> path = this.getPath(src.toString(), dst.toString());
						if(path!=null){
							for(int i = 0; i < path.size()-1; i++){
								for(Edge e: path.get(i).getEdges()){
									if(e.getDest().equals(path.get(i+1))){
										System.out.println(e.getSource()+" -> "+e.getDest()+" : "+e.getFilm());
										break;
									}
								}
							}
						}
						else{
							System.out.println(src.toString()+" -/- "+dst.toString());
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					System.out.println("ERROR: Invalid number of streets");
				}
			}
		}
			
			
	}
	
	private void startGUI() {
		new MainPanel(this);
	}
	
	public List<Node> getData(double[] max, double[] min, HashSet<Node> nodeSet, HashSet<Edge> waySet) {
		
		long time = 0;
		long time2 = 0;
		long time3 = 0-System.currentTimeMillis();
		
		//System.out.println("within box" + Arrays.toString(max) + " " + Arrays.toString(min));
		List<Node> nodes = _tree.withinBox(max, min);
		//System.out.println("Nodes loaded");
		
		int count = 0;
		
		_ways.calls++;
		_ways.nullWays = 0;
		
		
		for(Node n : nodes) {
			nodeSet.add(n);
			if(n.edgesLoaded()) {
				for(Edge e : n.getEdges()) {
					waySet.add(e);
				}
			}
			else {
				long start = System.currentTimeMillis();
				List<String> wayIds = _nodes.getWayIds(n.getId());
				long end = System.currentTimeMillis();
				time+=end;
				time-=start;
				for(String s : wayIds) {
					count++;
					long start2 = System.currentTimeMillis();
					Edge e = _ways.getWay(s);
					long end2 = System.currentTimeMillis();
					time2+=end2;
					time2-=start2;
					if(e != null) {
						waySet.add(e);
						n.addEdge(e);
					}
				}
			}
			
		}
		time3+=System.currentTimeMillis();
		//System.out.println("Null ways: " + _ways.nullWays);
		//System.out.println(String.format("Ways loaded - Disk: %d Mem: %d", _ways.toDisk, _ways.inMem));
		//System.out.println("Buffer read: " + _ways.bufferRead);
		System.out.println("Time spent getting way ids " + time);
		System.out.println("Time spend getting ways " + time2);
		System.out.println("Total time spent getting tile: " + time3);
		_ways.bufferRead = 0;
		_ways.toDisk = 0;
		_ways.inMem = 0;
		//System.out.println("Total ways searched: " + count);
		//System.out.println();
		return _tree.withinBox(max, min);
	}
	
	
	
	public Node nearestNeighbor(double[] coords) {
		List<Node> result = _tree.nearestNeighbors(coords, 1);
		if(result.size() == 1) {
			return result.get(0);
		}
		return null;
	}
	
	public List<String> getSuggestions(String query) {
		return _trie.getSuggestion(query);
	}
	
	public Node getIntersection(String street1, String street2) {
		String nodeId = _trie.getIntersection(street1, street2);
		if(nodeId != null) {
			return _nodes.getNode(nodeId);
		}
		return null;
	}
	
	public List<Node> getPath(String source, String dest) throws IOException {
		return _astar.getPath(source, dest);
	}
	
}

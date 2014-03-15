package tmp;

import gui.AltMainPanel;
import gui.MainPanel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import newGraph.Edge;
import newGraph.Node;
import newGraph.NodeDictionary;
import newGraph.WayDictionary;
import kdTree.KDTree;

public class Controller {
	private KDTree<Node> _tree;
	private WayDictionary _ways;
	private NodeDictionary _nodes;
	
	public Controller(String wayFile, String nodeFile, String indexFile, boolean gui) {
		
		try {
		_nodes = new NodeDictionary(nodeFile);
		_ways = new WayDictionary(wayFile, _nodes);
		}
		catch(IOException e) {
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
			commands = line.split(" ");
		}
			
			
	}
	
	private void startGUI() {
		new AltMainPanel(this);
	}
	
	public List<Node> getData(double[] max, double[] min, HashSet<Node> nodeSet, HashSet<Edge> waySet) {
		//System.out.println("within box" + max + " " + min);
		List<Node> nodes = _tree.withinBox(max, min);
		System.out.println("Nodes loaded");
		
		HashSet<String> found = new HashSet<String>();
		
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
				List<String> wayIds = _nodes.getWayIds(n.getId());
				for(String s : wayIds) {
					count++;
					Edge e = _ways.getWay(s);
					if(e != null) {
						waySet.add(e);
						n.addEdge(e);
					}
				}
			}
		}
		System.out.println("Null ways: " + _ways.nullWays);
		System.out.println(String.format("Ways loaded - Disk: %d Mem: %d", _ways.toDisk, _ways.inMem));
		System.out.println("Buffer read: " + _ways.bufferRead);
		_ways.bufferRead = 0;
		_ways.toDisk = 0;
		_ways.inMem = 0;
		System.out.println("Total ways searched: " + count);
		System.out.println();
		return _tree.withinBox(max, min);
	}
	
	public static void main(String[] args) {
		new Controller("smallways.tsv", "smallnodes.tsv", "small_nodes.tsv", true);
	}
}

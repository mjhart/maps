package tmp;

import gui.AltMainPanel;
import gui.MainPanel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import newGraph.Node;
import newGraph.NodeDictionary;
import newGraph.WayDictionary;
import kdTree.KDTree;

public class Controller {
	private KDTree<Node> tree;
	private WayDictionary ways;
	private NodeDictionary nodes;
	
	public Controller(String wayFile, String nodeFile, String indexFile, boolean gui) {
		
		try {
		nodes = new NodeDictionary(nodeFile);
		ways = new WayDictionary(wayFile, nodes);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		tree = new KDTree<Node>(2);
		tree.addAll(nodes.nodeList());
		
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
		System.out.println("here");
	}
	
	public List<Node> nodeBoundingBox(double[] max, double[] min) {
		//System.out.println("within box" + max + " " + min);
		return tree.withinBox(max, min);
	}
	
	public static void main(String[] args) {
		new Controller("smallways.tsv", "smallnodes.tsv", "small_nodes.tsv", true);
	}
}

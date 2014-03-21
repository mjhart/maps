package graph;

/****
 * This class is for our Astar search.  It is a simple implementation of Astar
 * that calls getBors when necessary to find neighbors from the data set.
 * 
 * Everytime Astar search is called, it throws out the old graph and starts over.
 * 
 * 
 */


import graph.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import parsers.NodeParser;
import parsers.WayParser;

public class Astar {
	
	private Graph _graph;
	private WayParser _wp;
	private NodeParser _np;
	
	public Astar(String nodespath, String wayspath) throws Exception{
		_graph = new Graph();
		_wp = new WayParser(wayspath);
		_np = new NodeParser(nodespath);
	}
	
	public List<Node> getPath(String src, String dst) throws IOException{
		_graph = new Graph();
		List<Node> list = this.search(_graph, src, dst, _wp, _np);
		/*if(list!=null){
			for(Node n: list){
				System.out.println(n.toString());
			}
		}*/
		
		return list;
	}
	
	public void printSearch(Graph g, String src, String dst, WayParser wp, NodeParser np) throws IOException{
		List<Node> list = this.search(g, src, dst, wp, np);
		for(Node n: list){
			System.out.println(n.toString());
		}
	}
	
	public List<Node> search(Graph g, String src, String dst, WayParser wp, NodeParser np) throws IOException{
		//System.out.println("Searching for path between "+src+" and "+dst);
		String[] latlon = np.search(src, true);
		if(latlon==null){
			return null;
		}
		g.insertNode(src, Double.parseDouble(latlon[0]), Double.parseDouble(latlon[1]));
		Node _src = g.findNode(src);
		if(_src!=null){
			PriorityQueue<Node> open = new PriorityQueue<Node>(20, new NodeComparator());
			Map<String, Node> close = new HashMap<String, Node>();
			//Map<Node, Double> gdist = new HashMap<Node, Double>();
			open.add(_src);
			//gdist.put(_src,0.0);
			Node goal = null;
			while(open.size() > 0){
				Node current = open.poll();
				if(current.toString().equals(dst)){
					goal = current;
					break;
				}
				close.put(current.toString(), current);
				List<Node> neighbors = getBors(current, wp, np, g);
				for(Node n : neighbors){
					//if(!gdist.containsKey(n)) gdist.put(n,getD(current,n));
					//double cost = gdist.get(current)+getD(current,n);
					double cost = current.getG() + getD(current, n);
					if(open.contains(n) && cost < n.getG()){//gdist.get(n)){//n.getG()){
						open.remove(n);
					}
					if(!open.contains(n) && cost < n.getG()){//gdist.get(n)){//n.getG()){
						close.remove(n.toString());
					}
					if(!open.contains(n) && !close.containsKey(n.toString())){
						n.setG(cost);//gdist.put(n,cost);//n.setG(cost);
						n.setH(this.getD(_src, n));
						open.add(n);
						n.setPrev(current);
					}
				}
			}
			
			
			
			if(goal != null){
				Stack<Node> stack = new Stack<Node>();
				List<Node> list = new ArrayList<Node>();
				stack.push(goal);
				Node parent = goal.getPrev();
				while(parent!=null){
					stack.push(parent);
					parent = parent.getPrev();
				}
				while(stack.size()>0){
					list.add(stack.pop());
				}
				return list;
			}
			else{
				System.out.println("Goal was Null");
				return null;
			}
		}
		else{
			System.out.println("Start was Null");
			return null;
		}
		//return null;
	}
	
	public List<Node> getBors(Node source, WayParser wp, NodeParser np, Graph g) throws IOException{
		List<Node> list = new ArrayList<Node>();
		String[] ways = np.search(source.toString(), false);
		//System.out.println("Finding bors for "+source.toString());
		if(ways!=null){
			for(String w : ways){
				//System.out.println("Way "+w);
			}
			for(String w : ways){
				//System.out.println("Way "+w);
				String[] nodes = wp.search(w);
				//System.out.println("Nodes length: "+nodes.length);
				int counter = 0;
				if(nodes!=null){
					//System.out.println("Nodes "+nodes.length);
					//System.out.println(nodes[0]);
					//System.out.println(nodes[1]);
					//System.out.println(nodes[2]);
					//counter++;
					//System.out.println("counter "+counter);
					//String[] slatlon = np.search(nodes[1], true);
					//System.out.println("finished first search");
					//System.out.println(nodes[2]);
					String[] dlatlon = np.search(nodes[2], true);
					//System.out.println("no issue with node parser");
					//System.out.println("slatlon "+slatlon.length);
					//System.out.println("dlatlon "+dlatlon.length);
					//double slat = Double.parseDouble(slatlon[0]);
					//double slon = Double.parseDouble(slatlon[1]);
					if(dlatlon!=null){
						//System.out.println("dlatlon: "+dlatlon.length);
						double dlat = Double.parseDouble(dlatlon[0]);
						double dlon = Double.parseDouble(dlatlon[1]);
						if(source.toString().equals(nodes[1])){
							//System.out.println("here");
							g.insertEdge(nodes[1], source.getLat()/*slat*/, source.getLon()/*slon*/, 
									dlat, dlon, nodes[2], nodes[0], this.getDdouble(source.getLat(), dlat,
											source.getLon(), dlon));
							//System.out.println("added way to graph");
							list.add(g.findNode(nodes[2]));
							//if(nodes[2].equals("/n/4182.7140.201260632")) System.out.println("END FOUND");
							//System.out.println(g.findNode(nodes[2]));
							//System.out.println("added node to list of neighbors");
						}
					}
				}
			}
		}
		else{
			return null;
		}
		//System.out.println("dafuq");
		return list;
	}
	
	public double getD(Node a, Node b){
		if(a!=null && b!=null){
			double dlat = (a.getLat()-b.getLat())*Math.PI/180;
			double dlon = (a.getLon()-b.getLon())*Math.PI/180;
			double A = Math.sin(dlat/2)*Math.sin(dlat/2) +
					   Math.sin(dlon/2)*Math.sin(dlon/2) * Math.cos(a.getLat()) * Math.cos(b.getLat());
			double C = 2 * Math.atan2(Math.sqrt(A), Math.sqrt(1-A));
			return C*6378100;
			//return Math.sqrt((a.getLat()-b.getLat())*(a.getLat()-b.getLat())+(a.getLon()-b.getLon())*(a.getLon()-b.getLon()));
		}
		else{
			return Double.MAX_VALUE;
		}
	}
	
	public double getDdouble(double lat1, double lat2, double lon1, double lon2){
		double dlat = (lat1-lat2)*Math.PI/180;
		double dlon = (lon1-lon2)*Math.PI/180;
		double a = Math.sin(dlat/2)*Math.sin(dlat/2) +
				   Math.sin(dlon/2)*Math.sin(dlon/2) * Math.cos(lat1) * Math.cos(lat2);
		double C = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return C*6378100;
	}

	public NodeParser getNp() {
		// TODO Auto-generated method stub
		return _np;
	}
	
	public WayParser getWp() {
		// TODO Auto-generated method stub
		return _wp;
	}

}

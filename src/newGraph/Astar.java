package newGraph;

import newGraph.Node;

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
	
	
	public List<Node> search(Graph g, String src, String dst, WayParser wp, NodeParser np) throws IOException{
		Node _src = g.findNode(src);
		if(_src!=null){
			Map<String, Node> open = new HashMap<String, Node>();
			Map<String, Node> close = new HashMap<String, Node>();
			PriorityQueue<Node> q = new PriorityQueue<Node>(20, new NodeComparator());
			
			open.put(_src.toString(), _src);
			q.add(_src);
			
			Node goal = null;
			while(open.size() > 0){
				Node n = q.poll();
				open.remove(n.toString());
				if(n.toString().equals(dst)){
					goal = n;
					break;
				}
				else{
					close.put(n.toString(), n);
					Set<Node> bors = this.getBors(n, wp, np,g);
					for(Node bor : bors){
						Node v = close.get(bor.toString());
						if(v == null){
							double newg = n.getG() + this.getD(n,  bor);
							Node t = open.get(bor.toString());
							if(t==null){
								open.put(bor.toString(), bor);
								q.add(bor);
								bor.setPrev(n);
								bor.setG(newg);
							}
							else if (newg < t.getG()){
								bor.setPrev(n);
								bor.setG(newg);
								bor.setH(this.getD(bor, null));
							}
						}
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
				return null;
			}
		}
		else{
			return null;
		}
		//return null;
	}
	
	public Set<Node> getBors(Node source, WayParser wp, NodeParser np, Graph g) throws IOException{
		Set<Node> list = new HashSet<Node>();
		String[] ways = np.search(source.toString(), false);
		if(ways!=null){
			for(String w : ways){
				String[] nodes = wp.search(w);
				if(nodes!=null){
					String[] slatlon = np.search(nodes[1], true);
					String[] dlatlon = np.search(nodes[2], true);
					double slat = Double.parseDouble(slatlon[0]);
					double slon = Double.parseDouble(slatlon[1]);
					double dlat = Double.parseDouble(dlatlon[0]);
					double dlon = Double.parseDouble(dlatlon[1]);
					if(source.toString().equals(nodes[1])){
						g.insertEdge(nodes[1], slat, slon, dlat, dlon, nodes[2], nodes[0], Math.sqrt(((slat-dlat)*(slat-dlat)+(slon-dlon)*(slon-dlon))));
						list.add(g.findNode(nodes[2]));
					}
				}
			}
		}
		else{
			return null;
		}
		return list;
	}
	
	public static double getD(Node a, Node b){
		if(a!=null && b!=null){
			return Math.sqrt((a.getLat()-b.getLat())*(a.getLat()-b.getLat())+(a.getLon()-b.getLon())*(a.getLon()-b.getLon()));
		}
		else{
			return Double.MAX_VALUE;
		}
	}

}

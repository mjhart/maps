package newGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Astar {
	
	public List<Node> search(Graph g, String src, String dst){
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
					Set<Node> bors = this.getBors(n);
				}
			}
		}
		else{
			return null;
		}
		return null;
	}
	
	public Set<Node> getBors(Node n){
		return null;
	}

}

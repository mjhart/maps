package graph;
/****
 * This is my graph class.  It is very simple and is represented by
 * vectors of nodes and edges.
 * 
 * 
 */
import java.util.List;
import java.util.Vector;

public class Graph {
	
	private List<Node> _nodes;
	private List<Edge> _edges;
	
	public Graph(){
		_nodes = new Vector<Node>();
		_edges = new Vector<Edge>();
	}
	
	public Graph(List<Node> nodes, List<Edge> edges){
		_nodes = nodes;
		_edges = edges;
	}
	
	public List<Node> getNodes(){
		return _nodes;
	}
	
	public List<Edge> getEdges() {
		return _edges;
	}
	
	
	/**
	 * this method adds a node to the graph
	 * @param actor
	 * @return
	 */
	public int insertNode(String actor){
		Node n = new Node(_nodes.size(),actor);
		if(!_nodes.contains(n)){
			_nodes.add(n);
			return _nodes.indexOf(n);
		}
		else{
			return -1;
		}
	}
	
	/**
	 * this method returns the node (if any) that matches the string given
	 * @param s
	 * @return
	 */
	public Node findNode(String s){
		int i = _nodes.indexOf(new Node(0, s));
		if(i >= 0){
			return _nodes.get(i);
		}
		else{
			return null;
		}
	}
	
	/**
	 * this method returns the edge (if any) between two nodes (given as strings)
	 * @param src
	 * @param dst
	 * @return
	 */
	public Edge findEdge(String src, String dst){
		for(Edge e: _edges){
			if(e.getSource().toString().equals(src) && e.getDest().toString().equals(dst)){
				return e;
			}
		}
		return null;
	}
	
	/*****
	 * this method inserts an edge from source to destination and adds those as nodes
	 * if they don't already exist in the graph.
	 * @param source
	 * @param destination
	 * @param film
	 * @param weight
	 * @return
	 */
	public int insertEdge(String source, String destination, String film, double weight){
		if(source!=null && destination!=null){
			if(source.length()>0 && destination.length()>0){
				Node src = new Node(_nodes.size(), source);
				Node dst = new Node(_nodes.size(), destination);
				if(_nodes.contains(src) && _nodes.contains(dst)){
					if(src.getSec()==dst.getFst()){
						Edge e = new Edge(_edges.size(),src,dst,film,weight);
						if(!_edges.contains(e)){
							_edges.add(e);
						}
						return _edges.indexOf(e);
					}
					else{
						return -1;
					}
				}
				else if(!_nodes.contains(src) && _nodes.contains(dst)){
					this.insertNode(source);
					if(src.getSec()==dst.getFst()){
						Edge e = new Edge(_edges.size(),src,dst,film,weight);
						if(!_edges.contains(e)){
							_edges.add(e);
						}
						return _edges.indexOf(e);
					}
					else{
						return -1;
					}
				}
				else if(_nodes.contains(src) && !_nodes.contains(dst)){
					this.insertNode(destination);
					if(src.getSec()==dst.getFst()){
						Edge e = new Edge(_edges.size(),src,dst,film,weight);
						if(!_edges.contains(e)){
							_edges.add(e);
						}
						return _edges.indexOf(e);
					}
					else{
						return -1;
					}
				}
				else{
					this.insertNode(source);
					this.insertNode(destination);
					if(src.getSec()==dst.getFst()){
						Edge e = new Edge(_edges.size(),src,dst,film,weight);
						if(!_edges.contains(e)){
							_edges.add(e);
						}
						return _edges.indexOf(e);
					}
					else{
						return -1;
					}
				}
			}
			else{
				return -1;
			}
		}
		else{
			return -1;
		}
	}

}

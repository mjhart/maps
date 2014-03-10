package bacon;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graph.*;

public class App {

	private IndexParser _ip;
	private DataParser _ap;
	private DataParser _fp;
	private String _src;
	private String _dst;
	private Graph _g;
	private Map<Node, Node> _preds;
	private Map<Node, Double> _dist;
	private Set<Node> _set;
	private Set<Node> _unset;

	public App(){
		_ip = null;
		_ap = null;
		_fp = null;
	}


	/*******
	 * this method initializes the application by parsing the arguments and creating
	 * all necessary data structures if the arguments are valid.
	 * 
	 * 
	 * @param args
	 */
	public void init(String[] args){
		if(args.length!=5){
			throw new RuntimeException("ERROR: Needs 5 args but got "+args.length);
		}
		else{
			try {
				_ip = new IndexParser(args[4]);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("ERROR: bad index file");
			}
			try {
				_ap = new DataParser(args[3], true);
			} catch (Exception e) {
				throw new RuntimeException("ERROR: bad actors file, couldn't be opened or essential column missing");
			}
			try {
				_fp = new DataParser(args[2], false);
			} catch (Exception e) {
				throw new RuntimeException("ERROR: bad films file, couldn't be opened or essential column missing");
			}
			_src = args[0];
			_dst = args[1];
			_g = new Graph();
		}
	}


	/******
	 * this method is called by Dijkstra's to add neighbors as necessary.
	 * it searches the index for the given source node, then adds all films
	 * of the source actor to a list, then for each film in that list creates
	 * a list of actors also in that film, and finally constructs edges in
	 * the graph as necessary.
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public List<Node> addNeighbors(Node source) throws IOException{
		//_g.insertNode(source);
		List<Node> list = new ArrayList<Node>();
		String uid = _ip.search(source.toString(), 0, _ip.getLength());
		//System.out.println(uid);
		if(uid!=null){
			String[] fids = _ap.search(uid, 0, _ap.getLength());
			/*for(String s : fids){
				System.out.println("Fids: "+s);
			}*/
			String[] fnames = fids;
			int i = 0;
			List<String[]> actors = new ArrayList<String[]>();
			//if(fids!=null)
			for(String s : fids){
				//System.out.println("S: "+s);
				fnames[i] = _fp.searchUID(s, 0, _fp.getLength());
				String[] test = _fp.search(s, 0, _fp.getLength());
				//System.out.println(fnames[i]);
				//System.out.println(test.length);
				//System.out.println("S: "+s);
				//System.out.println(test.length);
				/*for(String s2 : test){
					System.out.println("Fids "+s+", "+s2);
				}*/
				actors.add(test);
				i++;
			}
			i = 0;
			for(String[] sar : actors){
				//System.out.println(sar.length);
				for(String sid : sar){
					String s = _ap.searchUID(sid, 0, _ap.getLength());
					//System.out.println(s);
					if(!source.toString().equals(s)){
						//System.out.println(s);
						if(-1!=_g.insertEdge(source.toString(), s.trim(), fnames[i], 1/sar.length)){
							//System.out.println("Adding edge from "+source.toString()+" to "+s+": "+fnames[i]);
							list.add(_g.findNode(s));
						}
					}

				}
				i++;
			}
			return list;
		}
		else{
			return null;
		}
	}


	/***
	 * this method is the highest level implementation of Dijkstra's algorithm
	 * I do not make any optimizations at all, as my implementation uses unsorted
	 * sets/maps and does not use any priority queues.
	 * @throws IOException
	 */
	public void search() throws IOException{
		_g.insertNode(_src);
		Node src = _g.findNode(_src);
		//System.out.println(src);
		List<Node> list = this.addNeighbors(src);
		if(list==null){
			System.out.println("ERROR: Source actor is not in the index provided.");
		}
		else{
			if(list.size()==0){
				System.out.println(_src+" -/- "+_dst);
			}
			//System.out.println(list.size());
			for(Node n: list){
				//System.out.println(n);
				if(n.compareTo(_dst)==0){
					System.out.println(_src+" -> "+_dst+" : "+_g.findEdge(src.toString(),_dst));
				}
				else{
					_set = new HashSet<Node>();
					_unset = new HashSet<Node>();
					_dist = new HashMap<Node, Double>();
					_preds = new HashMap<Node, Node>();
					_dist.put(_g.findNode(_src), 0.0);
					_unset.add(_g.findNode(_src));
					while(_unset.size() > 0){
						Node temp = this.getMin(_unset);
						_set.add(temp);
						_unset.remove(temp);
						this.getMinD(temp);
					}

					ArrayList<Node> path = new ArrayList<Node>();
					n = _g.findNode(_dst);
					if(n!=null){
						if(_preds.get(n) != null){
							path.add(n);
							while(_preds.get(n) != null){
								n = _preds.get(n);
								path.add(n);
							}
						}
					}
					Collections.reverse(path);
					boolean nopath = true;
					for(int i = 0; i < path.size()-1; i++){
						Node source = path.get(i);
						for(Edge e : _g.getEdges()){
							if(e.getSource().equals(source) && e.getDest().equals(path.get(i+1))){
								System.out.println(source+" -> "+path.get(i+1)+" : "+e.toString());
								nopath = false;
							}
						}
						//System.out.println(t);
					}
					//System.out.println(nopath);
					if(nopath){
						System.out.println(_src+" -/- "+_dst);
					}
				}
			}
		}
	}

	/***
	 * this method returns the nearest neighbor from the given set.
	 * 
	 * @param nodes
	 * @return
	 */
	private Node getMin(Set<Node> nodes){
		Node min = null;
		for(Node n : nodes){
			if(min == null){
				min = n;
			}
			else{
				if(this.getShortD(n) < this.getShortD(min)){
					min = n;
				}
			}
		}
		return min;
	}

	private double getShortD(Node n){
		Double d = _dist.get(n);
		if(d != null){
			return d;
		}
		else{
			return Double.POSITIVE_INFINITY;
		}
	}

	/***
	 * this method adds updated distances to distance map
	 * @param node
	 * @throws IOException
	 */
	private void getMinD(Node node) throws IOException{
		List<Node> adj = this.addNeighbors(node);
		if(adj!=null){
			for(Node n : adj){
				if(this.getShortD(n) > (this.getShortD(node) + getD(node, n))){
					_dist.put(n, (this.getShortD(node) + getD(node, n)));
					_preds.put(n, node);
					_unset.add(n);
				}
			}
		}
	}

	/****
	 * this method get the distance (edge weight) between two nodes
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	private double getD(Node src, Node dst){
		for(Edge e : _g.getEdges()){
			if(e.getSource().equals(src) && e.getDest().equals(dst)){
				return e.getWeight();
			}
		}
		throw new RuntimeException("ERROR: MISSING EDGE");
	}
}
package gui;

import java.util.HashSet;

import newGraph.Edge;
import newGraph.Node;

public class Tile {
	public final HashSet<Node> nodes;
	public final HashSet<Edge> ways;
	
	public final double[] _max;
	public final double[] _min;
	
	public Tile(double[] max, double[] min, HashSet<Node> nodes, HashSet<Edge> ways) {
		this._max = max;
		this._min = min;
		this.nodes = nodes;
		this.ways = ways;
	}
	
	public boolean intersects(double[] max, double[] min) {
		return !(min[0] > _max[0] ||
				 max[0] < _min[0] ||
				 max[1] > _min[1] ||
				 min[1] < _max[1]); 
	}

}

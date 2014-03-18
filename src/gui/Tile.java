package gui;

import java.util.HashSet;

import newGraph.Edge;
import newGraph.Node;

public class Tile {
	public final HashSet<Node> nodes;
	public final HashSet<Edge> ways;
	
	public final int x;
	public final int y;
	
	public Tile(int x, int y, HashSet<Node> nodes, HashSet<Edge> ways) {
		this.x = x;
		this.y = y;
		this.nodes = nodes;
		this.ways = ways;
	}
	
	/*
	public boolean intersects(double[] max, double[] min) {
		return !(min[0] > _max[0] ||
				 max[0] < _min[0] ||
				 max[1] > _min[1] ||
				 min[1] < _max[1]); 
	}
	*/
	
	public double getMinLon() {
		return AltDrawingPanel.txToLon(x);
	}
	public double getMinLat() {
		return AltDrawingPanel.tyToLat(y);
	}
	public double getMaxLon() {
		return AltDrawingPanel.txToLon(x) + AltDrawingPanel.TILE_SIZE;
	}
	public double getMaxLat() {
		return AltDrawingPanel.tyToLat(y) + AltDrawingPanel.TILE_SIZE;
	}
}

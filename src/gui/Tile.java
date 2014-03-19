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
	
	
	public boolean intersects(int[] max, int[] min) {
		return !(min[0] > x+1 ||
				 max[0] < x ||
				 max[1] < y ||
				 min[1] > y+1); 
	}
	
	
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

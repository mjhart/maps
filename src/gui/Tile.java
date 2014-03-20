package gui;

import graph.Edge;
import graph.Node;

import java.util.HashSet;


public class Tile {
	public final HashSet<Node> nodes;
	public final HashSet<Edge> ways;
	
	public final int x;
	public final int y;
	
	private boolean _loaded;
	
	public Tile(int x, int y, HashSet<Node> nodes, HashSet<Edge> ways) {
		this.x = x;
		this.y = y;
		this.nodes = nodes;
		this.ways = ways;
		_loaded = false;
	}
	
	
	public boolean intersects(int[] max, int[] min) {
		return !(min[0] >= x+1 ||
				 max[0] <= x ||
				 max[1] <= y ||
				 min[1] >= y+1); 
	}
	
	
	public double getMinLon() {
		return DrawingPanel.txToLon(x);
	}
	public double getMinLat() {
		return DrawingPanel.tyToLat(y);
	}
	public double getMaxLon() {
		return DrawingPanel.txToLon(x) + DrawingPanel.TILE_SIZE;
	}
	public double getMaxLat() {
		return DrawingPanel.tyToLat(y) + DrawingPanel.TILE_SIZE;
	}
	public void setLoaded() {
		synchronized(this) {
		_loaded = true;
		}
	}
	public boolean isLoaded() {
		synchronized(this) {
			return _loaded;
		}
	}
	
	@Override
	public String toString() {
		return new String("(" + this.x + "," + this.y + ")");
	}
}

package gui;

import graph.Edge;
import graph.Node;

import java.util.HashSet;


/**
 * @author mjhart
 * This is the basic unit in our map. It holds 
 * all the nodes and ways contained in its 
 * coordinates
 */
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
	
	
	/**
	 * Checks if this tile intersects the given box
	 * 
	 * @param max
	 * @param min
	 * @return
	 */
	public boolean intersects(int[] max, int[] min) {
		return !(min[0] >= x+1 ||
				 max[0] <= x ||
				 max[1] <= y ||
				 min[1] >= y+1); 
	}
	
	/* lat-lon bounding box info*/
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
	
	
	/**
	 * Call this once the way and node data has been loaded into this tile
	 */
	public void setLoaded() {
		synchronized(this) {
		_loaded = true;
		}
	}
	
	
	/**
	 * @return true if way and node data has been loaded
	 */
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

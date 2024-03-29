package graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import kdTree.KDTreeEntry;

/***
 * this class models a node for my graph.  It stores the id, latitude, longitude, and Astar data as well as a unique ID for debugging.
 * @author sbreslow
 *
 */
public class Node extends Point2D.Double implements Comparable<Object>, KDTreeEntry {
	
	private String _id;
	private int _my_id;
	private double _lat;
	private double _lon;
	private double _g;
	private double _h;
	private double _f;
	private Node _prev;
	
	private List<Edge> _edges;
	
	public Node(int my_id, String id, double lat, double lon){
		super();
		_my_id = my_id;
		_id = id;
		_g = 0;//java.lang.Double.MAX_VALUE;
		_h = 0;//java.lang.Double.MAX_VALUE;
		_f = 0;//java.lang.Double.MAX_VALUE;
		_lat = lat;
		_lon = lon;
		_prev = null;
	}
	
	private int latToY(double min, double max, double scale){
		//System.out.println("Y: "+(int) ((max-_lat)/(max-min) * scale));
		//System.out.println(max-_lat);
		this.setLocation(this.getX(), (int) ((max-_lat)/(max-min) * scale));
		return (int) ((max-_lat)/(max-min) * scale);
	}
	
	private int lonToX(double min, double max, double scale){
		//System.out.println("X: "+(int) ((_lon-min)/(max-min) * scale));
		//System.out.println(min);
		//System.out.println(_lon);
		this.setLocation((int) ((_lon-min)/(max-min) * scale), this.getY());
		return (int) ((_lon-min)/(max-min) * scale);
	}
	
	public void paint(Graphics2D brush, double maxLat, double minLat, double maxLon, double minLon, double height, double width){
		int y = this.latToY(minLat, maxLat, height);
		int x = this.lonToX(minLon, maxLon, width);
		
		if(this.getLat() > maxLat) {
			System.out.println(this.getY());
		}
		if(this.getLon() > maxLon) {
			System.out.println(this.getX());
		}
		//System.out.println("x: " + x);
		//System.out.println("y: " + y);
		brush.setColor(Color.BLACK);
		brush.drawRect(x,y,1,1);
	}
	
	public double getLat(){
		return _lat;
	}
	
	public double getLon(){
		return _lon;
	}
	
	public String getId(){
		return _id;
	}
	
	public double getG(){
		return _g;
	}
	
	public double getH(){
		return _h;
	}
	
	public double getF(){
		return _f;
	}
	
	public void setPrev(Node prev){
		_prev = prev;
	}
	
	public void setG(double g){
		_g = g;
		_f = _g + _h;
	}
	
	public void setH(double h){
		_h = h;
		_f = _g + _h;
	}
	
	public Node getPrev(){
		return _prev;
	}

	@Override
	public int compareTo(Object o) {
		//Node n = (Node) o;
		return this.toString().compareTo(o.toString());
	}
	
	@Override
	public boolean equals(Object o) {
		//Node n = (Node) o;
		if(this.toString().equals(o.toString())){
			return true;
		}
		else{
			return false;
		}
		//return this.toString().equals(n.toString());
	}
	
	@Override
	public String toString(){
		return _id;
	}
	
	/* added these methods to comply with KDTreeENtry */

	@Override
	public double getCoord(int dim) {
		if(dim==0) {
			return _lon;
		}
		if(dim==1) {
			return _lat;
		}
		throw new IllegalArgumentException("dim only valid for 0 or 1");
	}

	@Override
	public double[] getCoords() {
		double[] array = {_lon, _lat};
		return array;
	}

	@Override
	public int getDimensions() {
		return 2;
	}
	
	public void setLoc(double x, double y){
		//some transform here
		this.setLocation(x, y);
	}
	
	public void addEdge(Edge e) {
		if(_edges == null) {
			_edges = new LinkedList<Edge>();
		}
		_edges.add(e);
	}
	
	public boolean edgesLoaded() {
		return _edges != null;
	}
	
	public List<Edge> getEdges() {
		return Collections.unmodifiableList(_edges);
	}

}

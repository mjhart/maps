package newGraph;

import java.awt.geom.Point2D;

import kdTree.KDTreeEntry;

/***
 * this class models a node for my graph.  It stores the name, first initial,
 * and second initial of the associated actor, as well as a unique ID for debugging.
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
	
	public Node(int my_id, String id, double lat, double lon){
		super();
		_my_id = my_id;
		_id = id;
		_g = java.lang.Double.MAX_VALUE;
		_h = java.lang.Double.MAX_VALUE;
		_f = java.lang.Double.MAX_VALUE;
		_lat = lat;
		_lon = lon;
		_prev = null;
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
			return _lat;
		}
		if(dim==1) {
			return _lon;
		}
		throw new IllegalArgumentException("dim only valid for 0 or 1");
	}

	@Override
	public double[] getCoords() {
		double[] array = {_lat, _lon};
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

}

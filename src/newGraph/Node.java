package newGraph;
/***
 * this class models a node for my graph.  It stores the name, first initial,
 * and second initial of the associated actor, as well as a unique ID for debugging.
 * @author sbreslow
 *
 */
public class Node implements Comparable<Object>{
	
	private String _id;
	private int _my_id;
	private double _lat;
	private double _lon;
	private double _g;
	private double _h;
	private double _f;
	private Node _prev;
	
	public Node(int my_id, String id, double lat, double lon){
		_my_id = my_id;
		_id = id;
		_g = Double.MAX_VALUE;
		_h = Double.MAX_VALUE;
		_f = Double.MAX_VALUE;
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

}

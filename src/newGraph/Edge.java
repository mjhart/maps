package newGraph;
/***
 * This class models an Edge for my graph.  It stores its source, destination, the name
 * of the film that connects the two, and the weight of the edge, as well as a unique ID
 * for debugging.
 * @author sbreslow
 *
 */
public class Edge {
	
	private int _id;
	private Node _source;
	private Node _dest;
	private String _film;
	private double _weight;
	
	public Edge(int id, Node source, Node dest, String film, double weight){
		_id = id;
		_source = source;
		_dest = dest;
		_film = film;
		_weight = weight;
	}
	
	public int getId(){
		return _id;
	}
	
	public void setWeight(double weight){
		_weight = weight;
	}
	
	public double getWeight(){
		return _weight;
	}
	
	public String getFilm(){
		return _film;
	}
	
	public Node getSource(){
		return _source;
	}
	
	public Node getDest(){
		return _dest;
	}
	
	@Override
	public boolean equals(Object o){
		Edge e = (Edge) o;
		if(_source.equals(e.getSource()) && _dest.equals(e.getDest()) && _weight==e.getWeight()){
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	public String toString(){
		return _film;
	}

}
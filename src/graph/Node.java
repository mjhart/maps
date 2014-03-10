package graph;
/***
 * this class models a node for my graph.  It stores the name, first initial,
 * and second initial of the associated actor, as well as a unique ID for debugging.
 * @author sbreslow
 *
 */
public class Node implements Comparable<Object>{
	
	private String _name;
	private int _id;
	String[] _split;
	char _fst;
	char _sec;
	
	public Node(int id, String name){
		_id = id;
		_name = name;
		_split = name.split(" ");
		_fst = _split[0].charAt(0);
		if(_split.length==1){
			_sec = _fst;
		}
		else{
			/*if(_split[_split.length-1].endsWith(".")){
				_sec = _split[_split.length-2].charAt(0);
			}
			else{*/
				_sec = _split[_split.length-1].charAt(0);
			//}
		}
	}
	
	public int getId(){
		return _id;
	}
	
	public char getFst(){
		return _fst;
	}
	
	public char getSec(){
		return _sec;
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
		return _name;
	}

}

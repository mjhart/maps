package parsers;

import java.util.Comparator;

public class NodeIdComparator implements Comparator<String>{
	private int _id;

	public NodeIdComparator(int id) {
		_id = id;
	}
	@Override
	public int compare(String current, String query) {
		String[] line = current.split("\t");
		System.out.println(line[_id]);
		return line[_id].compareTo(query);
	}
}

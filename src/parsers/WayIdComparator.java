package parsers;
import java.util.Comparator;


public class WayIdComparator implements Comparator<String> {
	private int _id;

	public WayIdComparator(int id) {
		_id = id;
	}
	@Override
	public int compare(String current, String query) {
		String[] line = current.split("\t");
		return line[_id].compareTo(query);
	}

}

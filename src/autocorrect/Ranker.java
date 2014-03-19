package autocorrect;

import java.io.FileNotFoundException;
import java.util.List;

public interface Ranker {

	/**
	 * Sort the given list
	 * 
	 * @param list
	 * @param current
	 * @param prev
	 */
	public void rank(List<String> list, String current);
}

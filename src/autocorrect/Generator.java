package autocorrect;

import java.util.List;

public interface Generator {

	/**
	 * Generate a list of strings based on this word
	 * 
	 * @param word
	 * @return
	 */
	public List<String> suggest(String word);
}

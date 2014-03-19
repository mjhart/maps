package autocorrect;

/**
 * @author mjhart
 * 
 * A structure that can be used to compare word suggestions
 * according to the specifications given
 *
 */
public class PQEntry implements Comparable<PQEntry> {

	public final int biCount;
	public final int uniCount;
	public final String s;
	
	/**
	 * Returns a PQEntry with the given counts and string
	 * 
	 * @param biCount
	 * @param uniCount
	 * @param s
	 * @throws Exception 
	 */
	public PQEntry(int biCount, int uniCount, String s) {
		this.biCount = biCount;
		this.uniCount = uniCount;
		this.s = s;
	}
	
	@Override
	public int compareTo(PQEntry o) {
		if(o == this) {
			return 0;
		}
		if(biCount == o.biCount) { // if bigram counts are same
			if(uniCount ==  o.uniCount) { // if unigram counts are same
				return (-1)*s.compareTo(o.s); // use reverse lexigraphical order
			}
			// unigram compare
			if(uniCount < o.uniCount) return -1;
			if(uniCount == o.uniCount) return 0;
			return 1;
		}
		//bigram compare
		if(biCount < o.biCount) return -1;
		if(biCount == o.biCount) return 0;
		return 1;
	}
}

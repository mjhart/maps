package graph;

import java.util.Comparator;


/*****
 * this class lets me compare nodes based on calculated distance for the Astar priority queue.
 * @author sbreslow
 *
 */

public class NodeComparator implements Comparator<Node> {

	@Override
	public int compare(Node a, Node b) {
		if(a.getF() < b.getF()) return -1;
		else if(a.getF() > b.getF()) return 1;
		else return 0;
	}

}

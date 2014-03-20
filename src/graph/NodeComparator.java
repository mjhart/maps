package graph;

import java.util.Comparator;


public class NodeComparator implements Comparator<Node> {

	@Override
	public int compare(Node a, Node b) {
		if(a.getF() < b.getF()) return -1;
		else if(a.getF() > b.getF()) return 1;
		else return 0;
	}

}

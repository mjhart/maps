package kdTree;

/**
 * @author mjhart
 * 
 * This class models the internal nodes used in a 
 * KDTree. They store an entry which must store 
 * coordinate information. The node has accessor 
 * methods to access the coordinate information
 * in the entry. It also stores a dimension which 
 * is associated with its depth in the tree.
 *
 * @param <T>
 */
class KDNode<T extends KDTreeEntry> {
	public final int dim;
	private KDNode<T> left;
	private KDNode<T> right;
	public final T entry;
	
	/**
	 * @param dim - depth of this node in the tree
	 * mod the total number of dimensions in the 
	 * tree
	 * 
	 * @param entry - entry to be stored at this 
	 * node
	 */
	public KDNode(int dim, T entry) {
		
		// check dim is greater than 0
		if(dim >= 0) {
			this.dim = dim;
		}
		else {
			throw new IllegalArgumentException("dim must be greater than 0");
		}
		
		// check entry stored at this node is non-null
		if(entry != null) {
			this.entry = entry;
		}
		else {
			throw new IllegalArgumentException("entry must be non-null");
		}
	}
	
	/**
	 * @return An array containing the coordinate information at this node
	 */
	public double[] getCoords() {
		return entry.getCoords();
	}
	
	/**
	 * @return the plane this node divides along
	 */
	public double getPlane() {
		return entry.getCoord(dim);
	}

	/**
	 * @return right child
	 */
	public KDNode<T> getRight() {
		return right;
	}

	/**
	 * @param right child
	 */
	public void setRight(KDNode<T> right) {
		this.right = right;
	}

	/**
	 * @return left child
	 */
	public KDNode<T> getLeft() {
		return left;
	}

	/**
	 * @param left child
	 */
	public void setLeft(KDNode<T> left) {
		this.left = left;
	}
	
	/**
	 * @return true if node has left child
	 * false otherwise
	 */
	public boolean hasLeft() {
		return left != null;
	}
	
	/**
	 * @return true if node has right child
	 * false otherwise
	 */
	public boolean hasRight() {
		return right != null;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Dim: ");
		str.append(dim);
		str.append(" Coords: ");
		for(double d : entry.getCoords()) {
			str.append(d + " ");
		}
		return str.toString();
	}
}

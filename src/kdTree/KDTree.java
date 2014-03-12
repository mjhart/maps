package kdTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TreeMap;

/**
 * This class models a k-dimensional tree. It can accept any
 * kind of entry which implements the KDTreeEntry interface.
 * 
 * @author mjhart
 *
 * @param <T>
 */
public class KDTree<T extends KDTreeEntry> {
	private KDNode<T> root;
	private int k;
	
	/**
	 * Creates a new empty tree which stores entries of the 
	 * given dimension
	 * 
	 * @param dimensions
	 */
	public KDTree(int dimensions) {
		
		// check dimensions is greater than 0
		if (dimensions > 0) {
			this.k = dimensions;
		}
		else {
			throw new IllegalArgumentException("Tree must have positive dimension");
		}
	}
	

	/**
	 * Inserts the given entry into the tree
	 * WARNING: using this method does not 
	 * guarantee the tree will be balanced.
	 * @param entry
	 */
	public void add(T entry) {
		
		// check entry has proper dimensionality
		if(entry.getDimensions() != k) {
			throw new IllegalArgumentException("Entries must have same dimensionality as tree");
		}
		
		// start new tree if empty
		if(root == null) {
			root = new KDNode<T>(0, entry);
		}
		
		// else call helper method
		else {
			insert(root, entry, 0);
		}
	}
	
	/**
	 * Helper method used to insert nodes into the tree
	 */
	private void insert(KDNode<T> node, T entry, int depth) {
		
		int dim = depth%k; // calculate dimension at this level
		
		// go down left subtree
		if(entry.getCoord(dim) < node.getPlane()) {
			if(node.hasLeft()) {
				insert(node.getLeft(), entry, depth+1);
			}
			else {
				node.setLeft(new KDNode<T>(depth+1, entry));
			}
		}
		
		// go down right subtree
		else {
			if(node.hasRight()) {
				insert(node.getRight(), entry, depth+1);
			}
			else {
				node.setRight(new KDNode<T>(depth+1, entry));
			}
		}
	}
	
	/**
	 * Adds all the entries in the list to the 
	 * tree. If the tree is empty, the nodes will
	 * be added in a way that results in a balanced 
	 * tree. If there are already nodes in the tree,
	 * the tree is not guaranteed to be balanced.
	 * 
	 * @param nodeList
	 */
	public void addAll(ArrayList<T> nodeList) {
		
		// if tree is empty call build tree helper
		if(root == null) {
			root = buildTree(nodeList, 0);
		}
		
		// else insert entries in order
		else {
			for(T e : nodeList) {
				add(e);
			}
		}
	}
	

	/**
	 * Helper method used to build a balanced tree. It recursively
	 * adds the median of all the nodes according to the dimension
	 * at the given depth.
	 * 
	 * @param nodeList
	 * @param depth
	 * @return
	 */
	private KDNode<T> buildTree(ArrayList<T> nodeList, int depth) {
		
		// base case 
		if(nodeList.size() == 0) {
			return null;
		}
		
		// base case
		if(nodeList.size() == 1) {
			return new KDNode<T>(depth%k, nodeList.get(0));
		}
		
		// find dimension
		int dim = depth%k;
		
		// find median of list on current dimension
		T mid = median(nodeList, dim);
		
		// check entry has proper dimensionality
		if(mid.getDimensions() != k) {
			throw new IllegalArgumentException("Entries must have same dimensionality as tree");
		}
		
		// create new node
		KDNode<T> node = new KDNode<T>(dim, mid);
		
		// recursively call on sublists
		node.setLeft(buildTree(leftList(nodeList, mid, dim), depth+1));
		node.setRight(buildTree(rightList(nodeList, mid, dim), depth+1));
		
		// return created node
		return node;
	}
	
	/**
	 * Finds and returns the median of a list of 
	 * entries the given dimension
	 * 
	 * @param entryList
	 * @param dim
	 * @return median entry
	 */
	private T median(ArrayList<T> entryList, int dim) {
		int from = 0; 
		int to = entryList.size()-1;
		int m = entryList.size()/2;
	
		while (from < to) {
			int r = from; // reader
			int w = to; // writer
			T pivot = entryList.get((r + w) / 2);
	
			// move entries greater than pivot to top of list
			while (r < w) {
				if (entryList.get(r).getCoord(dim) >= pivot.getCoord(dim)) {
					Collections.swap(entryList, w, r);
					w--;
				} else { 
					r++;
				}
			}
			
			// move down reader
			if (entryList.get(r).getCoord(dim) > pivot.getCoord(dim))
				r--;
			
			// repeat on half of list that contains mid
			if (m <= r) {
				to = r;
			} else {
				from = r + 1;
			}
		}
	
		return entryList.get(m);
	}


	/**
	 * Adds entries smaller than mid to left list
	 * 
	 * @param nodeList
	 * @param mid
	 * @param dim
	 * @return list of entries
	 */
	private ArrayList<T> leftList(ArrayList<T> nodeList, T mid, int dim) {
		ArrayList<T> newList = new ArrayList<T>();
		for(T e : nodeList) {
			if(e.getCoord(dim) < mid.getCoord(dim)) {
				newList.add(e);
			}
		}
		return newList;
	}

	/**
	 * Adds entries greater than mid to right list
	 * 
	 * @param nodeList
	 * @param mid
	 * @param dim
	 * @return list of entries
	 */
	private ArrayList<T> rightList(ArrayList<T> nodeList, T mid, int dim) {
		ArrayList<T> newList = new ArrayList<T>();
		for(T e : nodeList) {
			if(e.getCoord(dim) > mid.getCoord(dim)) {
				newList.add(e);
			}
			else if(e.getCoord(dim) == mid.getCoord(dim) && !e.equals(mid)) {
				newList.add(e);
			}
		}
		return newList;
	}


	/**
	 * Finds the n nearest neighbors to the given coordinates
	 * 
	 * @param coordinates
	 * @param n - number of neighbors
	 * @return list of entries
	 */
	public List<T> nearestNeighbors(double[] coordinates, int n) {
		
		// check neighbor count is non-negative
		if(n < 0) {
			throw new IllegalArgumentException("Neighbor count must be non-negative");
		}
		
		// check coordinates match dimension of tree
		if(coordinates.length != k) {
			throw new IllegalArgumentException("Length of coordinate array must match tree dimension");
		}
		
		// create new tree map
		TreeMap<Double, T> tm = new TreeMap<Double, T>();
		
		// call helper function
		if(root != null && n != 0) {
			nnHelper(coordinates, root, 0, tm, n);
		}
		return new ArrayList<T>(tm.values());
	}
	
	/**
	 * Helper function to return the nearest neighbors
	 * 
	 * @param coords
	 * @param node
	 * @param depth
	 * @param tm
	 * @param n
	 */
	private void nnHelper(double[] coords, KDNode<T> node, int depth, TreeMap<Double, T> tm, int n) {
		
		int dim = depth%k; // calculate dimension at this depth
		
		// throw error if dimensions dont match
		if(dim != node.dim) {
			System.err.println("Something went wrong");
		}
		
		// left subtree
		if(coords[dim] < node.getPlane()) {
			if(node.hasLeft()) {
				nnHelper(coords, node.getLeft(), depth+1, tm, n);
			}
			// if result set not full or sphere of max result intersects plane go down right
			if(tm.size() < n || Math.pow(node.getPlane()-coords[dim], 2) < tm.lastKey()) {
				if(node.hasRight()) {
					nnHelper(coords, node.getRight(), depth+1, tm, n);
				}
			}
		}
		
		// right subtree
		else {
			if(node.hasRight()) {
				nnHelper(coords, node.getRight(), depth+1, tm, n);
			}
			
			// if result set not full or sphere of max result intersects plane go down left
			if(tm.size() < n || Math.pow(node.getPlane()-coords[dim], 2) < tm.lastKey()) {
				if(node.hasLeft()) {
					nnHelper(coords, node.getLeft(), depth+1, tm, n);
				}
			}
		}
		
		// attempt to add this node
		T entry = node.entry;
		double d = distance(coords, entry.getCoords());
		
		if(tm.size() < n) { // if result set has space, insert immediately
			tm.put(d, entry);
		}
		else if(d < tm.lastKey()) { // else compare to last key
			tm.pollLastEntry();
			tm.put(d, entry);
		}
	}
	
	
	/**
	 * Finds all the entries within the given radius from the 
	 * coordinates
	 * 
	 * @param coordinates
	 * @param radius
	 * @return list of entries
	 */
	public List<T> withinRadius(double[] coordinates, double radius) {
		
		// check for illegal input
		if(radius < 0) {
			throw new IllegalArgumentException("Radius must be non-negative");
		}
		if(coordinates.length != k) {
			throw new IllegalArgumentException("Length of coordinate array must match tree dimension");
		}
		
		// create tree map for results
		TreeMap<Double, T> tm = new TreeMap<Double, T>();
		
		// square radius
		radius = Math.pow(radius, 2);
		
		// if tree not empty call helper function
		if(root != null) {
			radiusHelper(coordinates, radius, root, 0, tm);
		}
		
		//build result list
		List<T> results = new LinkedList<T>(tm.values());
		return results;
	}
	
	/**
	 * Helper function to return all the entries in the given radius
	 * 
	 * @param coords
	 * @param r
	 * @param node
	 * @param depth
	 * @param tm
	 */
	private void radiusHelper(double[] coords, double r, KDNode<T> node, int depth, TreeMap<Double, T> tm) {
		int dim = depth%k;
				
		// left subtree
		if(coords[dim] < node.getPlane()) {
			if(node.hasLeft()) {
				radiusHelper(coords, r, node.getLeft(), depth+1, tm);
			}
			
			// if plane intersects sphere go down right subtree
			if(Math.pow((node.getPlane()-coords[dim]), 2) <=r) {
				if(node.hasRight()) {
					radiusHelper(coords, r, node.getRight(), depth+1, tm);
				}
			}
			
		}
		
		// right subtree
		else {
			if(node.hasRight()) {
				radiusHelper(coords, r, node.getRight(), depth+1, tm);
			}
			
			// if plane intersects sphere go down left subtree
			if(Math.pow((node.getPlane()-coords[dim]), 2) <=r) {
				if(node.hasLeft()) {
					radiusHelper(coords, r, node.getLeft(), depth+1, tm);
				}
			}
		}
		
		// find distance between points and current node
		double d = 0;
		for(int i=0; i<k; i++) {
			d += (Math.pow((coords[i]-node.getCoords()[i]), 2));
		}
		
		// add if within r
		if(d <= r) {
			tm.put(d, node.entry);
		}
	}
	
	/**
	 * Prints all the nodes in this tree
	 */
	public void printTree() {
		
		if(root == null) {
			System.out.println("Empty tree");
			return;
		}
		Queue<KDNode<T>> q = new LinkedList<KDNode<T>>();
		Queue<KDNode<T>> next = new LinkedList<KDNode<T>>();
		next.offer(root);
		while(!next.isEmpty()) {
			q = next;
			next = new LinkedList<KDNode<T>>();
			System.out.print("[");
			while(!q.isEmpty()) {
				KDNode<T> node = q.poll();
				System.out.print(node+ ", ");
				if(node.hasLeft()) {
					next.offer(node.getLeft());
				}
				if(node.hasRight()) {
					next.offer(node.getRight());
				}
			}
			System.out.print("]");
			System.out.println();
		}
	}
	
	/**
	 * Calculates the cartesian distance between two sets 
	 * of coordinates
	 * 
	 * @param coords1
	 * @param coords2
	 * @return distance
	 */
	private double distance(double[] coords1,double[] coords2) {
		double d = 0;
		for(int i=0; i<k; i++) {
			d += (Math.pow((coords1[i]-coords2[i]), 2));
		}
		return d;
	}
	
	public List<T> withinBox(double[] max, double[] min) {
		
		// check for illegal input
		if(max.length != k) {
			throw new IllegalArgumentException("Length of nw array must match tree dimension");
		}
		
		if(min.length != k) {
			throw new IllegalArgumentException("Length of se array must match tree dimension");
		}
		
		// create linked list for results
		LinkedList<T> results = new LinkedList<T>();
		
		// if tree not empty call helper function
		if(root != null) {
			boxHelper(max, min, root, 0, results);
		}
		

		return results;
	}

	private void boxHelper(double[] max, double[] min, KDNode<T> node, int depth, LinkedList<T> results) {
		int dim = depth%k;
		
		boolean contained = true;
		double[] coords = node.getCoords();
		for(int i=0; i<k; i++) {
			if(coords[i] > max[i] || coords[i] < min[i]) {
				contained = false;
				break;
			}
		}
		if(contained) {
			results.add(node.entry);
		}
		
		// left subtree
		if(min[dim] < node.getPlane()) {
			if(node.hasLeft()) {
				boxHelper(max, min, node.getLeft(), depth+1, results);
			}

		}

		// right subtree
		if(max[dim] >= node.getPlane()) {
			if(node.hasRight()) {
				boxHelper(max, min, node.getRight(), depth+1, results);
			}
		}
	}
}

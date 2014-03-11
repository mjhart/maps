package kdTree;

public interface KDTreeEntry {
	
	/**
	 * Returns the coordinate in the given dimension
	 * @param dim
	 * @return coordinate
	 */
	public double getCoord(int dim);
	
	/**
	 * Returns all the coordinates in this entry
	 * @return array of coordinates
	 */
	public double[] getCoords();
	
	/**
	 * Returns the number of dimensions this entry has
	 * @return number of dimensions
	 */
	public int getDimensions();
}

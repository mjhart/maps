package newGraph;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import parsers.FileSearcher;
import parsers.NodeIdComparator;

public class NodeDictionary {

	private HashMap<String, Node> _nodes;
	private RandomAccessFile _file;
	private int _id;
	private int _lat;
	private int _lon;
	private int _ways;
	
	public NodeDictionary(String filename) throws FileNotFoundException {
		
		_file = new RandomAccessFile(filename, "r");
		
		// set up indices
		_id = -1;
		_lon = -1;
		_lat = -1;
		_ways = -1;
		
		String[] header = null;
		try {
			header = _file.readLine().split("\t");
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(int i=0; i<header.length; i++) {
			if(header[i].equals("id")) {
				_id = i;
			}
			if(header[i].equals("latitude")) {
				_lat = i;
			}
			if(header[i].equals("longitude")) {
				_lon = i;
			}
			if(header[i].equals("ways")) {
				_ways = i;
			}
		}

		if(_id == -1) {
			System.err.println("ERROR: No id field in " + filename);
			System.exit(1);
		}
		if(_lon == -1) {
			System.err.println("ERROR: No longitude field in " + filename);
			System.exit(1);
		}
		if(_lat == -1) {
			System.err.println("ERROR: No latitude field in " + filename);
			System.exit(1);
		}
		if(_ways == -1) {
			System.err.println("ERROR: No ways field in " + filename);
			System.exit(1);
		}
		
		_nodes = new HashMap<String, Node>();
		
		buildDictionary();
	}
	
	private void buildDictionary() {
		
		try {
			
			String line;
			int i=0;
			while((line = _file.readLine()) != null) {
				String[] data = line.split("\t");
				_nodes.put(data[_id], new Node(++i, data[_id], Double.parseDouble(data[_lat]), Double.parseDouble(data[_lon])));
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Node> nodeList() {
		return new ArrayList<Node>(_nodes.values());
	}
	
	public List<String> getWayIds(String nodeId) {
		LinkedList<String> results = new LinkedList<String>();
		NodeIdComparator nc = new NodeIdComparator(_id);
		FileSearcher fs = new FileSearcher(_file, nc);
		long location = fs.search(nodeId);
		if(location != -1) {
			try {
				_file.seek(location);
				String[] line = _file.readLine().split("\t");
				if(line.length > _ways) {
					String[] ways = line[_ways].split(",");
					for(String s : ways) {
						results.add(s);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	public Node getNode(String id) {
		return _nodes.get(id);
	}

}

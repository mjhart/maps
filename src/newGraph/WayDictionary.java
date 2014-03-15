package newGraph;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

import parsers.FileSearcher;
import parsers.WayIdComparator;

public class WayDictionary {
	
	public static int BUFFER_SIZE = 1024; 
	
	private RandomAccessFile _file;
	private HashMap<String, Edge> _ways;
	private int _id;
	private int _start;
	private int _end;
	private NodeDictionary _nodes;
	
	// debugging
	public int toDisk = 0;
	public int inMem = 0;
	public int calls = 0;
	public int nullWays = 0;
	
	public WayDictionary(String filename, NodeDictionary nd) throws FileNotFoundException {
		_file = new RandomAccessFile(filename, "r");
		
		// set up indices
		_id = -1;
		_start = -1;
		_end = -1;
		
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
			if(header[i].equals("start")) {
				_start = i;
			}
			if(header[i].equals("end")) {
				_end = i;
			}
		}

		if(_id == -1) {
			System.err.println("ERROR: No id field in " + filename);
			System.exit(1);
		}
		if(_start == -1) {
			System.err.println("ERROR: No start field in " + filename);
			System.exit(1);
		}
		if(_end == -1) {
			System.err.println("ERROR: No end field in " + filename);
			System.exit(1);
		}
		
		_nodes = nd;
		_ways = new HashMap<String, Edge>();
	}
	
	public ArrayList<Edge> wayList() {
		return new ArrayList<Edge>(_ways.values());
	}
	
	public Edge getWay(String id) {
		if(_ways.containsKey(id)) {
			inMem++;
			return _ways.get(id);
		}
		
		/*
		if(calls > 2) {
			System.out.println("returning early");
			return null;
		}
		*/
		toDisk++;
		
		WayIdComparator comp = new WayIdComparator(_id);
		FileSearcher fs = new FileSearcher(_file, comp);
		long pos = fs.search(id);
		
		Edge result = null;
		
		if(pos > 0) {
			try {
				_file.seek(pos);
				String[] line = _file.readLine().split("\t");
				result = createEdge(line);

				//importBox(result, pos);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(result == null) {
			nullWays++;
		}
		
		return result;
	}
	
	public Node getEnd(String id){
		Edge e = this.getWay(id);
		if(e==null){
			return null;
		}
		else{
			return e.getDest();
		}
	}
	
	public Node getStart(String id){
		Edge e = this.getWay(id);
		if(e==null){
			return null;
		}
		else{
			return e.getSource();
		}
	}
	
	private Edge createEdge(String[] data) {
		String sid = data[_start];
		String eid = data[_end];
		String id = data[_id];
		
		Node start = _nodes.getNode(sid);
		Node end = _nodes.getNode(eid);
		
		Edge e = null;
		
		//System.out.println(start);
		//System.out.println(end);
		
		if(start!=null && end!=null){
			e = new Edge(_ways.size(), start, end, id, Astar.getD(start,end));
			_ways.put(id, e);
		}
		
		return e;
	}
	
	private void importBox(Edge entry, long pos) {
		
		// take position of source (arbitrary)
//		double lat = entry.getSource().getLat();
//		double lon = entry.getSource().getLon();
		
		// find borders of bounding box
//		double min_lat = lat - lat%0.01;
//		double min_lon = lon - lon%0.01;
//		double max_lat = lat + 0.01 - lat%0.01;
//		double max_lon = lon + 0.01 - lon%0.01;
		
		
		try {
			
			// read buffer from below input
			
			long size = BUFFER_SIZE;
			
			if(pos - BUFFER_SIZE < 0) {
				_file.seek(0);
				size = pos;
			}
			else {
				_file.seek(pos-BUFFER_SIZE);
			}
			
			System.out.println(size);
			
			byte[] buf = new byte[(int) size];
			_file.read(buf);
			
			int i = 0;
			while(buf[i] != '\n') {
				i++;
			}
			i++;
			
			while(i<size) {
				StringBuilder sb = new StringBuilder();
				while(buf[i] != '\n' && i<size) {
					sb.append((char)buf[i]);
					i++;
				}
				System.out.println(sb.toString());
				createEdge(sb.toString().split("\t"));
				i++;
			}
			
			_file.seek(pos);
			_file.readLine();
			
			size = BUFFER_SIZE;
			
			if(pos + BUFFER_SIZE >= _file.length()) {
				size = _file.length() - _file.getFilePointer();
			}
			
			_file.read(buf);
			
			i = 0;
			while(buf[i] != '\n') {
				i++;
			}
			i++;
			
			while(i<size) {
				StringBuilder sb = new StringBuilder();
				while(buf[i] != '\n' && i<size) {
					sb.append((char)buf[i]);
					i++;
				}
				System.out.println(sb.toString());
				createEdge(sb.toString().split("\t"));
				i++;
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

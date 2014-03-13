package gui;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.swing.JPanel;

import newGraph.*;

public class DrawingPanel extends JPanel {

	private double _centerLat;
	private double _centerLon;
	private double _maxDim;
	private final double ZOOM_OUT = 1.1;
	private double _delta;
	
	private ArrayList<Node> _nodes;
	private ArrayList<Edge> _ways;
	
	private int _idcol;
	private int _namecol;
	private int _srccol;
	private int _dstcol;
	private int _zoom;
	
	public DrawingPanel() throws IOException {
		super();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(750,750));
		this.setBackground(new Color(255,252,173));
		
		double minLat = 41.82;
		double maxLat = 41.829999999999999999999999;
		double maxLon = -71.4;
		double minLon = -71.409999999999999999999999;
		
		_zoom = 8;
		
		this.init(minLat, maxLat, minLon, maxLon);
		
		NodeDictionary nd = new NodeDictionary("smallnodes.tsv");
		_nodes = nd.nodeList();
		//WayDictionary wd = new WayDictionary("smallways.tsv", nd);
		//_ways = wd.wayList();
		RandomAccessFile ways = new RandomAccessFile("smallways.tsv", "r");
		String[] header = ways.readLine().split("\t");
		for(int i=0; i<header.length; i++) {
			if(header[i].equals("id")) {
				_idcol = i;
			}
			if(header[i].equals("start")) {
				_srccol = i;
			}
			if(header[i].equals("end")) {
				_dstcol = i;
			}
			if(header[i].equals("name")){
				_namecol = i;
			}
		}
		_ways = new ArrayList<Edge>();
		while(ways.getFilePointer()< ways.length()){
			String line = ways.readLine();
			String[] l = line.split("\t");
			//System.out.println(l.length);
			if(l.length!=9){
				break;
			}
			Node src = nd.getNode(l[_srccol]);
			Node dst = nd.getNode(l[_dstcol]);
			if(src!=null && dst!=null){
				Edge e = new Edge(_ways.size(), src, dst, l[_namecol], 1);
				_ways.add(e);
			}
		}
		this.updateZoom(8);
		
		//this.update(nd,wd);
		//System.out.println("DONE");
	}

	private void init(double minLat, double maxLat, double minLon, double maxLon) {
		_centerLat = minLat + (maxLat - minLat)/2.0;
		_centerLon = minLon + (maxLon - minLon)/2.0;
		_maxDim = Math.max(maxLat-minLat, maxLon-minLon);
		_delta = _maxDim * ZOOM_OUT;
	}
	
	private double minLat() { return _centerLat - _delta; }
	private double maxLat() { return _centerLat + _delta; }
	private double minLon() { return _centerLon - _delta; }
	private double maxLon() { return _centerLon + _delta; }
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(getBackground());
		System.out.println(g.getColor());
		Graphics2D brush = (Graphics2D) g;
		
		double _maxLat = maxLat();
		double _minLat = minLat();
		double _maxLon = maxLon();
		double _minLon = minLon();
		
		//int counter = 0;
		for(Node n : _nodes){
			//if(counter < 5)
			//System.out.println("NODE: "+n.toString());
			//System.out.println("LAT: "+n.getLat());
			//System.out.println("LON: "+n.getLon());
			n.paint(brush, _maxLat, _minLat, _maxLon, _minLon, this.getHeight(), this.getWidth());
			//counter++;
		}
		//System.out.println(_ways.size());
		for(Edge e : _ways){
			//System.out.println("WAY: "+e.toString());
			e.paint(brush, _maxLat, _minLat, _maxLon, _minLon, this.getHeight(), this.getWidth());
		}
	}
	
	
	public void updateZoom(int zoom){
		_zoom = zoom;
		_delta = (_zoom * _zoom * 1.0)/(8*8)*.5*_maxDim; // set 10 as inital zoom and max zoomed out
		if(_zoom == 8){
			_delta = _delta * ZOOM_OUT;
		}
		
		this.repaint();
	}

}

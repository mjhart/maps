package gui;

import java.awt.*;
import java.io.FileNotFoundException;
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
	
	public DrawingPanel() throws FileNotFoundException {
		super();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(750,750));
		this.setBackground(new Color(255,252,173));
		
		double minLat = 41.82;
		double maxLat = 41.829999999999999999999999;
		double maxLon = -71.4;
		double minLon = -71.409999999999999999999999;
		this.init(minLat, maxLat, minLon, maxLon);
		
		NodeDictionary nd = new NodeDictionary("smallnodes.tsv");
		_nodes = nd.nodeList();
		
		//this.update(nd);
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
			System.out.println("NODE: "+n.toString());
			System.out.println("LAT: "+n.getLat());
			System.out.println("LON: "+n.getLon());
				n.paint(brush, _maxLat, _minLat, _maxLon, _minLon, this.getHeight(), this.getWidth());
			//counter++;
		}
	}
	
	public void update(NodeDictionary nd){
		_nodes = nd.nodeList();
		this.repaint();
	}

}

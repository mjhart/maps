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
	private double _minLat;
	private double _maxLat;
	private double _minLon;
	private double _maxLon;
	
	private ArrayList<Node> _nodes;
	
	public DrawingPanel() throws FileNotFoundException {
		super();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(750,750));
		this.setBackground(new Color(255,252,173));
		
		_minLat = 41.82;
		_maxLat = 41.829999999999999999999999;
		_minLon = 71.4;
		_maxLon = 71.409999999999999999999999;
		this.init();
		
		NodeDictionary nd = new NodeDictionary("smallnodes.tsv");
		_nodes = nd.nodeList();
		this.update(nd);
		//System.out.println("DONE");
	}

	private void init() {
		_centerLat = _minLat + (_maxLat - _minLat)/2.0;
		_centerLon = _minLon + (_maxLon - _minLon)/2.0;
		_maxDim = Math.max(_maxLat-_minLat, _maxLon-_minLon);
		_delta = _maxDim * ZOOM_OUT;
	}
	
	/*public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(getBackground());
		System.out.println(g.getColor());
		Graphics2D brush = (Graphics2D) g;
		int counter = 0;
		for(Node n : _nodes){
			if(counter < 0)
				n.paint(brush, _maxLat, _minLat, _maxLon, _minLon, this.getHeight(), this.getWidth());
			counter++;
		}
	}*/
	
	public void update(NodeDictionary nd){
		_nodes = nd.nodeList();
		this.repaint();
	}

}

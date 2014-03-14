package gui;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JPanel;

import tmp.Controller;

import newGraph.*;

public class AltDrawingPanel extends JPanel {

	private double[] min;
	private double[] max;
	private Controller c;
	
	int zoom = 10;
	
	private HashSet<Node> _nodes;
	private HashSet<Edge> _ways;
	
	public AltDrawingPanel(Controller c) {
		super();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(750,750));
		this.setBackground(new Color(255,252,173));
		
		max = new double[2];
		min = new double[2];
		max[0] = 41.83;
		max[1] = -71.40;
		min[0] = 41.82;
		min[1] = -71.41;
		
		this.c = c;
		
		_nodes = new HashSet<Node>();
		_ways = new HashSet<Edge>();
		
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(getBackground());
		Graphics2D brush = (Graphics2D) g;
		
		_nodes = new HashSet<Node>();
		
		_nodes.addAll(c.nodeBoundingBox(max, min));
		
		System.out.println("Nodes being painted " + _nodes.size());
		System.out.println("Bounding box: " + Arrays.toString(max) + " " + Arrays.toString(min));
		
		brush.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		for(Node n : _nodes){
			n.paint(brush, max[0], min[0], max[1], min[1], this.getHeight(), this.getWidth());
		}
		
		for(Edge e : _ways){
			e.paint(brush, max[0], min[0], max[1], min[1], this.getHeight(), this.getWidth());
		}
	}
	
	public void updateZoom(int zoom){ 
		
		double dif = (this.zoom-zoom)*0.0005;
		max[0]-=dif;
		max[1]-=dif;
		min[0]+=dif;
		min[1]+=dif;
		this.zoom = zoom;
		this.repaint();
	}

}

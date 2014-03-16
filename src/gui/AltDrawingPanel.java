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
		max[0] = 41.8299;
		max[1] = -71.40;
		min[0] = 41.82;
		min[1] = -71.4099;
		
		this.c = c;
		
		_nodes = new HashSet<Node>();
		_ways = new HashSet<Edge>();
		
		PanListener pan = new PanListener(this);
		this.addMouseListener(pan);
		this.addMouseMotionListener(pan);
		
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(getBackground());
		Graphics2D brush = (Graphics2D) g;
		
		_nodes = new HashSet<Node>();
		_ways = new HashSet<Edge>();
		
		c.getData(max, min, _nodes, _ways);
		
		System.out.println("Nodes being painted " + _nodes.size());
		System.out.println("Ways being painted " + _ways.size());
		System.out.println("Bounding box: " + Arrays.toString(max) + " " + Arrays.toString(min));
		
		
		brush.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		for(Node n : _nodes){
			paintNode(brush, n);
		}
		
		for(Edge e : _ways){
			paintWay(brush, e);
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
	
	public void moveWindow(double[] delta) {
		max[0]+=delta[1];
		max[1]-=delta[0];
		min[0]+=delta[1];
		min[1]-=delta[0];
		this.repaint();
	}
	
	private int latToY(double lat){
		//System.out.println("Y: "+(int) ((max-_lat)/(max-min) * scale));
		//System.out.println(max-_lat);
		return (int) ((max[0]-lat)/(max[0]-min[0]) * this.getHeight());
	}
	
	private int lonToX(double lon){
		//System.out.println("Y: "+(int) ((max-_lat)/(max-min) * scale));
		//System.out.println(max-_lat);
		return (int) ((lon - min[1])/(max[1]-min[1]) * this.getWidth());
	}
	
	private void paintNode(Graphics2D brush, Node node) {
		int x = lonToX(node.getLon());
		int y = latToY(node.getLat());
		System.out.println(String.format("X: %d Y: %d", x, y));
		brush.setColor(Color.BLACK);
		brush.drawRect(x,y,1,1);
	}
	
	private void paintWay(Graphics2D brush, Edge way) {
		int x1 = lonToX(way.getSource().getLon());
		int y1 = latToY(way.getSource().getLat());
		int x2 = lonToX(way.getDest().getLon());
		int y2 = latToY(way.getDest().getLat());
		brush.drawLine(x1, y1, x2, y2);
	}
	

}

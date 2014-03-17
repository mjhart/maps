package gui;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.JPanel;

import tmp.Controller;

import newGraph.*;

public class AltDrawingPanel extends JPanel {
	
	public static double TILE_SIZE = 0.05;

	private double[] wMin;
	private double[] wMax;
	private double[] dMax;
	private double[] dMin;
	private Controller c;
	
	private LinkedList<Tile> tiles;
	
	int zoom = 10;
	
	private HashSet<Node> _nodes;
	private HashSet<Edge> _ways;
	
	public AltDrawingPanel(Controller c) {
		super();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(750,750));
		this.setBackground(new Color(255,252,173));
		
		wMax = new double[2];
		wMin = new double[2];
		wMax[0] = -71.40;
		wMax[1] = 41.83;
		wMin[0] = -71.41;
		wMin[1] = 41.82;
		
		//wMax[0] = wMin[0] + TILE_SIZE;
		//wMax[1] = wMax[1] + TILE_SIZE;
		
		dMax = new double[2];
		dMin = new double[2];
		
		this.c = c;
		
		tiles = new LinkedList<Tile>();
		
		loadData();
		
		PanListener pan = new PanListener(this);
		this.addMouseListener(pan);
		this.addMouseMotionListener(pan);
		this.addMouseWheelListener(pan);
		
		
	}
	
	private void loadData() {
		//_nodes = new HashSet<Node>();
		//_ways = new HashSet<Edge>();
		
		
		HashSet<Node> nodes;
		HashSet<Edge> ways;
		
		dMin[0] = wMin[0] - (wMin[0]%TILE_SIZE) - TILE_SIZE;
		dMin[1] = wMin[1] - (wMin[1]%TILE_SIZE) - TILE_SIZE;
		dMax[0] = wMax[0] + 2*TILE_SIZE;
		dMax[1] = wMax[1] + 2*TILE_SIZE;
		
		for(int i=0; i<tiles.size(); i++) {
			if(!tiles.get(i).intersects(dMax, dMin)) {
				tiles.remove(i);
			}
		}
		
		for(double i=wMin[0]-(wMin[0]%TILE_SIZE)-TILE_SIZE; i<wMax[0]+TILE_SIZE; i+=TILE_SIZE) {
			for(double j=wMin[1]-(wMin[1]%TILE_SIZE)-TILE_SIZE; j<wMax[1]+TILE_SIZE; j+=TILE_SIZE) {
				for(Tile t : tiles) {
					if(t._min[0] == i && t._min[1] == j) {
						continue;
					}
				}
				
				System.out.println(String.format("x: %f y: %f", i, j));
				double[] min = {i, j};
				double[] max = {i+TILE_SIZE, j+TILE_SIZE};
				nodes = new HashSet<Node>();
				ways = new HashSet<Edge>();
				c.getData(max, min, nodes, ways);
				tiles.add(new Tile(max, min, nodes, ways));
			}
		}
		
		
		dMax[0] = 2*wMax[0] - wMin[0];
		dMax[1] = 2*wMax[1] - wMin[1];
		dMin[0] = 2*wMin[0] - wMax[0];
		dMin[1] = 2*wMin[1] - wMax[1];
		
		//c.getData(dMax, dMin, _nodes, _ways);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(getBackground());
		Graphics2D brush = (Graphics2D) g;
		
		/*
		if(wMax[0] > dMax[0] || wMax[1] > dMax[1] || wMin[0] < dMin[0] || wMin[1] < wMin[1]) {
			System.out.println("Loading data");
			loadData();
		}
		
		
		System.out.println("Nodes being painted " + _nodes.size());
		System.out.println("Ways being painted " + _ways.size());
		System.out.println("Bounding box: " + Arrays.toString(wMax) + " " + Arrays.toString(wMin));
		
		
		brush.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		for(Node n : _nodes){
			paintNode(brush, n);
		}
		
		for(Edge e : _ways){
			paintWay(brush, e);
		}
		*/
		if(wMax[0] > dMax[0] || wMax[1] > dMax[1] || wMin[0] < dMin[0] || wMin[1] < wMin[1]) {
			System.out.println("Loading data");
			loadData();
		}
		
		brush.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		for(Tile t : tiles) {
			for(Node n : t.nodes){
				paintNode(brush, n);
			}
			
			for(Edge e : t.ways){
				paintWay(brush, e);
			}
		}
		
	}
	
	public void updateZoom(int zoom){ 
		
		if(zoom > 0) {

			double dif = (this.zoom-zoom)*0.0005;
			wMax[0]-=dif;
			wMax[1]-=dif;
			wMin[0]+=dif;
			wMin[1]+=dif;
			this.zoom = zoom;
			this.repaint();
		}
	}
	
	public void zoomIn() {
		updateZoom(this.zoom-1);
	}
	
	public void zoomOut() {
		updateZoom(this.zoom+1);
	}
	
	public void moveWindow(double[] delta) {
		
		double dx = delta[0]*(wMax[0]-wMin[0])/this.getWidth();
		double dy = delta[1]*(wMax[1]-wMin[1])/this.getHeight();
		
		wMax[0]+=dx;
		wMax[1]-=dy;
		wMin[0]+=dx;
		wMin[1]-=dy;

		this.repaint();
	}
	
	private int latToY(double lat) {
		//System.out.println("Y: "+(int) ((max-_lat)/(max-min) * scale));
		//System.out.println(max-_lat);
		return (int) ((wMax[1]-lat)/(wMax[1]-wMin[1]) * this.getHeight());
	}
	
	private int lonToX(double lon) {
		//System.out.println("Y: "+(int) ((max-_lat)/(max-min) * scale));
		//System.out.println(max-_lat);
		return (int) ((lon - wMin[0])/(wMax[0]-wMin[0]) * this.getWidth());
	}
	
	private double xToLon(double x) {
		return (double) (x*(wMax[0]-wMin[0])/this.getWidth() + wMin[0]);
	}
	
	private double yToLat(double y) {
		return (double) (wMax[1] - y*(wMax[1]-wMin[1])/this.getHeight());
	}
	
	private void paintNode(Graphics2D brush, Node node) {
		int x = lonToX(node.getLon());
		int y = latToY(node.getLat());
		//System.out.println(String.format("X: %d Y: %d", x, y));
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

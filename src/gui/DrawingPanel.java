package gui;

import graph.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JPanel;

import map.Controller;


/**
 * This class handles much of the logic of the gui
 * as well as drawing all the data.
 * 
 * @author mjhart
 *
 */
public class DrawingPanel extends JPanel {
	
	// data defining tile coordinate system
	public static double TILE_SIZE = 0.001;
	public static double LON_INIT = -71.41;
	public static double LAT_INIT = 41.82;

	// bounding boxes for window and data
	private double[] wMin;
	private double[] wMax;
	private int[] dMax;
	private int[] dMin;
	
	private Controller c;
	
	private Node _start;
	private Node _end;
	private List<Node> _path;
	
	private List<Tile> tiles;
	private LinkedBlockingQueue<Tile> _tileQueue;
	private TileLoader _tileLoader;
	
	public boolean load = false;
	
	int zoom = 10;
	
	private HashSet<Node> _nodes;
	private HashSet<Edge> _ways;
	private InputPanel _ip;
	
	public DrawingPanel(Controller c) {
		super();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(750,750));
		this.setBackground(new Color(204,204,255));
		//this.setOpaque(false);
		
		wMax = new double[2];
		wMin = new double[2];
		wMax[0] = -71.40;
		wMax[1] = 41.83;
		wMin[0] = -71.41;
		wMin[1] = 41.82;
		
		dMax = new int[2];
		dMin = new int[2];
		
		this.c = c;
		
		tiles = Collections.synchronizedList(new LinkedList<Tile>());
		_tileQueue = new LinkedBlockingQueue<Tile>();
		_tileLoader = new TileLoader(_tileQueue, tiles, c, this);
		_tileLoader.start();
		
		loadData();
		
		PanListener pan = new PanListener(this);
		this.addMouseListener(pan);
		this.addMouseMotionListener(pan);
		this.addMouseWheelListener(pan);
		
		_path = new LinkedList<Node>();
		_ip = new InputPanel(this, c);
	}
	
	public void loadData() {
		
		dMin[0] = lonToTx(wMin[0] - TILE_SIZE/2);
		dMin[1] = latToTy(wMin[1] - TILE_SIZE/2);
		dMax[0] = lonToTx(wMax[0] + 3*TILE_SIZE/2);
		dMax[1] = latToTy(wMax[1] + 3*TILE_SIZE/2);
		
		synchronized (tiles) {
			List<Tile> toRemove = new LinkedList<Tile>();
			for(int i=0; i<tiles.size(); i++) {
				if(!tiles.get(i).intersects(dMax, dMin)) {
					toRemove.add(tiles.get(i));
				}
			}
			tiles.removeAll(toRemove);
		}
		
		
		LinkedList<Tile> temp = new LinkedList<Tile>();
		for(int i=dMin[0]; i<dMax[0]; i++) {
			for(int j=dMin[1]; j<dMax[1]; j++) {
				
				boolean inCache = false;
				synchronized (tiles) {
					for(Tile t : tiles) {
						if(t.x == i && t.y == j) {
							inCache = true;
							break;
						}
					}
				}
				if(!inCache) {
					temp.add(new Tile(i, j, new HashSet<Node>(), new HashSet<Edge>()));
				}
			}
		}
		
		for(Tile t : temp) {
			tiles.add(t);
			_tileQueue.add(t);
		}
	}
	
	public static int lonToTx(double lon) {
		return (int) Math.floor(((lon - LON_INIT)/TILE_SIZE));
	}
	
	public static int latToTy(double lat) {
		return (int) Math.floor(((lat - LAT_INIT)/TILE_SIZE));
	}
	
	public static double txToLon(int x) {
		return (double) (x*TILE_SIZE + LON_INIT);
	}
	
	public static double tyToLat(int y) {
		return (double) (y*TILE_SIZE + LAT_INIT);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(getBackground());
		Graphics2D brush = (Graphics2D) g;
		
		if(load) {
			if(wMax[0] > txToLon(dMax[0]) || wMax[1] > tyToLat(dMax[1]) || 
					wMin[0] < txToLon(dMin[0]) || wMin[1] < tyToLat(dMin[1])) {
				loadData();
			}
		}
		
		brush.clearRect(0, 0, this.getWidth(), this.getHeight());
		brush.setColor(new Color(204, 204, 255));
		brush.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		//Stroke wayStroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
		//Stroke nodeStroke = new BasicStroke(3);
		
		synchronized (tiles) {
			for(Tile t : tiles) {

				if(t.isLoaded()) {
					
					
					/*   Debugging info
					//brush.setStroke(nodeStroke);
					Composite tmp = brush.getComposite();
					brush.setColor(java.awt.Color.RED);
					brush.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
					brush.drawRect(lonToX(t.getMinLon()), latToY(t.getMaxLat()),(int) (TILE_SIZE/(wMax[0]-wMin[0])*this.getWidth()),(int) (TILE_SIZE/(wMax[1]-wMin[1])*this.getHeight()));
					brush.fillRect(lonToX(t.getMinLon()), latToY(t.getMaxLat()),(int) (TILE_SIZE/(wMax[0]-wMin[0])*this.getWidth()),(int) (TILE_SIZE/(wMax[1]-wMin[1])*this.getHeight()));
					brush.setColor(java.awt.Color.BLUE);
					brush.drawRect(lonToX(txToLon(dMin[0])), latToY(tyToLat(dMax[1])), lonToX(txToLon(dMax[0]))-lonToX(txToLon(dMin[0])),latToY(tyToLat(dMin[1]))-latToY(tyToLat(dMax[1])));
					brush.drawRect(lonToX(txToLon(0)), latToY(tyToLat(0)), 5, 5);
					brush.setColor(java.awt.Color.GREEN);
					String str = "(" + t.x +"," + t.y + ")";
					brush.drawString(str, lonToX(t.getMinLon()), latToY(t.getMinLat()));
					brush.setComposite(tmp);
					*/
					
					brush.setColor(java.awt.Color.BLACK);
					for(Node n : t.nodes){
						paintNode(brush, n);
					}

					for(Edge e : t.ways){
						paintWay(brush, e);
					}
				}
			}
		}
		if(_start != null) {
			brush.setColor(java.awt.Color.GREEN);
			brush.fillOval(lonToX(_start.getLon())-2, latToY(_start.getLat())-2, 5, 5);
		}
		if(_end!=null){
			brush.setColor(java.awt.Color.RED);
			brush.fillOval(lonToX(_end.getLon())-2, latToY(_end.getLat())-2, 5, 5);
		}
		
		synchronized(_path) {
			if(_path!=null){
				brush.setColor(java.awt.Color.GREEN);
				for(int i = 1; i < _path.size(); i++){
					brush.drawLine(lonToX(_path.get(i-1).getLon()), latToY(_path.get(i-1).getLat()), lonToX(_path.get(i).getLon()), latToY(_path.get(i).getLat()));
				}
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
		return (int) ((wMax[1]-lat)/(wMax[1]-wMin[1]) * this.getHeight());
	}
	
	private int lonToX(double lon) {
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
		brush.drawRect(x,y,1,1);
	}
	
	private void paintWay(Graphics2D brush, Edge way) {
		int x1 = lonToX(way.getSource().getLon());
		int y1 = latToY(way.getSource().getLat());
		int x2 = lonToX(way.getDest().getLon());
		int y2 = latToY(way.getDest().getLat());
		brush.drawLine(x1, y1, x2, y2);
	}
	
	public void clickAt(int x, int y) {
		double[] coords = {xToLon(x), yToLat(y)};
		if(_start==null){
			_start = c.nearestNeighbor(coords);
		}
		else{
			_end = _start;
			_start = c.nearestNeighbor(coords);
		}
		this.repaint();
	}

	public void startSearch(Node src, Node dst) {
		PathFinder pf = new PathFinder(src.toString(), dst.toString(), this, c, _ip);
		pf.start();
	}

	public Node getStart() {
		return _start;
	}

	public Node getEnd() {
		return _end;
	}

	public List<Node> getPath() {
		return _path;
	}
	
	public void setIp(InputPanel ip){
		_ip = ip;
	}

}

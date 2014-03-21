package gui;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.SwingUtilities;

import map.Controller;


import gui.Tile;

/**
 * @author mjhart
 * Worker thread which takes tiles off of 
 * a shared blocking queue and fills them 
 * with data
 */
public class TileLoader extends Thread {
	
	private BlockingQueue<Tile> _tileQueue;
	private Controller _c;
	private DrawingPanel _dp;
	
	public TileLoader(BlockingQueue<Tile> tq, List<Tile> tl, Controller c, DrawingPanel dp) {
		_tileQueue = tq;
		_c = c;
		_dp = dp;
	}
	
	/**
	 * Takes Tiles off of the queue and fills them 
	 * with data
	 */
	public void run() {
		while(true) {
			try {
				// take next tile
				Tile t = _tileQueue.take();
				
				// get bounding box
				double[] max = {t.getMaxLon(), t.getMaxLat()};
				double[] min = {t.getMinLon(), t.getMinLat()};
				
				// get data
				_c.getData(max, min, t.nodes, t.ways);
				
				// set tile to be loaded
				t.setLoaded();
				
				// tell swing to repaint
				SwingUtilities.invokeLater(paint);
			} catch (InterruptedException e) {}
			
		}
	}
	
	private Runnable paint = new Runnable() {
		public void run() {
			_dp.repaint();
		}
	};
}
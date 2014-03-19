package gui;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.SwingUtilities;

import tmp.Controller;

import gui.Tile;

public class TileLoader extends Thread {
	
	private BlockingQueue<Tile> _tileQueue;
	private List<Tile> _tileList;
	private Controller _c;
	private AltDrawingPanel _dp;
	
	public TileLoader(BlockingQueue<Tile> tq, List<Tile> tl, Controller c, AltDrawingPanel dp) {
		_tileQueue = tq;
		_c = c;
		_tileList = tl;
		_dp = dp;
	}
	
	public void run() {
		while(true) {
			try {
				Tile t = _tileQueue.take();
				double[] max = {t.getMaxLon(), t.getMaxLat()};
				double[] min = {t.getMinLon(), t.getMinLat()};
				_c.getData(max, min, t.nodes, t.ways);
				synchronized (_tileList) {
					_tileList.add(t);
				}
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
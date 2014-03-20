package gui;

import java.io.IOException;

import javax.swing.SwingUtilities;

import map.Controller;


public class PathFinder extends Thread {
	
	private String _src;
	private String _dst; 
	private DrawingPanel _dp;
	Controller _c;
	
	
	public PathFinder(String src, String dst, DrawingPanel dp, Controller c) {
		_src = src;
		_dst = dst;
		_dp = dp;
		_c = c;
	}
	
	@Override
	public void run() {
		synchronized(_dp.getPath()) {
			try {
				_dp.getPath().removeAll(_dp.getPath());
				_dp.getPath().addAll(_c.getPath(_src, _dst));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SwingUtilities.invokeLater(paint);
	}
	
	private Runnable paint = new Runnable() {
		public void run() {
			_dp.repaint();
		}
	};

}
